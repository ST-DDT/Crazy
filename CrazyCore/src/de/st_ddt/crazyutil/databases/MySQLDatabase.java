package de.st_ddt.crazyutil.databases;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import org.bukkit.configuration.ConfigurationSection;

public class MySQLDatabase<S extends MySQLDatabaseEntry> extends BasicDatabase<S>
{

	private final String tableName;
	private final MySQLConnection connection;
	private final MySQLColumn[] columns;
	private final String[] columnNames;
	private final boolean cached;

	public MySQLDatabase(final Class<S> clazz, final MySQLColumn[] columns, final String defaultTableName, final ConfigurationSection config)
	{
		super(DatabaseType.MYSQL, clazz, getConstructor(clazz), convertColumnNames(columns));
		if (config == null)
		{
			this.tableName = defaultTableName;
			this.connection = MySQLConnection.getConnection(null);
			this.columns = columns;
			this.columnNames = defaultColumnNames;
			this.cached = true;
		}
		else
		{
			this.tableName = config.getString("tablename", defaultTableName);
			this.connection = MySQLConnection.getConnection(config.getConfigurationSection("connection"));
			this.columns = columns;
			this.columnNames = new String[defaultColumnNames.length];
			for (int i = 0; i < defaultColumnNames.length; i++)
			{
				columnNames[i] = config.getString("columns." + defaultColumnNames[i], defaultColumnNames[i]);
				columns[i].setRealName(columnNames[i]);
			}
			this.cached = config.getBoolean("cached", true);
		}
	}

	public MySQLDatabase(final Class<S> clazz, final MySQLColumn[] columns, final String tableName, final String[] columnNames, final String host, final String port, final String database, final String user, final String password, final boolean cached)
	{
		super(DatabaseType.MYSQL, clazz, getConstructor(clazz), convertColumnNames(columns));
		this.tableName = tableName;
		this.connection = new MySQLConnection(host, port, database, user, password);
		this.columns = columns;
		this.columnNames = columnNames;
		for (int i = 0; i < columns.length; i++)
			columns[i].setRealName(columnNames[i]);
		this.cached = cached;
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
		return connection;
	}

	@Override
	public void checkTable() throws Exception
	{
		Statement query = null;
		try
		{
			// Create Table if not exists
			query = connection.getConnection().createStatement();
			query.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " (" + MySQLColumn.getFullCreateString(columns) + ");");
			query.close();
			// Create columns if not exist
			query = null;
			final HashSet<String> columnsNames = new HashSet<String>();
			query = connection.getConnection().createStatement();
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
				query = connection.getConnection().createStatement();
				query.executeUpdate("ALTER TABLE " + tableName + " ADD " + column.getCreateString(true));
				query.close();
				connection.closeConnection();
			}
		}
		catch (final SQLException e)
		{
			connection.closeConnection();
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void initialize() throws Exception
	{
		connection.connect();
		checkTable();
		if (cached)
			loadAllEntries();
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
		ResultSet result = null;
		try
		{
			query = connection.getConnection().createStatement();
			result = query.executeQuery("SELECT `" + columnNames[0] + "` FROM `" + tableName + "` WHERE " + columnNames[0] + "='" + key + "' LIMIT 1");
			if (result.next())
				res = true;
			query.close();
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (result != null)
				try
				{
					result.close();
				}
				catch (final Exception e)
				{}
			if (query != null)
				try
				{
					query.close();
				}
				catch (final Exception e)
				{}
			connection.closeConnection();
		}
		return res;
	}

	@Override
	public S updateEntry(final String key)
	{
		return loadEntry(key);
	}

	@Override
	public S loadEntry(final String key)
	{
		S data = null;
		Statement query = null;
		ResultSet result = null;
		try
		{
			query = connection.getConnection().createStatement();
			result = query.executeQuery("SELECT * FROM `" + tableName + "` WHERE " + columnNames[0] + "='" + key + "' LIMIT 1");
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
			query.close();
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (result != null)
				try
				{
					result.close();
				}
				catch (final Exception e)
				{}
			if (query != null)
				try
				{
					query.close();
				}
				catch (final Exception e)
				{}
			connection.closeConnection();
		}
		return data;
	}

	@Override
	public void loadAllEntries()
	{
		Statement query = null;
		ResultSet result = null;
		try
		{
			query = connection.getConnection().createStatement();
			result = query.executeQuery("SELECT * FROM `" + tableName + "` WHERE 1=1");
			try
			{
				while (result.next())
				{
					final S data = constructor.newInstance(result, columnNames);
					datas.put(data.getName().toLowerCase(), data);
				}
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (result != null)
				try
				{
					result.close();
				}
				catch (final Exception e)
				{}
			if (query != null)
				try
				{
					query.close();
				}
				catch (final Exception e)
				{}
			connection.closeConnection();
		}
	}

	@Override
	public boolean deleteEntry(final String key)
	{
		Statement query = null;
		try
		{
			query = connection.getConnection().createStatement();
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
			connection.closeConnection();
		}
		return super.deleteEntry(key);
	}

	@Override
	public void save(final S entry)
	{
		super.save(entry);
		Statement query = null;
		String sql = null;
		if (containsEntry(entry.getName()))
			sql = "UPDATE `" + tableName + "` SET " + entry.saveToMySQLDatabase(columnNames) + " WHERE " + columnNames[0] + "='" + entry.getName() + "'";
		else
			sql = "INSERT INTO `" + tableName + "` SET " + columnNames[0] + "='" + entry.getName() + "', " + entry.saveToMySQLDatabase(columnNames);
		try
		{
			query = connection.getConnection().createStatement();
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
			connection.closeConnection();
		}
	}

	@Override
	public void purgeDatabase()
	{
		Statement query = null;
		try
		{
			query = connection.getConnection().createStatement();
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
			connection.closeConnection();
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
		for (int i = 0; i < defaultColumnNames.length; i++)
			config.set(path + "MYSQL.columns." + defaultColumnNames[i], columnNames[i]);
	}
}
