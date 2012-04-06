package de.st_ddt.crazyutil.databases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQLDatabase<S extends MySQLDatabaseEntry> extends Database<S>
{

	protected final MySQLConnection connection;
	protected String table;
	protected final Column[] columns;
	protected final Column primary;

	public MySQLDatabase(Class<S> clazz, MySQLConnection connection, String table, Column[] columns, Column primary)
	{
		super(DatabaseTypes.MySQL, clazz);
		this.connection = connection;
		this.table = table;
		this.columns = columns;
		this.primary = primary;
	}

	@Override
	public void checkTable()
	{
		Statement query;
		try
		{
			query = connection.getConnection().createStatement();
			query.executeQuery("CREATE TABLE IF NOT EXIST `" + table + "` (" + Column.getFullCreateString(columns) + ")");
			query.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public S getEntry(String key)
	{
		ResultSet result = connection.getData("SELECT * FROM `" + table + "` WHERE " + primary.getName() + "='" + key + "' LIMIT=1");
		try
		{
			result.next();
			return clazz.getConstructor(ResultSet.class).newInstance(result);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<S> getEntries(String key)
	{
		List<S> list = new ArrayList<S>();
		ResultSet result = connection.getData("SELECT * FROM `" + table + "` WHERE " + primary.getName() + "='" + key + "'");
		try
		{
			while (result.next())
				list.add(clazz.getConstructor(ResultSet.class).newInstance(result));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<S> getAllEntries()
	{
		List<S> list = new ArrayList<S>();
		ResultSet result = connection.getData("SELECT * FROM `" + table + "` WHERE 1=1");
		try
		{
			while (result.next())
				list.add(clazz.getConstructor(ResultSet.class).newInstance(result));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void delete(String key)
	{
		connection.getData("DELETE FROM `" + table + "` WHERE " + primary.getName() + "='" + key + "'");
	}

	@Override
	public void save(S entry)
	{
		entry.save(connection, table);
	}
}
