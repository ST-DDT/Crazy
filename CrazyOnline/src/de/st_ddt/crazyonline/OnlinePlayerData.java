package de.st_ddt.crazyonline;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class OnlinePlayerData
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

	public OnlinePlayerData(String name, ConfigurationSection config)
	{
		super();
		this.name = name.toLowerCase();
		this.firstLogin = StringToDate(config.getString("LoginFirst"), new Date());
		this.lastLogin = StringToDate(config.getString("LoginLast"), new Date());
		this.lastLogout = StringToDate(config.getString("LogoutLast"), new Date());
		this.onlineTime = config.getInt("TimeTotal", 0);
	}

	public void save(FileConfiguration config, String path)
	{
		config.set(path + "LoginFirst", DateFormat.format(firstLogin));
		config.set(path + "LoginLast", DateFormat.format(lastLogin));
		config.set(path + "LogoutLast", DateFormat.format(lastLogout));
		config.set(path + "TimeTotal", onlineTime);
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
		long past = lastLogout.getTime() - lastLogin.getTime();
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
