package de.st_ddt.crazyutil.databases;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQLDatabase<S extends MySQLDatabaseEntry> extends Database<S>
{

	protected final MySQLConnection connection;
	protected String table;
	protected final MySQLColumn[] columns;
	protected final MySQLColumn primary;

	public MySQLDatabase(Class<S> clazz, MySQLConnection connection, String table, MySQLColumn[] columns, int primaryIndex)
	{
		super(DatabaseTypes.MySQL, clazz, convertColumnNames(columns), getConstructor(clazz));
		this.connection = connection;
		this.table = table;
		this.columns = columns;
		this.primary = columns[primaryIndex];
	}

	private static <S> Constructor<S> getConstructor(Class<S> clazz)
	{
		try
		{
			return clazz.getConstructor(ResultSet.class, String[].class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void checkTable()
	{
		Statement query = null;
		try
		{
			query = connection.getConnection().createStatement();
			query.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" + MySQLColumn.getFullCreateString(columns) + ");");
		}
		catch (SQLException e)
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
				catch (SQLException e)
				{}
			connection.closeConnection();
		}
	}

	@Override
	public S getEntry(String key)
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
			catch (Exception e)
			{
				e.printStackTrace();
			}
			query.close();
		}
		catch (SQLException e)
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
				catch (Exception e)
				{}
			if (query != null)
				try
				{
					query.close();
				}
				catch (Exception e)
				{}
			connection.closeConnection();
		}
		return res;
	}

	@Override
	public List<S> getEntries(String key)
	{
		List<S> list = new ArrayList<S>();
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
			catch (Exception e)
			{
				e.printStackTrace();
			}
			query.close();
		}
		catch (SQLException e)
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
				catch (Exception e)
				{}
			if (query != null)
				try
				{
					query.close();
				}
				catch (Exception e)
				{}
			connection.closeConnection();
		}
		return list;
	}

	@Override
	public List<S> getAllEntries()
	{
		List<S> list = new ArrayList<S>();
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
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		catch (SQLException e)
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
				catch (Exception e)
				{}
			if (query != null)
				try
				{
					query.close();
				}
				catch (Exception e)
				{}
			connection.closeConnection();
		}
		return list;
	}

	@Override
	public void delete(String key)
	{
		Statement query = null;
		try
		{
			query = connection.getConnection().createStatement();
			query.executeUpdate("DELETE FROM " + table + " WHERE " + primary.getName() + "='" + key + "'");
		}
		catch (SQLException e)
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
				catch (SQLException e)
				{}
			connection.closeConnection();
		}
	}

	@Override
	public void save(S entry)
	{
		entry.saveToMySQLDatabase(connection, table, getColumnNames());
	}

	public final MySQLColumn[] getColumns()
	{
		return columns;
	}

	private static String[] convertColumnNames(MySQLColumn[] columns)
	{
		int length = columns.length;
		String[] names = new String[length];
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
		int length = columns.length;
		for (int i = 0; i < length; i++)
			if (columns[i] == primary)
				return i;
		return -1;
	}
}
