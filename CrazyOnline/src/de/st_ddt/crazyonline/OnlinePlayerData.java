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
import de.st_ddt.crazyutil.databases.FlatDatabaseEntry;
import de.st_ddt.crazyutil.databases.MySQLConnection;
import de.st_ddt.crazyutil.databases.MySQLDatabaseEntry;

public class OnlinePlayerData implements ConfigurationDatabaseEntry, MySQLDatabaseEntry, FlatDatabaseEntry
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
	public OnlinePlayerData(ConfigurationSection rawData, String[] columnNames)
	{
		super();
		String colName = columnNames[0];
		String colFirstLogin = columnNames[1];
		String colLastLogin = columnNames[2];
		String colLastLogout = columnNames[3];
		String colOnlineTime = columnNames[4];
		this.name = rawData.getString(colName, rawData.getName());
		this.firstLogin = StringToDate(rawData.getString(colFirstLogin), new Date());
		this.lastLogin = StringToDate(rawData.getString(colLastLogin), new Date());
		this.lastLogout = StringToDate(rawData.getString(colLastLogout), new Date());
		this.onlineTime = rawData.getInt(colOnlineTime, 0);
	}

	// in Config-Datenbank speichern
	@Override
	public void saveToConfigDatabase(ConfigurationSection config, String path, String[] columnNames)
	{
		String colName = columnNames[0];
		String colFirstLogin = columnNames[1];
		String colLastLogin = columnNames[2];
		String colLastLogout = columnNames[3];
		String colOnlineTime = columnNames[4];
		config.set(path + colName, name);
		config.set(path + colFirstLogin, DateFormat.format(firstLogin));
		config.set(path + colLastLogin, DateFormat.format(lastLogin));
		config.set(path + colLastLogout, DateFormat.format(lastLogout));
		config.set(path + colOnlineTime, onlineTime);
	}

	// aus MySQL-Datenbank laden
	public OnlinePlayerData(ResultSet rawData, String[] columnNames)
	{
		super();
		String colName = columnNames[0];
		String colFirstLogin = columnNames[1];
		String colLastLogin = columnNames[2];
		String colLastLogout = columnNames[3];
		String colOnlineTime = columnNames[4];
		String name = null;
		try
		{
			name = rawData.getString(colName);
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
			firstLogin = StringToDate(rawData.getString(colFirstLogin), new Date());
		}
		catch (SQLException e)
		{
			firstLogin = new Date();
			e.printStackTrace();
		}
		try
		{
			lastLogin = StringToDate(rawData.getString(colLastLogin), new Date());
		}
		catch (SQLException e)
		{
			lastLogin = new Date();
			e.printStackTrace();
		}
		try
		{
			lastLogout = StringToDate(rawData.getString(colLastLogout), new Date());
		}
		catch (SQLException e)
		{
			lastLogout = new Date();
			e.printStackTrace();
		}
		try
		{
			onlineTime = rawData.getInt(colOnlineTime);
		}
		catch (SQLException e)
		{
			onlineTime = 0;
			e.printStackTrace();
		}
	}

	// in MySQL-Datenbank speichern
	@Override
	public void saveToMySQLDatabase(MySQLConnection connection, String table, String[] columnNames)
	{
		String colName = columnNames[0];
		String colFirstLogin = columnNames[1];
		String colLastLogin = columnNames[2];
		String colLastLogout = columnNames[3];
		String colOnlineTime = columnNames[4];
		Statement query = null;
		try
		{
			query = connection.getConnection().createStatement();
			query.executeUpdate("INSERT INTO " + table + " (" + colName + "," + colFirstLogin + "," + colLastLogin + "," + colLastLogout + "," + colOnlineTime + ") VALUES ('" + getName() + "','" + getFirstLoginString() + "','" + getLastLoginString() + "','" + getLastLogoutString() + "','" + getTimeTotal() + "') " + "ON DUPLICATE KEY UPDATE " + colFirstLogin + "='" + getFirstLoginString() + "', " + colLastLogin + "='" + getLastLoginString() + "', " + colLastLogout + "='" + getLastLogoutString() + "', " + colOnlineTime + "='" + onlineTime + "'");
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

	// aus Flat-Datenbank laden
	public OnlinePlayerData(String[] rawData)
	{
		super();
		this.name = rawData[0];
		this.firstLogin = StringToDate(rawData[1], new Date());
		this.lastLogin = StringToDate(rawData[2], new Date());
		this.lastLogout = StringToDate(rawData[3], new Date());
		this.onlineTime = Integer.parseInt(rawData[4]);
	}

	// in Flat-Datenbank speichern
	@Override
	public String[] saveToFlatDatabase()
	{
		String[] strings = new String[5];
		strings[0] = getName();
		strings[1] = getFirstLoginString();
		strings[2] = getLastLoginString();
		strings[3] = getLastLogoutString();
		strings[4] = String.valueOf(getTimeTotal());
		return strings;
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
		int time = onlineTime;
		if (lastLogin.after(lastLogout))
			time = (int) ((new Date().getTime() - lastLogin.getTime()) / 1000 / 60);
		return time;
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
