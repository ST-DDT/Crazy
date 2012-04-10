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

	public static MySQLConnection connect(ConfigurationSection config)
	{
		if (config == null)
			return null;
		return new MySQLConnection(config.getString("host", "localhost"), config.getString("port", "3306"), config.getString("database"), config.getString("user"), config.getString("password"));
	}

	public MySQLConnection(String host, String port, String database, String user, String password)
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
		catch (ClassNotFoundException e)
		{
			// TODO Download and retry
			System.err.println("JBDC-Treiber not found");
		}
		try
		{
			connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?" + "user=" + this.user + "&" + "password=" + this.password);
		}
		catch (SQLException e)
		{
			System.err.println("Connection failed");
			e.printStackTrace();
		}
	}

	public Connection getConnection()
	{
		return connection;
	}

}
