package de.st_ddt.crazyonline;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.databases.ConfigurationDatabaseEntry;
import de.st_ddt.crazyutil.databases.MySQLConnection;
import de.st_ddt.crazyutil.databases.MySQLDatabaseEntry;

public class OnlinePlayerData implements ConfigurationDatabaseEntry, MySQLDatabaseEntry
{

	protected final String name;
	protected Date firstLogin;
	protected Date lastLogin;
	protected Date lastLogout;
	protected int onlineTime;
	private static final SimpleDateFormat DateFormat = CrazyOnline.DateFormat;

	public OnlinePlayerData(String name)
	{
		super();
		this.name = name.toLowerCase();
		this.firstLogin = new Date();
		this.lastLogin = new Date();
		this.lastLogout = new Date();
		this.onlineTime = 0;
	}

	public OnlinePlayerData(Player player)
	{
		this(player.getName());
	}

	// aus Config-Datenbank laden
	public OnlinePlayerData(ConfigurationSection rawData)
	{
		super();
		this.name = rawData.getString("name", rawData.getName());
		this.firstLogin = StringToDate(rawData.getString("LoginFirst"), new Date());
		this.lastLogin = StringToDate(rawData.getString("LoginLast"), new Date());
		this.lastLogout = StringToDate(rawData.getString("LogoutLast"), new Date());
		this.onlineTime = rawData.getInt("TimeTotal", 0);
	}

	// in Config-Datenbank speichern
	@Override
	public void save(ConfigurationSection config, String path)
	{
		config.set(path + "name", name);
		config.set(path + "LoginFirst", DateFormat.format(firstLogin));
		config.set(path + "LoginLast", DateFormat.format(lastLogin));
		config.set(path + "LogoutLast", DateFormat.format(lastLogout));
		config.set(path + "TimeTotal", onlineTime);
	}

	// aus MySQL-Datenbank laden
	public OnlinePlayerData(ResultSet rawData)
	{
		super();
		String name = null;
		try
		{
			name = rawData.getString("Name");
		}
		catch (Exception e)
		{
			name = "ERROR";
			e.printStackTrace();
		}
		finally
		{
			this.name = name;
		}
		try
		{
			firstLogin = StringToDate(rawData.getString("FirstLogin"), new Date());
		}
		catch (SQLException e)
		{
			firstLogin = new Date();
			e.printStackTrace();
		}
		try
		{
			lastLogin = StringToDate(rawData.getString("LastLogin"), new Date());
		}
		catch (SQLException e)
		{
			lastLogin = new Date();
			e.printStackTrace();
		}
		try
		{
			lastLogout = StringToDate(rawData.getString("LastLogout"), new Date());
		}
		catch (SQLException e)
		{
			lastLogout = new Date();
			e.printStackTrace();
		}
		try
		{
			onlineTime = rawData.getInt("OnlineTime");
		}
		catch (SQLException e)
		{
			onlineTime = 0;
			e.printStackTrace();
		}
	}

	// in MySQL-Datenbank speichern
	@Override
	public void save(MySQLConnection connection, String table)
	{
		Statement query;
		try
		{
			query = connection.getConnection().createStatement();
			query.executeUpdate("INSERT INTO " + table + " (Name,FirstLogin,LastLogin,LastLogout,OnlineTime) VALUES ('" + getName() + "','" + getFirstLoginString() + "','" + getLastLoginString() + "','" + getLastLogoutString() + "','" + getTimeTotal() + "')");
			query.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	protected Date StringToDate(String date, Date defaultDate)
	{
		if (date == null)
			return defaultDate;
		try
		{
			return DateFormat.parse(date);
		}
		catch (ParseException e)
		{
			return defaultDate;
		}
	}

	public OfflinePlayer getPlayer()
	{
		return Bukkit.getOfflinePlayer(name);
	}

	public String getName()
	{
		return name;
	}

	public Date getFirstLogin()
	{
		return firstLogin;
	}

	public String getFirstLoginString()
	{
		return DateFormat.format(firstLogin);
	}

	public Date getLastLogin()
	{
		return lastLogin;
	}

	public String getLastLoginString()
	{
		return DateFormat.format(lastLogin);
	}

	public Date getLastLogout()
	{
		return lastLogout;
	}

	public String getLastLogoutString()
	{
		return DateFormat.format(lastLogout);
	}

	public int getTimeLast()
	{
		long past;
		if (lastLogin.after(lastLogout))
			past = new Date().getTime() - lastLogin.getTime();
		else
			past = lastLogout.getTime() - lastLogin.getTime();
		return (int) past / 1000 / 60;
	}

	public int getTimeTotal()
	{
		return onlineTime;
	}

	public static SimpleDateFormat getDateFormat()
	{
		return DateFormat;
	}

	public void login()
	{
		lastLogin = new Date();
	}

	public void logout()
	{
		lastLogout = new Date();
		long past = lastLogout.getTime() - lastLogin.getTime();
		onlineTime += (int) past / 1000 / 60;
	}
}
