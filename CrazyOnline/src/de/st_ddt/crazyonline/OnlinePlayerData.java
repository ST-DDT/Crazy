package de.st_ddt.crazyonline;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
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
	protected long onlineTime;
	private static final SimpleDateFormat DateFormat = CrazyPlugin.DateFormat;

	public OnlinePlayerData(final String name)
	{
		super();
		this.name = name;
		this.firstLogin = new Date();
		this.lastLogin = new Date();
		this.lastLogout = new Date();
		this.onlineTime = 0;
	}

	public OnlinePlayerData(final Player player)
	{
		this(player.getName());
	}

	// aus Config-Datenbank laden
	public OnlinePlayerData(final ConfigurationSection rawData, final String[] columnNames)
	{
		super();
		final String colName = columnNames[0];
		final String colFirstLogin = columnNames[1];
		final String colLastLogin = columnNames[2];
		final String colLastLogout = columnNames[3];
		final String colOnlineTime = columnNames[4];
		this.name = rawData.getString(colName, rawData.getName());
		this.firstLogin = ObjectSaveLoadHelper.StringToDate(rawData.getString(colFirstLogin), new Date());
		this.lastLogin = ObjectSaveLoadHelper.StringToDate(rawData.getString(colLastLogin), new Date());
		this.lastLogout = ObjectSaveLoadHelper.StringToDate(rawData.getString(colLastLogout), new Date());
		this.onlineTime = rawData.getInt(colOnlineTime, 0);
	}

	// in Config-Datenbank speichern
	@Override
	public void saveToConfigDatabase(final ConfigurationSection config, final String path, final String[] columnNames)
	{
		final String colName = columnNames[0];
		final String colFirstLogin = columnNames[1];
		final String colLastLogin = columnNames[2];
		final String colLastLogout = columnNames[3];
		final String colOnlineTime = columnNames[4];
		config.set(path + colName, name);
		config.set(path + colFirstLogin, DateFormat.format(firstLogin));
		config.set(path + colLastLogin, DateFormat.format(lastLogin));
		config.set(path + colLastLogout, DateFormat.format(lastLogout));
		config.set(path + colOnlineTime, onlineTime);
	}

	// aus MySQL-Datenbank laden
	public OnlinePlayerData(final ResultSet rawData, final String[] columnNames)
	{
		super();
		final String colName = columnNames[0];
		final String colFirstLogin = columnNames[1];
		final String colLastLogin = columnNames[2];
		final String colLastLogout = columnNames[3];
		final String colOnlineTime = columnNames[4];
		String name = null;
		try
		{
			name = rawData.getString(colName);
		}
		catch (final Exception e)
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
			firstLogin = ObjectSaveLoadHelper.StringToDate(rawData.getString(colFirstLogin), new Date());
		}
		catch (final SQLException e)
		{
			firstLogin = new Date();
			e.printStackTrace();
		}
		try
		{
			lastLogin = ObjectSaveLoadHelper.StringToDate(rawData.getString(colLastLogin), new Date());
		}
		catch (final SQLException e)
		{
			lastLogin = new Date();
			e.printStackTrace();
		}
		try
		{
			lastLogout = ObjectSaveLoadHelper.StringToDate(rawData.getString(colLastLogout), new Date());
		}
		catch (final SQLException e)
		{
			lastLogout = new Date();
			e.printStackTrace();
		}
		try
		{
			onlineTime = rawData.getInt(colOnlineTime);
		}
		catch (final SQLException e)
		{
			onlineTime = 0;
			e.printStackTrace();
		}
	}

	// in MySQL-Datenbank speichern
	@Override
	public void saveToMySQLDatabase(final MySQLConnection connection, final String table, final String[] columnNames)
	{
		final String colName = columnNames[0];
		final String colFirstLogin = columnNames[1];
		final String colLastLogin = columnNames[2];
		final String colLastLogout = columnNames[3];
		final String colOnlineTime = columnNames[4];
		Statement query = null;
		try
		{
			query = connection.getConnection().createStatement();
			query.executeUpdate("INSERT INTO " + table + " (" + colName + "," + colFirstLogin + "," + colLastLogin + "," + colLastLogout + "," + colOnlineTime + ") VALUES ('" + getName() + "','" + getFirstLoginString() + "','" + getLastLoginString() + "','" + getLastLogoutString() + "','" + getTimeTotal() + "') " + "ON DUPLICATE KEY UPDATE " + colFirstLogin + "='" + getFirstLoginString() + "', " + colLastLogin + "='" + getLastLoginString() + "', " + colLastLogout + "='" + getLastLogoutString() + "', " + colOnlineTime + "='" + onlineTime + "'");
		}
		catch (final SQLException e)
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
				catch (final SQLException e)
				{}
			connection.closeConnection();
		}
	}

	// aus Flat-Datenbank laden
	public OnlinePlayerData(final String[] rawData)
	{
		super();
		this.name = rawData[0];
		this.firstLogin = ObjectSaveLoadHelper.StringToDate(rawData[1], new Date());
		this.lastLogin = ObjectSaveLoadHelper.StringToDate(rawData[2], new Date());
		this.lastLogout = ObjectSaveLoadHelper.StringToDate(rawData[3], new Date());
		this.onlineTime = Integer.parseInt(rawData[4]);
	}

	// in Flat-Datenbank speichern
	@Override
	public String[] saveToFlatDatabase()
	{
		final String[] strings = new String[5];
		strings[0] = getName();
		strings[1] = getFirstLoginString();
		strings[2] = getLastLoginString();
		strings[3] = getLastLogoutString();
		strings[4] = String.valueOf(getTimeTotal());
		return strings;
	}

	public OfflinePlayer getPlayer()
	{
		return Bukkit.getOfflinePlayer(name);
	}

	@Override
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

	public long getTimeLast()
	{
		long past;
		if (lastLogin.after(lastLogout))
			past = new Date().getTime() - lastLogin.getTime();
		else
			past = lastLogout.getTime() - lastLogin.getTime();
		return past / 1000 / 60;
	}

	public long getTimeTotal()
	{
		long time = onlineTime;
		if (lastLogin.after(lastLogout))
			time += Math.round((new Date().getTime() - lastLogin.getTime()) / 1000 / 60);
		return time;
	}

	public void resetOnlineTime()
	{
		onlineTime = 0;
		if (lastLogin.after(lastLogout))
			lastLogin = new Date();
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
		final long past = lastLogout.getTime() - lastLogin.getTime();
		onlineTime += (int) past / 1000 / 60;
	}
}
