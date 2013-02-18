package de.st_ddt.crazyonline.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyonline.CrazyOnline;
import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.data.PlayerData;
import de.st_ddt.crazyutil.ChatConverter;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.databases.ConfigurationPlayerDataDatabaseEntry;
import de.st_ddt.crazyutil.databases.FlatPlayerDataDatabaseEntry;
import de.st_ddt.crazyutil.databases.MySQLPlayerDataDatabaseEntry;
import de.st_ddt.crazyutil.databases.SQLDatabase;
import de.st_ddt.crazyutil.databases.SQLitePlayerDataDatabaseEntry;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.locales.Localized;

public class OnlinePlayerData extends PlayerData<OnlinePlayerData> implements ConfigurationPlayerDataDatabaseEntry, FlatPlayerDataDatabaseEntry, MySQLPlayerDataDatabaseEntry, SQLitePlayerDataDatabaseEntry, OnlineData
{

	private static final DecimalFormat PERCENTAGEFORMAT = new DecimalFormat("0.00");
	protected final Date firstLogin;
	protected Date lastLogin;
	protected Date lastLogout;
	protected long onlineTime;
	protected long onlineTimeMonth;
	protected long onlineTimeWeek;
	protected long onlineTimeDay;
	protected String ip;

	public OnlinePlayerData(final String name)
	{
		super(name);
		this.firstLogin = new Date();
		this.lastLogin = firstLogin;
		this.lastLogout = firstLogin;
		this.onlineTime = 0;
		this.onlineTimeMonth = 0;
		this.onlineTimeWeek = 0;
		this.onlineTimeDay = 0;
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
		this.onlineTimeMonth = rawData.getInt(columnNames[5], 0);
		this.onlineTimeWeek = rawData.getInt(columnNames[6], 0);
		this.onlineTimeDay = rawData.getInt(columnNames[7], 0);
		this.ip = rawData.getString(columnNames[8], "");
	}

	// in Config-Datenbank speichern
	@Override
	public void saveToConfigDatabase(final ConfigurationSection config, final String path, final String[] columnNames)
	{
		config.set(path + columnNames[0], name);
		config.set(path + columnNames[1], CrazyLightPluginInterface.DATETIMEFORMAT.format(firstLogin));
		config.set(path + columnNames[2], CrazyLightPluginInterface.DATETIMEFORMAT.format(lastLogin));
		config.set(path + columnNames[3], CrazyLightPluginInterface.DATETIMEFORMAT.format(lastLogout));
		config.set(path + columnNames[4], onlineTime);
		config.set(path + columnNames[5], onlineTimeMonth);
		config.set(path + columnNames[6], onlineTimeWeek);
		config.set(path + columnNames[7], onlineTimeDay);
		config.set(path + columnNames[8], ip);
	}

	// aus Flat-Datenbank laden
	public OnlinePlayerData(final String[] rawData)
	{
		super(rawData[0]);
		this.firstLogin = ObjectSaveLoadHelper.StringToDate(rawData[1], new Date());
		this.lastLogin = ObjectSaveLoadHelper.StringToDate(rawData[2], new Date());
		this.lastLogout = ObjectSaveLoadHelper.StringToDate(rawData[3], new Date());
		try
		{
			this.onlineTime = Integer.parseInt(rawData[4]);
		}
		catch (final NumberFormatException e)
		{
			this.onlineTime = 0;
		}
		// new length
		if (rawData.length == 9)
		{
			try
			{
				this.onlineTimeMonth = Integer.parseInt(rawData[5]);
			}
			catch (final NumberFormatException e)
			{
				this.onlineTimeMonth = 0;
			}
			try
			{
				this.onlineTimeWeek = Integer.parseInt(rawData[6]);
			}
			catch (final NumberFormatException e)
			{
				this.onlineTimeWeek = 0;
			}
			try
			{
				this.onlineTimeDay = Integer.parseInt(rawData[7]);
			}
			catch (final NumberFormatException e)
			{
				this.onlineTimeDay = 0;
			}
			this.ip = rawData[8];
		}
		// old length
		else if (rawData.length == 6)
			this.ip = rawData[5];
		// oldest length
		else
			this.ip = "";
	}

	// in Flat-Datenbank speichern
	@Override
	public String[] saveToFlatDatabase()
	{
		final String[] strings = new String[9];
		strings[0] = getName();
		strings[1] = getFirstLoginString();
		strings[2] = getLastLoginString();
		strings[3] = getLastLogoutString();
		strings[4] = String.valueOf(getTimeTotal());
		strings[5] = String.valueOf(onlineTimeMonth);
		strings[6] = String.valueOf(onlineTimeWeek);
		strings[7] = String.valueOf(onlineTimeDay);
		strings[8] = ip;
		return strings;
	}

	// aus MySQL-Datenbank laden
	public OnlinePlayerData(final ResultSet rawData, final String[] columnNames)
	{
		super(SQLDatabase.readName(rawData, columnNames[0]));
		String temp;
		try
		{
			temp = rawData.getString(columnNames[1]);
		}
		catch (final SQLException e)
		{
			temp = null;
			e.printStackTrace();
		}
		firstLogin = ObjectSaveLoadHelper.StringToDate(temp, new Date());
		;
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
			onlineTimeMonth = rawData.getInt(columnNames[5]);
		}
		catch (final SQLException e)
		{
			onlineTimeMonth = 0;
			e.printStackTrace();
		}
		try
		{
			onlineTimeWeek = rawData.getInt(columnNames[6]);
		}
		catch (final SQLException e)
		{
			onlineTimeWeek = 0;
			e.printStackTrace();
		}
		try
		{
			onlineTimeDay = rawData.getInt(columnNames[7]);
		}
		catch (final SQLException e)
		{
			onlineTimeDay = 0;
			e.printStackTrace();
		}
		try
		{
			ip = rawData.getString(columnNames[8]);
		}
		catch (final SQLException e)
		{
			ip = "";
			e.printStackTrace();
		}
	}

	// in MySQL-Datenbank speichern
	@Override
	public String saveToMySQLDatabase(final String[] columnNames)
	{
		return columnNames[1] + "='" + getFirstLoginString() + "', " + columnNames[2] + "='" + getLastLoginString() + "', " + columnNames[3] + "='" + getLastLogoutString() + "', " + columnNames[4] + "='" + onlineTime + "', " + columnNames[5] + "='" + onlineTimeMonth + "', " + columnNames[6] + "='" + onlineTimeWeek + "', " + columnNames[7] + "='" + onlineTimeDay + "', " + columnNames[8] + "='" + ip + "'";
	}

	// in SQLite-Datenbank speichern
	@Override
	public String saveInsertToSQLiteDatabase(final String[] columnNames)
	{
		return "(" + columnNames[0] + ", " + columnNames[1] + ", " + columnNames[2] + ", " + columnNames[3] + ", " + columnNames[4] + ", " + columnNames[5] + ", " + columnNames[6] + ", " + columnNames[7] + ", " + columnNames[8] + ") VALUES ('" + name + "','" + getFirstLoginString() + "', '" + getLastLoginString() + "', '" + getLastLogoutString() + "', '" + onlineTime + "'" + onlineTimeMonth + "'" + onlineTimeWeek + "'" + onlineTimeDay + "'" + ip + "')";
	}

	@Override
	public String saveUpdateToSQLiteDatabase(final String[] columnNames)
	{
		return columnNames[1] + "='" + getFirstLoginString() + "', " + columnNames[2] + "='" + getLastLoginString() + "', " + columnNames[3] + "='" + getLastLogoutString() + "', " + columnNames[4] + "='" + onlineTime + "', " + columnNames[5] + "='" + onlineTimeMonth + "', " + columnNames[6] + "='" + onlineTimeWeek + "', " + columnNames[7] + "='" + onlineTimeDay + "', " + columnNames[8] + "='" + ip + "'";
	}

	@Override
	public Date getFirstLogin()
	{
		return firstLogin;
	}

	@Override
	public String getFirstLoginString()
	{
		return CrazyLightPluginInterface.DATETIMEFORMAT.format(firstLogin);
	}

	@Override
	public Date getLastLogin()
	{
		return lastLogin;
	}

	@Override
	public String getLastLoginString()
	{
		return CrazyLightPluginInterface.DATETIMEFORMAT.format(lastLogin);
	}

	@Override
	public Date getLastLogout()
	{
		return lastLogout;
	}

	@Override
	public String getLastLogoutString()
	{
		return CrazyLightPluginInterface.DATETIMEFORMAT.format(lastLogout);
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

	public long getTimeMonth()
	{
		long time = onlineTimeMonth;
		if (lastLogin.after(lastLogout))
		{
			final long nowTime = new Date().getTime() / 1000 / 60;
			final long lastLoginTime = lastLogin.getTime() / 1000 / 60;
			if (nowTime / 43829 > lastLoginTime / 43829)
				time += nowTime % 43829;
			else
				time += nowTime - lastLoginTime;
		}
		return time;
	}

	public long getTimeWeek()
	{
		long time = onlineTimeWeek;
		if (lastLogin.after(lastLogout))
		{
			final long nowTime = new Date().getTime() / 1000 / 60;
			final long lastLoginTime = lastLogin.getTime() / 1000 / 60;
			if (nowTime / 10080 > lastLoginTime / 10080)
				time += nowTime % 10080;
			else
				time += nowTime - lastLoginTime;
		}
		return time;
	}

	public long getTimeDay()
	{
		long time = onlineTimeDay;
		if (lastLogin.after(lastLogout))
		{
			final long nowTime = new Date().getTime() / 1000 / 60;
			final long lastLoginTime = lastLogin.getTime() / 1000 / 60;
			if (nowTime / 1440 > lastLoginTime / 1440)
				time += nowTime % 1440;
			else
				time += nowTime - lastLoginTime;
		}
		return time;
	}

	@Override
	public void resetOnlineTime()
	{
		onlineTime = 0;
		onlineTimeMonth = 0;
		onlineTimeWeek = 0;
		onlineTimeDay = 0;
		if (!isOnline())
			if (lastLogin.after(lastLogout))
				lastLogin = lastLogout;
	}

	public void setIp(final String ip)
	{
		this.ip = ip;
	}

	@Override
	public String getLatestIP()
	{
		return ip;
	}

	public synchronized void join()
	{
		final long lastLoginTime = lastLogin.getTime() / 1000 / 60;
		lastLogin = new Date();
		final long nowTime = lastLogin.getTime() / 1000 / 60;
		if (lastLogin.after(lastLogout))
			if (nowTime / 1440 > lastLoginTime / 1440)
			{
				final CrazyOnline plugin = getPlugin();
				if (nowTime / 43829 > lastLoginTime / 43829)
				{
					plugin.statsMonth(name, lastLogout, onlineTimeMonth);
					onlineTimeMonth = 0;
				}
				if (nowTime / 10080 > lastLoginTime / 10080)
				{
					plugin.statsWeek(name, lastLogout, onlineTimeWeek);
					onlineTimeWeek = 0;
				}
				plugin.statsDay(name, lastLogout, onlineTimeDay);
				onlineTimeDay = 0;
			}
	}

	public void join(final String ip)
	{
		join();
		this.ip = ip;
	}

	public synchronized void quit()
	{
		if (lastLogin.after(lastLogout))
		{
			final long past = (new Date().getTime() - lastLogin.getTime()) / 1000 / 60;
			onlineTime += past;
			final long past2 = (new Date().getTime() - Math.max(lastLogin.getTime(), getToday().getTime())) / 1000 / 60;
			onlineTimeMonth += past2;
			onlineTimeWeek += past2;
			onlineTimeDay += past2;
		}
		lastLogout = new Date();
	}

	public void dayChange()
	{
		final Date now = new Date();
		final long nowTime = now.getTime() / 1000 / 60;
		final long lastLoginTime = lastLogin.getTime() / 1000 / 60;
		if (nowTime / 1440 > lastLoginTime / 1440)
		{
			final Date today = getToday();
			final Date yesterday = new Date(now.getTime() - 1000 * 60 * 60 * 24);
			final long pastTime = nowTime - lastLoginTime;
			final long oldTime = today.getTime() / 1000 / 60 - lastLoginTime;
			final CrazyOnline plugin = getPlugin();
			if (nowTime / 43829 - lastLoginTime / 43829 == 1)
			{
				plugin.statsMonth(name, yesterday, onlineTimeMonth + oldTime);
				onlineTimeMonth = nowTime % 43829;
			}
			else
				onlineTimeMonth += pastTime;
			if (nowTime / 10080 - lastLoginTime / 10080 == 1)
			{
				plugin.statsWeek(name, yesterday, onlineTimeWeek + oldTime);
				onlineTimeWeek = nowTime % 10080;
			}
			else
				onlineTimeWeek += pastTime;
			plugin.statsDay(name, yesterday, onlineTimeDay + oldTime);
			onlineTimeDay = nowTime % 1440;
		}
	}

	@Override
	public String getParameter(final CommandSender sender, final int index)
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
				return Long.toString(getTimeTotal());
			case 6:
				return ChatConverter.timeConverter(getTimeTotal() * 60, 2, sender, 2, false);
			case 7:
				return PERCENTAGEFORMAT.format(100D * getTimeTotal() / ((new Date().getTime() - firstLogin.getTime()) / 1000 / 60));
			case 8:
				return Long.toString(getTimeMonth());
			case 9:
				return ChatConverter.timeConverter(getTimeMonth() * 60, 2, sender, 2, false);
			case 10:
				return PERCENTAGEFORMAT.format(100D * getTimeMonth() / (new Date().getTime() / 1000 / 60 % 43829));
			case 11:
				return Long.toString(getTimeWeek());
			case 12:
				return ChatConverter.timeConverter(getTimeWeek() * 60, 2, sender, 2, false);
			case 13:
				return PERCENTAGEFORMAT.format(100D * getTimeWeek() / (new Date().getTime() / 1000 / 60 % 10080));
			case 14:
				return Long.toString(getTimeDay());
			case 15:
				return ChatConverter.timeConverter(getTimeDay() * 60, 2, sender, 2, false);
			case 16:
				return PERCENTAGEFORMAT.format(100D * getTimeDay() / (new Date().getTime() / 1000 / 60 % 1440));
			case 17:
				return ip;
		}
		return "";
	}

	@Override
	public int getParameterCount()
	{
		return 18;
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
	@Localized({ "CRAZYONLINE.PLAYERINFO.LOGINFIRST $FirstLogin$", "CRAZYONLINE.PLAYERINFO.LOGINLAST $LastLogin$", "CRAZYONLINE.PLAYERINFO.LOGOUTLAST $LastLogout$", "CRAZYONLINE.PLAYERINFO.TIMETOTAL $TimeTotalText$ $TimeTotalQuota$", "CRAZYONLINE.PLAYERINFO.TIMEMONTH $TimeMonthText$ $TimeMonthQuota$", "CRAZYONLINE.PLAYERINFO.TIMEWEEK $TimeWeekText$ $TimeWeekQuota$", "CRAZYONLINE.PLAYERINFO.TIMEDAY $TimeDayText$ $TimeDayQuota$", "CRAZYONLINE.PLAYERINFO.TIMELAST $TimeLastText$" })
	public void showDetailed(final CommandSender target, final String chatHeader)
	{
		final CrazyLocale locale = CrazyLocale.getLocaleHead().getSecureLanguageEntry("CRAZYONLINE.PLAYERINFO");
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("LOGINFIRST"), getFirstLoginString());
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("LOGINLAST"), getLastLoginString());
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("LOGOUTLAST"), getLastLogoutString());
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("TIMETOTAL"), ChatConverter.timeConverter(getTimeTotal() * 60, 2, target, 2, false), getParameter(target, 7));
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("TIMEMONTH"), ChatConverter.timeConverter(getTimeMonth() * 60, 2, target, 2, false), getParameter(target, 10));
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("TIMEWEEK"), ChatConverter.timeConverter(getTimeWeek() * 60, 2, target, 2, false), getParameter(target, 13));
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("TIMEDAY"), ChatConverter.timeConverter(getTimeDay() * 60, 2, target, 2, false), getParameter(target, 16));
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("TIMELAST"), ChatConverter.timeConverter(getTimeLast() * 60, 2, target, 2, false));
	}

	private static Date getToday()
	{
		final Calendar today = Calendar.getInstance(Locale.ENGLISH);
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.clear(Calendar.MINUTE);
		today.clear(Calendar.SECOND);
		today.clear(Calendar.MILLISECOND);
		return today.getTime();
	}
}
