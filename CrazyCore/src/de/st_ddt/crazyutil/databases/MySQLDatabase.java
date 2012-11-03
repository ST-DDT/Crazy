package de.st_ddt.crazyutil.databases;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import org.bukkit.configuration.ConfigurationSection;

public class MySQLDatabase<S extends MySQLDatabaseEntry> extends BasicDatabase<S>
{

	private final String tableName;
	private final MySQLConnection mysqlConnection;
	private final MySQLConnectionPool mysqlConnectionPool;
	private final MySQLColumn[] columns;
	private final String[] columnNames;
	private final boolean cached;
	private final boolean doNotUpdate;

	public MySQLDatabase(final Class<S> clazz, final MySQLColumn[] columns, final String defaultTableName, final ConfigurationSection config)
	{
		super(DatabaseType.MYSQL, clazz, getConstructor(clazz), convertColumnNames(columns));
		if (config == null)
		{
			this.tableName = defaultTableName;
			this.mysqlConnection = MySQLConnection.getConnection(null);
			this.mysqlConnectionPool = new MySQLConnectionPool(mysqlConnection);
			this.columns = columns;
			this.columnNames = defaultColumnNames;
			this.cached = true;
			this.doNotUpdate = false;
		}
		else
		{
			this.tableName = config.getString("tableName", defaultTableName);
			this.mysqlConnection = MySQLConnection.getConnection(config.getConfigurationSection("connection"));
			this.mysqlConnectionPool = new MySQLConnectionPool(mysqlConnection);
			this.columns = columns;
			this.columnNames = new String[defaultColumnNames.length];
			for (int i = 0; i < defaultColumnNames.length; i++)
			{
				columnNames[i] = config.getString("columns." + defaultColumnNames[i], defaultColumnNames[i]);
				columns[i].setRealName(columnNames[i]);
			}
			this.cached = config.getBoolean("cached", true);
			this.doNotUpdate = config.getBoolean("static", true);
		}
	}

	public MySQLDatabase(final Class<S> clazz, final MySQLColumn[] columns, final String tableName, final String[] columnNames, final String host, final String port, final String database, final String user, final String password, final boolean cached, final boolean doNoUpdate)
	{
		super(DatabaseType.MYSQL, clazz, getConstructor(clazz), convertColumnNames(columns));
		this.tableName = tableName;
		this.mysqlConnection = new MySQLConnection(host, port, database, user, password);
		this.mysqlConnectionPool = new MySQLConnectionPool(mysqlConnection);
		this.columns = columns;
		this.columnNames = columnNames;
		for (int i = 0; i < columns.length; i++)
			columns[i].setRealName(columnNames[i]);
		this.cached = cached;
		this.doNotUpdate = doNoUpdate;
	}

	private static <S> Constructor<S> getConstructor(final Class<S> clazz)
	{
		try
		{
			return clazz.getConstructor(ResultSet.class, String[].class);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static String[] convertColumnNames(final MySQLColumn[] columns)
	{
		final int length = columns.length;
		final String[] names = new String[length];
		for (int i = 0; i < length; i++)
			names[i] = columns[i].getName();
		return names;
	}

	public static String readName(final ResultSet rawData, final String colName)
	{
		try
		{
			return rawData.getString(colName);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return "ERROR";
		}
	}

	public final MySQLColumn[] getColumns()
	{
		return columns;
	}

	public final MySQLConnection getConnection()
	{
		return mysqlConnection;
	}

	public final MySQLConnectionPool getConnectionpool()
	{
		return mysqlConnectionPool;
	}

	@Override
	public void checkTable() throws Exception
	{
		Statement query = null;
		final Connection connection = mysqlConnectionPool.getConnection();
		try
		{
			// Create Table if not exists
			query = connection.createStatement();
			query.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " (" + MySQLColumn.getFullCreateString(columns) + ");");
			query.close();
			// Create columns if not exist
			query = null;
			final HashSet<String> columnsNames = new HashSet<String>();
			query = connection.createStatement();
			// Vorhandene Spalten abfragen
			final ResultSet result = query.executeQuery("SHOW COLUMNS FROM " + tableName);
			while (result.next())
				columnsNames.add(result.getString("Field"));
			query.close();
			query = null;
			for (final MySQLColumn column : columns)
			{
				// Prüfen ob Spalte vorhanden ist
				if (columnsNames.contains(column.getName()))
					continue;
				System.out.println("ADDED COLUMN " + column.getName() + " TO TABLE " + tableName);
				query = null;
				// Spalte hinzufügen
				query = connection.createStatement();
				query.executeUpdate("ALTER TABLE " + tableName + " ADD " + column.getCreateString(true));
				query.close();
			}
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			if (query != null)
				try
				{
					query.close();
				}
				catch (final SQLException e)
				{}
			mysqlConnectionPool.releaseConnection(connection);
		}
	}

	@Override
	public void initialize() throws Exception
	{
		checkTable();
		if (cached)
			loadAllEntries();
	}

	@Override
	public final boolean isStaticDatabase()
	{
		return doNotUpdate;
	}

	@Override
	public final boolean isCachedDatabase()
	{
		return cached;
	}

	@Override
	public boolean hasEntry(final String key)
	{
		if (super.hasEntry(key))
			return true;
		return loadEntry(key) != null;
	}

	protected boolean containsEntry(final String key)
	{
		boolean res = false;
		Statement query = null;
		final Connection connection = mysqlConnectionPool.getConnection();
		try
		{
			query = connection.createStatement();
			final ResultSet result = query.executeQuery("SELECT `" + columnNames[0] + "` FROM `" + tableName + "` WHERE " + columnNames[0] + "='" + key + "' LIMIT 1");
			if (result.next())
				res = true;
			result.close();
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (query != null)
				try
				{
					query.close();
				}
				catch (final Exception e)
				{}
			mysqlConnectionPool.releaseConnection(connection);
		}
		return res;
	}

	@Override
	public S updateEntry(final String key)
	{
		if (doNotUpdate)
			return getEntry(key);
		else
			return loadEntry(key);
	}

	@Override
	public S loadEntry(final String key)
	{
		S data = null;
		Statement query = null;
		final Connection connection = mysqlConnectionPool.getConnection();
		try
		{
			query = connection.createStatement();
			final ResultSet result = query.executeQuery("SELECT * FROM `" + tableName + "` WHERE " + columnNames[0] + "='" + key + "' LIMIT 1");
			if (result.next())
				try
				{
					data = constructor.newInstance(result, columnNames);
					datas.put(data.getName().toLowerCase(), data);
				}
				catch (final Exception e)
				{
					data = null;
					e.printStackTrace();
				}
			result.close();
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (query != null)
				try
				{
					query.close();
				}
				catch (final Exception e)
				{}
			mysqlConnectionPool.releaseConnection(connection);
		}
		return data;
	}

	@Override
	public void loadAllEntries()
	{
		Statement query = null;
		final Connection connection = mysqlConnectionPool.getConnection();
		try
		{
			query = connection.createStatement();
			final ResultSet result = query.executeQuery("SELECT * FROM `" + tableName + "` WHERE 1=1");
			while (result.next())
				try
				{
					final S data = constructor.newInstance(result, columnNames);
					datas.put(data.getName().toLowerCase(), data);
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
			result.close();
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (query != null)
				try
				{
					query.close();
				}
				catch (final Exception e)
				{}
			mysqlConnectionPool.releaseConnection(connection);
		}
	}

	@Override
	public boolean deleteEntry(final String key)
	{
		Statement query = null;
		final Connection connection = mysqlConnectionPool.getConnection();
		try
		{
			query = connection.createStatement();
			query.executeUpdate("DELETE FROM `" + tableName + "` WHERE " + columnNames[0] + "='" + key + "'");
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (query != null)
				try
				{
					query.close();
				}
				catch (final SQLException e)
				{}
			mysqlConnectionPool.releaseConnection(connection);
		}
		return super.deleteEntry(key);
	}

	@Override
	public void save(final S entry)
	{
		super.save(entry);
		String sql = null;
		if (containsEntry(entry.getName()))
			sql = "UPDATE `" + tableName + "` SET " + entry.saveToMySQLDatabase(columnNames) + " WHERE " + columnNames[0] + "='" + entry.getName() + "'";
		else
			sql = "INSERT INTO `" + tableName + "` SET " + columnNames[0] + "='" + entry.getName() + "', " + entry.saveToMySQLDatabase(columnNames);
		final Connection connection = mysqlConnectionPool.getConnection();
		Statement query = null;
		try
		{
			query = connection.createStatement();
			query.executeUpdate(sql);
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (query != null)
				try
				{
					query.close();
				}
				catch (final SQLException e)
				{}
			mysqlConnectionPool.releaseConnection(connection);
		}
	}

	@Override
	public void purgeDatabase()
	{
		Statement query = null;
		final Connection connection = mysqlConnectionPool.getConnection();
		try
		{
			query = connection.createStatement();
			query.executeUpdate("DELETE FROM `" + tableName + "`");
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (query != null)
				try
				{
					query.close();
				}
				catch (final SQLException e)
				{}
			mysqlConnectionPool.releaseConnection(connection);
		}
		super.purgeDatabase();
	}

	@Override
	public void saveDatabase()
	{
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		mysqlConnection.save(config, path + "MYSQL.connection.");
		config.set(path + "MYSQL.tableName", tableName);
		config.set(path + "MYSQL.cached", cached);
		config.set(path + "MYSQL.static", doNotUpdate);
		for (int i = 0; i < defaultColumnNames.length; i++)
			config.set(path + "MYSQL.columns." + defaultColumnNames[i], columnNames[i]);
	}
}
