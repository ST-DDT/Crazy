package de.st_ddt.crazyutil.databases;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQLDatabase<S extends MySQLDatabaseEntry> extends BasicDatabase<S>
{

	protected final MySQLConnection connection;
	protected String table;
	protected final MySQLColumn[] columns;
	protected final MySQLColumn primary;

	public MySQLDatabase(final Class<S> clazz, final MySQLConnection connection, final String table, final MySQLColumn[] columns, final int primaryIndex)
	{
		super(DatabaseType.MySQL, clazz, convertColumnNames(columns), getConstructor(clazz));
		this.connection = connection;
		this.table = table;
		this.columns = columns;
		this.primary = columns[primaryIndex];
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
	public void checkTable()
	{
		Statement query = null;
		// Create Table if not exists
		try
		{
			query = connection.getConnection().createStatement();
			query.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" + MySQLColumn.getFullCreateString(columns) + ");");
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
		}
		// Create columns if not exist
		query = null;
		final ArrayList<String> columnsNames = new ArrayList<String>();
		try
		{
			query = connection.getConnection().createStatement();
			// Vorhandene Spalten abfragen
			final ResultSet result = query.executeQuery("SHOW COLUMNS FROM " + table);
			try
			{
				while (result.next())
					columnsNames.add(result.getString("Field"));
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
			if (query != null)
				try
				{
					query.close();
				}
				catch (final SQLException e)
				{}
		}
		query = null;
		for (final MySQLColumn column : columns)
		{
			// Prüfen ob Spalte vorhanden ist
			if (columnsNames.contains(column.getName()))
				continue;
			System.out.println("ADDED COLUMN " + column.getName() + " TO TABLE " + table);
			query = null;
			try
			{
				// Spalte hinzufügen
				query = connection.getConnection().createStatement();
				query.executeUpdate("ALTER TABLE " + table + " ADD " + column.getCreateString());
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
			}
		}
		connection.closeConnection();
	}

	@Override
	public String getTableName()
	{
		return table;
	}

	@Override
	public S getEntry(final String key)
	{
		S res = null;
		Statement query = null;
		ResultSet result = null;
		try
		{
			query = connection.getConnection().createStatement();
			result = query.executeQuery("SELECT * FROM `" + table + "` WHERE " + primary.getName() + "='" + key + "' LIMIT=1");
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
		return res;
	}

	@Override
	public List<S> getEntries(final String key)
	{
		final List<S> list = new ArrayList<S>();
		Statement query = null;
		ResultSet result = null;
		try
		{
			query = connection.getConnection().createStatement();
			result = query.executeQuery("SELECT * FROM `" + table + "` WHERE " + primary.getName() + "='" + key + "'");
			try
			{
				while (result.next())
					list.add(constructor.newInstance(result, columnNames));
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
		return list;
	}

	@Override
	public List<S> getAllEntries()
	{
		final List<S> list = new ArrayList<S>();
		Statement query = null;
		ResultSet result = null;
		try
		{
			query = connection.getConnection().createStatement();
			result = query.executeQuery("SELECT * FROM " + table + " WHERE 1=1");
			try
			{
				while (result.next())
					list.add(constructor.newInstance(result, columnNames));
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
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
		return list;
	}

	@Override
	public void delete(final String key)
	{
		Statement query = null;
		try
		{
			query = connection.getConnection().createStatement();
			query.executeUpdate("DELETE FROM " + table + " WHERE " + primary.getName() + "='" + key + "'");
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
	public void save(final S entry)
	{
		entry.saveToMySQLDatabase(connection, table, getColumnNames());
	}

	public final MySQLColumn[] getColumns()
	{
		return columns;
	}

	private static String[] convertColumnNames(final MySQLColumn[] columns)
	{
		final int length = columns.length;
		final String[] names = new String[length];
		for (int i = 0; i < length; i++)
			names[i] = columns[i].getName();
		return names;
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
	protected void saveDatabase()
	{
	}
}
