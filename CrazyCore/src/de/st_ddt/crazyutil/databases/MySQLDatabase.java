package de.st_ddt.crazyutil.databases;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import org.bukkit.configuration.ConfigurationSection;

public class MySQLDatabase<S extends MySQLDatabaseEntry> extends SQLDatabase<S>
{

	private final SQLConnection connection;
	protected final SQLConnectionPool connectionPool;
	@Deprecated
	protected final SQLConnectionPool mysqlConnectionPool;

	public MySQLDatabase(final Class<S> clazz, final SQLColumn[] columns, final String defaultTableName, final ConfigurationSection config)
	{
		super(DatabaseType.MYSQL, clazz, columns, defaultTableName, config);
		if (config == null)
		{
			this.connection = MySQLConnection.getConnection(null);
			this.connectionPool = new MySQLConnectionPool(connection);
		}
		else
		{
			this.connection = MySQLConnection.getConnection(config.getConfigurationSection("connection"));
			this.connectionPool = new MySQLConnectionPool(connection);
		}
		mysqlConnectionPool = connectionPool;
	}

	public MySQLDatabase(final Class<S> clazz, final SQLColumn[] columns, final String tableName, final String[] columnNames, final String host, final String port, final String database, final String user, final String password, final boolean cached, final boolean doNoUpdate)
	{
		super(DatabaseType.MYSQL, clazz, columns, tableName, columnNames, cached, doNoUpdate);
		this.connection = new MySQLConnection(host, port, database, user, password);
		this.connectionPool = new MySQLConnectionPool(connection);
		mysqlConnectionPool = connectionPool;
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
			final ResultSet result = query.executeQuery("SHOW COLUMNS FROM " + tableName);
			while (result.next())
				columnsNames.add(result.getString("Field"));
			query.close();
			query = null;
			for (final SQLColumn column : columns)
			{
				// Prüfen ob Spalte vorhanden ist
				if (columnsNames.contains(column.getRealName()))
					continue;
				System.out.println("ADDED COLUMN " + column.getRealName() + " TO TABLE " + tableName);
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
}
