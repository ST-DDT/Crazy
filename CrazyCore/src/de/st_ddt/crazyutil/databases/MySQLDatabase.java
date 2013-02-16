package de.st_ddt.crazyutil.databases;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import org.bukkit.configuration.ConfigurationSection;

public class MySQLDatabase<S extends MySQLDatabaseEntry> extends BasicDatabase<S>
{

	protected final String tableName;
	private final SQLConnection connection;
	protected final SQLConnectionPool connectionPool;
	protected final SQLColumn[] columns;
	protected final String[] columnNames;
	private final boolean cached;
	private final boolean doNotUpdate;

	public MySQLDatabase(final Class<S> clazz, final SQLColumn[] columns, final String defaultTableName, final ConfigurationSection config)
	{
		super(DatabaseType.MYSQL, clazz, convertColumnNames(columns));
		if (config == null)
		{
			this.tableName = defaultTableName;
			this.connection = MySQLConnection.getConnection(null);
			this.connectionPool = new MySQLConnectionPool(connection);
			this.columns = columns;
			this.columnNames = defaultColumnNames;
			this.cached = true;
			this.doNotUpdate = false;
		}
		else
		{
			this.tableName = config.getString("tableName", defaultTableName);
			this.connection = MySQLConnection.getConnection(config.getConfigurationSection("connection"));
			this.connectionPool = new MySQLConnectionPool(connection);
			this.columns = columns;
			this.columnNames = new String[defaultColumnNames.length];
			for (int i = 0; i < defaultColumnNames.length; i++)
			{
				columnNames[i] = config.getString("columns." + defaultColumnNames[i], defaultColumnNames[i]);
				columns[i].setRealName(columnNames[i]);
			}
			this.cached = config.getBoolean("cached", true);
			this.doNotUpdate = config.getBoolean("static", false);
		}
	}

	public MySQLDatabase(final Class<S> clazz, final SQLColumn[] columns, final String tableName, final String[] columnNames, final String host, final String port, final String database, final String user, final String password, final boolean cached, final boolean doNoUpdate)
	{
		super(DatabaseType.MYSQL, clazz, convertColumnNames(columns));
		this.tableName = tableName;
		this.connection = new MySQLConnection(host, port, database, user, password);
		this.connectionPool = new MySQLConnectionPool(connection);
		this.columns = columns;
		this.columnNames = columnNames;
		for (int i = 0; i < columns.length; i++)
			columns[i].setRealName(columnNames[i]);
		this.cached = cached;
		this.doNotUpdate = doNoUpdate;
	}

	@Override
	protected Constructor<S> getConstructor(final Class<S> clazz)
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

	private static String[] convertColumnNames(final SQLColumn[] columns)
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

	public final SQLColumn[] getColumns()
	{
		return columns;
	}

	public final SQLConnection getConnection()
	{
		return connection;
	}

	public final SQLConnectionPool getConnectionpool()
	{
		return connectionPool;
	}

	@Override
	public void checkTable() throws Exception
	{
		final Connection connection = connectionPool.getConnection();
		if (connection == null)
			throw new Exception("Database not accessible!");
		Statement query = null;
		try
		{
			// Create Table if not exists
			query = connection.createStatement();
			query.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " (" + SQLColumn.getFullCreateString(columns) + ");");
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
			for (final SQLColumn column : columns)
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
			connectionPool.releaseConnection(connection);
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
		else
			return loadEntry(key) != null;
	}

	protected boolean containsEntry(final String key)
	{
		System.out.println(System.currentTimeMillis() + " MySQLDatabase.containsEntry() " + key);
		final Connection connection = connectionPool.getConnection();
		if (connection == null)
			return false;
		Statement query = null;
		try
		{
			System.out.println(System.currentTimeMillis() + " MySQLDatabase.containsEntry() " + 1);
			query = connection.createStatement();
			final ResultSet result = query.executeQuery("SELECT `" + columnNames[0] + "` FROM `" + tableName + "` WHERE " + columnNames[0] + "='" + key + "' LIMIT 1");
			return result.next();
		}
		catch (final SQLException e)
		{
			return false;
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
			connectionPool.releaseConnection(connection);
			System.out.println(System.currentTimeMillis() + " MySQLDatabase.containsEntry() " + 2);
		}
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
		final Connection connection = connectionPool.getConnection();
		if (connection == null)
			return null;
		Statement query = null;
		try
		{
			query = connection.createStatement();
			final ResultSet result = query.executeQuery("SELECT * FROM `" + tableName + "` WHERE " + columnNames[0] + "='" + key + "' LIMIT 1");
			if (result.next())
				try
				{
					final S data = constructor.newInstance(result, columnNames);
					datas.put(data.getName().toLowerCase(), data);
					result.close();
					return data;
				}
				catch (final InvocationTargetException e)
				{
					System.err.println("Error occured while trying to load entry: " + key);
					shortPrintStackTrace(e, e.getCause());
				}
				catch (final Exception e)
				{
					System.err.println("Error occured while trying to load entry: " + key);
					e.printStackTrace();
				}
			result.close();
			return null;
		}
		catch (final SQLException e)
		{
			return null;
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
			connectionPool.releaseConnection(connection);
		}
	}

	@Override
	public void loadAllEntries()
	{
		final Connection connection = connectionPool.getConnection();
		if (connection == null)
			return;
		Statement query = null;
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
				catch (final InvocationTargetException e)
				{
					shortPrintStackTrace(e, e.getCause());
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
			result.close();
		}
		catch (final SQLException e)
		{}
		finally
		{
			if (query != null)
				try
				{
					query.close();
				}
				catch (final Exception e)
				{}
			connectionPool.releaseConnection(connection);
		}
	}

	@Override
	public boolean deleteEntry(final String key)
	{
		final Connection connection = connectionPool.getConnection();
		if (connection == null)
			return false;
		Statement query = null;
		try
		{
			query = connection.createStatement();
			query.executeUpdate("DELETE FROM `" + tableName + "` WHERE " + columnNames[0] + "='" + key + "'");
		}
		catch (final SQLException e)
		{}
		finally
		{
			if (query != null)
				try
				{
					query.close();
				}
				catch (final SQLException e)
				{}
			connectionPool.releaseConnection(connection);
		}
		return super.deleteEntry(key);
	}

	@Override
	public void save(final S entry)
	{
		if (entry == null)
			return;
		super.save(entry);
		final String sql;
		if (containsEntry(entry.getName()))
			sql = "UPDATE `" + tableName + "` SET " + entry.saveToMySQLDatabase(columnNames) + " WHERE " + columnNames[0] + "='" + entry.getName() + "'";
		else
			sql = "INSERT INTO `" + tableName + "` SET " + columnNames[0] + "='" + entry.getName() + "', " + entry.saveToMySQLDatabase(columnNames);
		final Connection connection = connectionPool.getConnection();
		if (connection == null)
			return;
		Statement query = null;
		try
		{
			query = connection.createStatement();
			query.executeUpdate(sql);
		}
		catch (final SQLException e)
		{}
		finally
		{
			if (query != null)
				try
				{
					query.close();
				}
				catch (final SQLException e)
				{}
			connectionPool.releaseConnection(connection);
		}
	}

	@Override
	public void purgeDatabase()
	{
		final Connection connection = connectionPool.getConnection();
		if (connection == null)
			return;
		Statement query = null;
		try
		{
			query = connection.createStatement();
			query.executeUpdate("DELETE FROM `" + tableName + "`");
		}
		catch (final SQLException e)
		{}
		finally
		{
			if (query != null)
				try
				{
					query.close();
				}
				catch (final SQLException e)
				{}
			connectionPool.releaseConnection(connection);
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
		connection.save(config, path + "MYSQL.connection.");
		config.set(path + "MYSQL.tableName", tableName);
		config.set(path + "MYSQL.cached", cached);
		config.set(path + "MYSQL.static", doNotUpdate);
		for (int i = 0; i < defaultColumnNames.length; i++)
			config.set(path + "MYSQL.columns." + defaultColumnNames[i], columnNames[i]);
	}
}
