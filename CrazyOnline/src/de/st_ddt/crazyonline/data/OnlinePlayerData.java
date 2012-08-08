package de.st_ddt.crazyonline.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyonline.CrazyOnline;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.data.PlayerData;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.databases.ConfigurationDatabaseEntry;
import de.st_ddt.crazyutil.databases.FlatDatabaseEntry;
import de.st_ddt.crazyutil.databases.MySQLConnection;
import de.st_ddt.crazyutil.databases.MySQLDatabase;
import de.st_ddt.crazyutil.databases.MySQLDatabaseEntry;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class OnlinePlayerData extends PlayerData<OnlinePlayerData> implements ConfigurationDatabaseEntry, MySQLDatabaseEntry, FlatDatabaseEntry, OnlineData
{

	protected Date firstLogin;
	protected Date lastLogin;
	protected Date lastLogout;
	protected long onlineTime;
	protected String ip;
	private static final SimpleDateFormat DateFormat = CrazyPlugin.DateFormat;

	public OnlinePlayerData(final String name)
	{
		super(name);
		this.firstLogin = new Date();
		this.lastLogin = new Date();
		this.lastLogout = new Date();
		this.onlineTime = 0;
		this.ip = "";
	}

	public OnlinePlayerData(final Player player)
	{
		this(player.getName());
		ip = player.getAddress().getAddress().getHostAddress();
	}

	// aus Config-Datenbank laden
	public OnlinePlayerData(final ConfigurationSection rawData, final String[] columnNames)
	{
		super(rawData.getString(columnNames[0], rawData.getName()));
		this.firstLogin = ObjectSaveLoadHelper.StringToDate(rawData.getString(columnNames[1]), new Date());
		this.lastLogin = ObjectSaveLoadHelper.StringToDate(rawData.getString(columnNames[2]), new Date());
		this.lastLogout = ObjectSaveLoadHelper.StringToDate(rawData.getString(columnNames[3]), new Date());
		this.onlineTime = rawData.getInt(columnNames[4], 0);
		this.ip = rawData.getString(columnNames[5], "");
	}

	// in Config-Datenbank speichern
	@Override
	public void saveToConfigDatabase(final ConfigurationSection config, final String path, final String[] columnNames)
	{
		config.set(path + columnNames[0], name);
		config.set(path + columnNames[1], DateFormat.format(firstLogin));
		config.set(path + columnNames[2], DateFormat.format(lastLogin));
		config.set(path + columnNames[3], DateFormat.format(lastLogout));
		config.set(path + columnNames[4], onlineTime);
		config.set(path + columnNames[5], ip);
	}

	// aus MySQL-Datenbank laden
	public OnlinePlayerData(final ResultSet rawData, final String[] columnNames)
	{
		super(MySQLDatabase.readName(rawData, columnNames[0]));
		try
		{
			firstLogin = ObjectSaveLoadHelper.StringToDate(rawData.getString(columnNames[1]), new Date());
		}
		catch (final SQLException e)
		{
			firstLogin = new Date();
			e.printStackTrace();
		}
		try
		{
			lastLogin = ObjectSaveLoadHelper.StringToDate(rawData.getString(columnNames[2]), new Date());
		}
		catch (final SQLException e)
		{
			lastLogin = new Date();
			e.printStackTrace();
		}
		try
		{
			lastLogout = ObjectSaveLoadHelper.StringToDate(rawData.getString(columnNames[3]), new Date());
		}
		catch (final SQLException e)
		{
			lastLogout = new Date();
			e.printStackTrace();
		}
		try
		{
			onlineTime = rawData.getInt(columnNames[4]);
		}
		catch (final SQLException e)
		{
			onlineTime = 0;
			e.printStackTrace();
		}
		try
		{
			ip = rawData.getString(columnNames[5]);
		}
		catch (final SQLException e)
		{
			ip = "";
			e.printStackTrace();
		}
	}

	// in MySQL-Datenbank speichern
	@Override
	public void saveToMySQLDatabase(final MySQLConnection connection, final String table, final String[] columnNames)
	{
		Statement query = null;
		try
		{
			query = connection.getConnection().createStatement();
			query.executeUpdate("INSERT INTO " + table + " (" + columnNames[0] + "," + columnNames[1] + "," + columnNames[2] + "," + columnNames[3] + "," + columnNames[4] + "," + columnNames[5] + ") VALUES ('" + getName() + "','" + getFirstLoginString() + "','" + getLastLoginString() + "','" + getLastLogoutString() + "','" + getTimeTotal() + "','" + getLatestIP() + "') " + "ON DUPLICATE KEY UPDATE " + columnNames[1] + "='" + getFirstLoginString() + "', " + columnNames[2] + "='" + getLastLoginString() + "', " + columnNames[3] + "='" + getLastLogoutString() + "', " + columnNames[4] + "='" + onlineTime + "', " + columnNames[5] + "='" + ip + "'");
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
		super(rawData[0]);
		this.firstLogin = ObjectSaveLoadHelper.StringToDate(rawData[1], new Date());
		this.lastLogin = ObjectSaveLoadHelper.StringToDate(rawData[2], new Date());
		this.lastLogout = ObjectSaveLoadHelper.StringToDate(rawData[3], new Date());
		this.onlineTime = Integer.parseInt(rawData[4]);
		if (rawData.length >= 6)
			this.ip = rawData[5];
		else
			this.ip = "";
	}

	// in Flat-Datenbank speichern
	@Override
	public String[] saveToFlatDatabase()
	{
		final String[] strings = new String[6];
		strings[0] = getName();
		strings[1] = getFirstLoginString();
		strings[2] = getLastLoginString();
		strings[3] = getLastLogoutString();
		strings[4] = String.valueOf(getTimeTotal());
		strings[5] = ip;
		return strings;
	}

	@Override
	public Date getFirstLogin()
	{
		return firstLogin;
	}

	@Override
	public String getFirstLoginString()
	{
		return DateFormat.format(firstLogin);
	}

	@Override
	public Date getLastLogin()
	{
		return lastLogin;
	}

	@Override
	public String getLastLoginString()
	{
		return DateFormat.format(lastLogin);
	}

	@Override
	public Date getLastLogout()
	{
		return lastLogout;
	}

	@Override
	public String getLastLogoutString()
	{
		return DateFormat.format(lastLogout);
	}

	@Override
	public long getTimeLast()
	{
		long past;
		if (lastLogin.after(lastLogout))
			past = new Date().getTime() - lastLogin.getTime();
		else
			past = lastLogout.getTime() - lastLogin.getTime();
		return past / 1000 / 60;
	}

	@Override
	public long getTimeTotal()
	{
		long time = onlineTime;
		if (lastLogin.after(lastLogout))
			time += Math.round((new Date().getTime() - lastLogin.getTime()) / 1000 / 60);
		return time;
	}

	@Override
	public void resetOnlineTime()
	{
		onlineTime = 0;
		if (lastLogin.after(lastLogout))
			lastLogin = new Date();
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	@Override
	public String getLatestIP()
	{
		return ip;
	}

	public static SimpleDateFormat getDateFormat()
	{
		return DateFormat;
	}

	public void join(String ip)
	{
		lastLogin = new Date();
		this.ip = ip;
	}

	public void quit()
	{
		lastLogout = new Date();
		final long past = lastLogout.getTime() - lastLogin.getTime();
		onlineTime += (int) past / 1000 / 60;
	}

	@Override
	public String getParameter(final int index)
	{
		switch (index)
		{
			case 0:
				return getName();
			case 1:
				return getFirstLoginString();
			case 2:
				return getLastLoginString();
			case 3:
				return getLastLogoutString();
			case 4:
				return String.valueOf(getTimeTotal());
			case 5:
				return ip;
		}
		return "";
	}

	@Override
	public int getParameterCount()
	{
		return 6;
	}

	public CrazyOnline getPlugin()
	{
		return CrazyOnline.getPlugin();
	}

	@Override
	protected String getChatHeader()
	{
		return getPlugin().getChatHeader();
	}

	@Override
	public void showDetailed(CommandSender target, String chatHeader)
	{
		final CrazyLocale locale = CrazyLocale.getLocaleHead().getSecureLanguageEntry("CRAZYONLINE.PLAYERINFO");
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("LOGINFIRST"), getFirstLoginString());
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("LOGINLAST"), getLastLoginString());
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("LOGOUTLAST"), getLastLogoutString());
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("TIMELAST"), ChatHelper.timeConverter(getTimeLast() * 60, 2, target, 2, false));
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("TIMETOTAL"), ChatHelper.timeConverter(getTimeTotal() * 60, 2, target, 2, false));
	}
}
