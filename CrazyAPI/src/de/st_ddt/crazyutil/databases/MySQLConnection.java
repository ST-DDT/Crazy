package de.st_ddt.crazyutil.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ConfigurationSaveable;

public class MySQLConnection implements ConfigurationSaveable
{

	private final String host;
	private final String port;
	private final String database;
	private final String user;
	private final String password;

	public static MySQLConnection getConnection(final ConfigurationSection config)
	{
		return getConnection(config, "localhost", "3306", "Crazy", "root", "password");
	}

	public static MySQLConnection getConnection(final ConfigurationSection config, final String defaultHost, final String defaultPort, final String defaultDatabase, final String defaultUser, final String defaultPassword)
	{
		MySQLConnection connection = null;
		if (config == null)
			connection = new MySQLConnection(defaultHost, defaultPort, defaultDatabase, defaultUser, defaultPassword);
		else
			connection = new MySQLConnection(config.getString("host", defaultHost), config.getString("port", defaultPort), config.getString("dbname", defaultDatabase), config.getString("user", defaultUser), config.getString("password", defaultPassword));
		return connection;
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
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "JBDC-Driver not found");
		}
	}

	public Connection openConnection() throws SQLException
	{
		try
		{
			return DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?" + "user=" + this.user + "&" + "password=" + this.password);
		}
		catch (final SQLException e)
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Connection failed");
			Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Please check:");
			Bukkit.getConsoleSender().sendMessage(ChatColor.WHITE + " 1) Is your database running/online?");
			Bukkit.getConsoleSender().sendMessage(ChatColor.WHITE + " 2) Did you made any mistakes with your server access data?");
			Bukkit.getConsoleSender().sendMessage(ChatColor.WHITE + " 3) Can you connect to your database from this server?");
			throw e;
		}
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "host", host);
		config.set(path + "port", port);
		config.set(path + "dbname", database);
		config.set(path + "user", user);
		config.set(path + "password", password);
	}
}
