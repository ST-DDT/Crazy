package de.st_ddt.crazyutil.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.configuration.ConfigurationSection;

public class MySQLConnection
{

	private Connection connection = null;
	private final String host;
	private final String port;
	private final String database;
	private final String user;
	private final String password;

	public static MySQLConnection connect(final ConfigurationSection config)
	{
		return connect(config, "localhost", "3306", "database", "root", "password");
	}

	public static MySQLConnection connect(final ConfigurationSection config, final String defaultHost, final String defaultPort, final String defaultDatabase, final String defaultUser, final String defaultPassword)
	{
		final String host = config.getString("database.host", defaultHost);
		config.set("database.host", host);
		final String port = config.getString("database.port", defaultPort);
		config.set("database.port", port);
		final String database = config.getString("database.dbname", defaultDatabase);
		config.set("database.dbname", database);
		final String user = config.getString("database.user", defaultUser);
		config.set("database.user", user);
		final String password = config.getString("database.password", defaultPassword);
		config.set("database.password", password);
		return new MySQLConnection(host, port, database, user, password);
	}

	public MySQLConnection(final String host, final String port, final String database, final String user, final String password)
	{
		super();
		this.host = host;
		this.port = port;
		this.database = database;
		this.user = user;
		this.password = password;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (final ClassNotFoundException e)
		{
			// TODO Download and retry
			System.err.println("JBDC-Treiber not found");
		}
		connect();
	}

	public void connect()
	{
		try
		{
			connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?" + "user=" + this.user + "&" + "password=" + this.password);
		}
		catch (final SQLException e)
		{
			System.err.println("Connection failed");
			e.printStackTrace();
		}
	}

	public Connection getConnection()
	{
		try
		{
			if (connection.isClosed())
				connect();
		}
		catch (final NullPointerException e)
		{
			connect();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return connection;
	}

	public void closeConnection()
	{
		try
		{
			connection.close();
		}
		catch (final SQLException e)
		{}
	}
}
