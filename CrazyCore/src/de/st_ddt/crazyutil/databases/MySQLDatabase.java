package de.st_ddt.crazyutil.databases;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import org.bukkit.configuration.ConfigurationSection;

public class MySQLDatabase<S extends MySQLDatabaseEntry> extends BasicDatabase<S>
{

	protected final MySQLConnection connection;
	protected final MySQLColumn[] columns;
	protected final MySQLColumn primary;

	public MySQLDatabase(final Class<S> clazz, final String tableName, final ConfigurationSection config, final MySQLColumn[] columns, final int primaryIndex) throws Exception
	{
		super(DatabaseType.MYSQL, clazz, tableName, config, convertColumnNames(columns), getConstructor(clazz));
		this.connection = new MySQLConnection(config);
		this.columns = columns;
		this.primary = columns[primaryIndex];
		checkTable();
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
				query.executeUpdate("ALTER TABLE " + tableName + " ADD " + column.getCreateString());
				query.close();
				connection.closeConnection();
			}
		}
		catch (SQLException e)
		{
			connection.closeConnection();
			e.printStackTrace();
			throw e;
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

	public final MySQLColumn[] getColumns()
	{
		return columns;
	}

	public final MySQLColumn getPrimary()
	{
		return primary;
	}

	public final String getPrimaryName()
	{
		return primary.getName();
	}

	public final int getPrimaryIndex()
	{
		final int length = columns.length;
		for (int i = 0; i < length; i++)
			if (columns[i] == primary)
				return i;
		return -1;
	}

	@Override
	public boolean hasEntry(final String key)
	{
		if (datas.containsKey(key.toLowerCase()))
			return true;
		return loadEntry(key) != null;
	}

	@Override
	public S loadEntry(final String key)
	{
		S res = null;
		Statement query = null;
		ResultSet result = null;
		try
		{
			query = connection.getConnection().createStatement();
			result = query.executeQuery("SELECT * FROM `" + tableName + "` WHERE " + primary.getName() + "='" + key + "' LIMIT 1");
			if (result.next())
				try
				{
					res = constructor.newInstance(result, columnNames);
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
			query.close();
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
			return null;
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
		datas.put(key.toLowerCase(), res);
		return res;
	}

	@Override
	public void loadAllEntries()
	{
		Statement query = null;
		ResultSet result = null;
		try
		{
			query = connection.getConnection().createStatement();
			result = query.executeQuery("SELECT * FROM " + tableName + " WHERE 1=1");
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
			query.executeUpdate("DELETE FROM " + tableName + " WHERE " + primary.getName() + "='" + key + "'");
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
	public synchronized void save(final S entry)
	{
		super.save(entry);
		entry.saveToMySQLDatabase(connection, tableName, getColumnNames());
	}

	@Override
	public void saveDatabase()
	{
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
}
