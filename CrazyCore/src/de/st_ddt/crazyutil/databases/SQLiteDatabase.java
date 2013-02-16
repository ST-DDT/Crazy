package de.st_ddt.crazyutil.databases;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import org.bukkit.configuration.ConfigurationSection;

public class SQLiteDatabase<S extends SQLiteDatabaseEntry> extends SQLDatabase<S>
{

	private final SQLConnection connection;
	protected final SQLConnectionPool connectionPool;

	public SQLiteDatabase(final Class<S> clazz, final SQLColumn[] columns, final String defaultPath, final String defaultTableName, final ConfigurationSection config)
	{
		super(DatabaseType.SQLITE, clazz, columns, defaultTableName, config);
		if (config == null)
		{
			this.connection = new SQLiteConnection(defaultPath);
			this.connectionPool = new SQLiteConnectionPool(connection);
		}
		else
		{
			this.connection = SQLiteConnection.getConnection(config.getConfigurationSection("connection"), defaultPath);
			this.connectionPool = new SQLiteConnectionPool(connection);
		}
	}

	public SQLiteDatabase(final Class<S> clazz, final SQLColumn[] columns, final String tableName, final String[] columnNames, final String path, final boolean cached, final boolean doNoUpdate)
	{
		super(DatabaseType.SQLITE, clazz, columns, tableName, columnNames, cached, doNoUpdate);
		this.connection = new SQLiteConnection(path);
		this.connectionPool = new SQLiteConnectionPool(connection);
	}

	@Override
	public final SQLConnection getConnection()
	{
		return connection;
	}

	@Override
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
			final ResultSet result = query.executeQuery("pragma table_info(" + tableName + ")");
			while (result.next())
				columnsNames.add(result.getString(2));
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
	public void save(final S entry)
	{
		if (entry == null)
			return;
		super.save(entry);
		final String sql;
		if (containsEntry(entry.getName()))
			sql = "UPDATE `" + tableName + "` SET " + entry.saveUpdateToSQLiteDatabase(columnNames) + " WHERE " + columnNames[0] + "='" + entry.getName() + "'";
		else
			sql = "INSERT INTO `" + tableName + "`" + entry.saveInsertToSQLiteDatabase(columnNames);
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
}
