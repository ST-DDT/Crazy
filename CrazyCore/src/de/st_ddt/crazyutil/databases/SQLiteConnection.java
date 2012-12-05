package de.st_ddt.crazyutil.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ConfigurationSaveable;

public class SQLiteConnection implements ConfigurationSaveable, MainConnection
{

	private final String path;

	public static SQLiteConnection getConnection(final ConfigurationSection config, final String defaultPath)
	{
		SQLiteConnection connection = null;
		if (config == null)
			connection = new SQLiteConnection(defaultPath);
		else
			connection = new SQLiteConnection(config.getString("path", defaultPath));
		return connection;
	}

	public SQLiteConnection(final String path)
	{
		super();
		this.path = path;
		try
		{
			Class.forName("org.sqlite.JDBC");
		}
		catch (final ClassNotFoundException e)
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "JBDC-Driver not found");
		}
	}

	@Override
	public Connection openConnection() throws SQLException
	{
		try
		{
			return DriverManager.getConnection("jdbc:sqlite:" + path);
		}
		catch (final SQLException e)
		{
			final CommandSender console = Bukkit.getConsoleSender();
			console.sendMessage(ChatColor.RED + "Connection failed");
			console.sendMessage(ChatColor.YELLOW + "Please check:");
			console.sendMessage(ChatColor.WHITE + " 1) Do you have read/write permission in that folder?");
			throw e;
		}
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "path", this.path);
	}
}
