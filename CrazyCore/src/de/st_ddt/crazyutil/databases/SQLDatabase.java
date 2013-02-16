package de.st_ddt.crazyutil.databases;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.configuration.ConfigurationSection;

public abstract class SQLDatabase<S extends SQLDatabaseEntry> extends BasicDatabase<S>
{

	final String tableName;
	final SQLColumn[] columns;
	final String[] columnNames;
	final boolean cached;
	final boolean doNotUpdate;

	public SQLDatabase(final DatabaseType type, final Class<S> clazz, final SQLColumn[] columns, final String defaultTableName, final ConfigurationSection config)
	{
		super(type, clazz, convertColumnNames(columns));
		if (config == null)
		{
			this.tableName = defaultTableName;
			this.columns = columns;
			this.columnNames = defaultColumnNames;
			this.cached = true;
			this.doNotUpdate = false;
		}
		else
		{
			this.tableName = config.getString("tableName", defaultTableName);
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

	public SQLDatabase(final DatabaseType type, final Class<S> clazz, final SQLColumn[] columns, final String tableName, final String[] columnNames, final boolean cached, final boolean doNoUpdate)
	{
		super(type, clazz, convertColumnNames(columns));
		this.tableName = tableName;
		this.columns = columns;
		this.columnNames = columnNames;
		for (int i = 0; i < columns.length; i++)
			columns[i].setRealName(columnNames[i]);
		this.cached = cached;
		this.doNotUpdate = doNoUpdate;
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

	@Override
	Constructor<S> getConstructor(final Class<S> clazz)
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

	public final SQLColumn[] getColumns()
	{
		return columns;
	}

	public abstract SQLConnectionPool getConnectionpool();

	public abstract SQLConnection getConnection();

	@Override
	public abstract void checkTable() throws Exception;

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
		final Connection connection = getConnectionpool().getConnection();
		if (connection == null)
			return false;
		Statement query = null;
		try
		{
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
			getConnectionpool().releaseConnection(connection);
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
		final Connection connection = getConnectionpool().getConnection();
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
			getConnectionpool().releaseConnection(connection);
		}
	}

	@Override
	public void loadAllEntries()
	{
		final Connection connection = getConnectionpool().getConnection();
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
			getConnectionpool().releaseConnection(connection);
		}
	}

	@Override
	public boolean deleteEntry(final String key)
	{
		final Connection connection = getConnectionpool().getConnection();
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
			getConnectionpool().releaseConnection(connection);
		}
		return super.deleteEntry(key);
	}

	@Override
	public void purgeDatabase()
	{
		final Connection connection = getConnectionpool().getConnection();
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
			getConnectionpool().releaseConnection(connection);
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
		final String typeName = type.name();
		getConnection().save(config, path + typeName + ".connection.");
		config.set(path + typeName + ".tableName", tableName);
		config.set(path + typeName + ".cached", cached);
		config.set(path + typeName + ".static", doNotUpdate);
		for (int i = 0; i < defaultColumnNames.length; i++)
			config.set(path + typeName + ".columns." + defaultColumnNames[i], columnNames[i]);
	}
}
