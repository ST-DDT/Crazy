package de.st_ddt.crazyonline;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyonline.databases.CrazyOnlineConfigurationDatabase;
import de.st_ddt.crazyonline.databases.CrazyOnlineFlatDatabase;
import de.st_ddt.crazyonline.databases.CrazyOnlineMySQLDatabase;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.databases.Database;
import de.st_ddt.crazyutil.databases.DatabaseType;
import de.st_ddt.crazyutil.databases.MySQLConnection;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class CrazyOnline extends CrazyPlugin
{

	private static CrazyOnline plugin;
	protected HashMap<String, OnlinePlayerData> datas = new HashMap<String, OnlinePlayerData>();
	protected CrazyOnlinePlayerListener playerListener = null;
	protected CrazyOnlineCrazyListener crazyListener = null;
	protected Database<OnlinePlayerData> database;
	protected boolean showOnlineInfo;

	public static CrazyOnline getPlugin()
	{
		return plugin;
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		registerHooks();
		super.onEnable();
	}

	@Override
	public void load()
	{
		super.load();
		final ConfigurationSection config = getConfig();
		showOnlineInfo = config.getBoolean("showOnlineInfo", true);
		logger.createLogChannels(config.getConfigurationSection("logs"), "Join", "Quit");
		datas.clear();
		setupDatabase();
		if (database != null)
			for (final OnlinePlayerData data : database.getAllEntries())
				datas.put(data.getName().toLowerCase(), data);
	}

	public void setupDatabase()
	{
		final ConfigurationSection config = getConfig();
		final String saveType = config.getString("database.saveType", "FLAT").toUpperCase();
		DatabaseType type = null;
		try
		{
			type = DatabaseType.valueOf(saveType);
		}
		catch (final Exception e)
		{
			System.out.println("NO SUCH SAVETYPE " + saveType);
			type = null;
		}
		final String tableName = config.getString("database.tableName", "CrazyOnline_players");
		config.set("database.tableName", tableName);
		// Columns
		final String colName = config.getString("database.columns.name", "name");
		config.set("database.columns.name", colName);
		final String colFirstLogin = config.getString("database.columns.firstlogin", "FirstLogin");
		config.set("database.columns.firstlogin", colFirstLogin);
		final String colLastLogin = config.getString("database.columns.lastlogin", "LastLogin");
		config.set("database.columns.lastlogin", colLastLogin);
		final String colLastLogout = config.getString("database.columns.lastlogout", "LastLogout");
		config.set("database.columns.lastlogout", colLastLogout);
		final String colOnlineTime = config.getString("database.columns.onlinetime", "OnlineTime");
		config.set("database.columns.onlinetime", colOnlineTime);
		if (type == DatabaseType.CONFIG)
		{
			database = new CrazyOnlineConfigurationDatabase(config, tableName, colName, colFirstLogin, colLastLogin, colLastLogout, colOnlineTime);
		}
		else if (type == DatabaseType.MYSQL)
		{
			final MySQLConnection connection = new MySQLConnection(config, "localhost", "3306", "Crazy", "root", "");
			database = new CrazyOnlineMySQLDatabase(connection, tableName, colName, colFirstLogin, colLastLogin, colLastLogout, colOnlineTime);
		}
		else if (type == DatabaseType.FLAT)
		{
			final File file = new File(getDataFolder().getPath() + "/" + tableName + ".db");
			database = new CrazyOnlineFlatDatabase(file, colName, colFirstLogin, colLastLogin, colLastLogout, colOnlineTime);
		}
	}

	@Override
	public void save()
	{
		saveDatabase();
		saveConfiguration();
	}

	public void saveDatabase()
	{
		final ConfigurationSection config = getConfig();
		if (database != null)
			config.set("database.saveType", database.getType().toString());
		if (database != null)
			database.saveAll(datas.values());
	}

	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		config.set("showOnlineInfo", showOnlineInfo);
		logger.save(config, "logs.");
		saveConfig();
	}

	public void registerHooks()
	{
		this.playerListener = new CrazyOnlinePlayerListener(this);
		this.crazyListener = new CrazyOnlineCrazyListener(this);
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
		pm.registerEvents(crazyListener, this);
	}

	@Override
	public boolean command(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyCommandException
	{
		if (commandLabel.equalsIgnoreCase("pinfo"))
		{
			switch (args.length)
			{
				case 0:
					if (!(sender instanceof Player))
						throw new CrazyCommandUsageException("/pinfo <Player>");
					commandInfo(sender, (Player) sender);
					return true;
				case 1:
					OfflinePlayer info = getServer().getPlayer(args[0]);
					if (info == null)
						info = getServer().getOfflinePlayer(args[0]);
					if (info == null)
						throw new CrazyCommandNoSuchException("Player", args[0]);
					commandInfo(sender, info);
					return true;
				default:
					throw new CrazyCommandUsageException("/pinfo <Player>");
			}
		}
		if (commandLabel.equalsIgnoreCase("ponlines"))
		{
			commandOnlines(sender);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("psince"))
		{
			switch (args.length)
			{
				case 0:
					if (sender instanceof Player)
					{
						commandSince((Player) sender);
						return true;
					}
					throw new CrazyCommandUsageException("/psince <yyyy.MM.dd>", "/psince <yyyy.MM.dd HH:mm:ss>");
				case 1:
					commandSince(sender, args[0] + " 00:00:00");
					return true;
				case 2:
					commandSince(sender, args[0] + " " + args[1]);
					return true;
				default:
					throw new CrazyCommandUsageException("/psince <yyyy.MM.dd>", "/psince <yyyy.MM.dd HH:mm:ss>");
			}
		}
		if (commandLabel.equalsIgnoreCase("pbefore"))
		{
			switch (args.length)
			{
				case 0:
					if (sender instanceof Player)
					{
						commandBefore((Player) sender);
						return true;
					}
					throw new CrazyCommandUsageException("/pbefore <yyyy.MM.dd>", "/pbefore <yyyy.MM.dd HH:mm:ss>");
				case 1:
					commandBefore(sender, args[0] + " 00:00:00");
					return true;
				case 2:
					commandBefore(sender, args[0] + " " + args[1]);
					return true;
				default:
					throw new CrazyCommandUsageException("/pbefore <yyyy.MM.dd>", "/pbefore <yyyy.MM.dd HH:mm:ss>");
			}
		}
		if (commandLabel.equalsIgnoreCase("ptop10"))
		{
			commandTop10(sender, args);
			return true;
		}
		return false;
	}

	private void commandOnlines(final CommandSender sender) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyonline.online"))
			throw new CrazyCommandPermissionException();
		sendLocaleMessage("MESSAGE.ONLINES.HEADER", sender);
		sendLocaleMessage("MESSAGE.SEPERATOR", sender);
		for (final Player player : getServer().getOnlinePlayers())
			sendLocaleMessage("MESSAGE.LIST", sender, player.getName(), getPlayerData(player).getLastLoginString());
	}

	public void commandInfo(final CommandSender sender, final OfflinePlayer player) throws CrazyCommandException
	{
		if (sender == player)
		{
			if (!sender.hasPermission("crazyonline.info.self"))
				throw new CrazyCommandPermissionException();
		}
		else if (!sender.hasPermission("crazyonline.info.other"))
			throw new CrazyCommandPermissionException();
		final OnlinePlayerData data = getPlayerData(player);
		if (data == null)
			throw new CrazyCommandNoSuchException("PlayerData", player.getName());
		sendLocaleMessage("MESSAGE.INFO.HEADER", sender, data.getName());
		sendLocaleMessage("MESSAGE.SEPERATOR", sender);
		sendLocaleMessage("MESSAGE.INFO.LOGIN.FIRST", sender, data.getFirstLoginString());
		sendLocaleMessage("MESSAGE.INFO.LOGIN.LAST", sender, data.getLastLoginString());
		sendLocaleMessage("MESSAGE.INFO.LOGOUT.LAST", sender, data.getLastLogoutString());
		sendLocaleMessage("MESSAGE.INFO.TIME.LAST", sender, timeOutputConverter(data.getTimeLast(), sender));
		sendLocaleMessage("MESSAGE.INFO.TIME.TOTAL", sender, timeOutputConverter(data.getTimeTotal(), sender));
	}

	public String timeOutputConverter(final long time, final CommandSender sender)
	{
		if (time > 2880)
		{
			final long days = time / 60 / 24;
			final long hours = time / 60 % 24;
			return days + " " + CrazyLocale.getUnitText("TIME.DAYS", sender) + " " + hours + " " + CrazyLocale.getUnitText("TIME.HOURS", sender);
		}
		else if (time > 120)
		{
			final long hours = time / 60;
			final long minutes = time % 60;
			return hours + " " + CrazyLocale.getUnitText("TIME.HOURS", sender) + " " + minutes + " " + CrazyLocale.getUnitText("TIME.MINUTES", sender);
		}
		else
			return time + " " + CrazyLocale.getUnitText("TIME.MINUTES", sender);
	}

	public void commandSince(final Player player) throws CrazyCommandException
	{
		if (getPlayerData(player) == null)
			throw new CrazyCommandCircumstanceException("when joined at least for the second time!");
		commandSince(player, getPlayerData(player).getLastLogout());
	}

	public void commandSince(final CommandSender sender, final String date) throws CrazyCommandException
	{
		try
		{
			commandSince(sender, DateFormat.parse(date));
		}
		catch (final ParseException e)
		{
			throw new CrazyCommandParameterException(1, "Date");
		}
	}

	public void commandSince(final CommandSender sender, final Date date) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyonline.since"))
			throw new CrazyCommandPermissionException();
		final ArrayList<OnlinePlayerData> list = new ArrayList<OnlinePlayerData>();
		for (final OnlinePlayerData data : datas.values())
			if (data.getLastLogin().after(date))
				list.add(data);
		Collections.sort(list, new OnlinePlayerDataLoginComperator());
		sendLocaleMessage("MESSAGE.SINCE.HEADER", sender, DateFormat.format(date));
		sendLocaleMessage("MESSAGE.SEPERATOR", sender);
		for (final OnlinePlayerData data : list)
			sendLocaleMessage("MESSAGE.LIST", sender, data.getName(), data.getLastLoginString());
	}

	public void commandBefore(final Player player) throws CrazyCommandException
	{
		commandSince(player, getPlayerData(player).getLastLogin());
	}

	public void commandBefore(final CommandSender sender, final String date) throws CrazyCommandException
	{
		try
		{
			commandBefore(sender, DateFormat.parse(date));
		}
		catch (final ParseException e)
		{
			throw new CrazyCommandParameterException(1, "Date");
		}
	}

	public void commandBefore(final CommandSender sender, final Date date) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyonline.before"))
			throw new CrazyCommandPermissionException();
		final ArrayList<OnlinePlayerData> list = new ArrayList<OnlinePlayerData>();
		for (final OnlinePlayerData data : datas.values())
			if (data.getLastLogin().after(date))
				list.add(data);
		Collections.sort(list, new OnlinePlayerDataLoginComperator());
		sendLocaleMessage("MESSAGE.BEFORE.HEADER", sender, DateFormat.format(date));
		sendLocaleMessage("MESSAGE.SEPERATOR", sender);
		for (final OnlinePlayerData data : list)
			sendLocaleMessage("MESSAGE.LIST", sender, data.getName(), data.getLastLoginString());
	}

	private void commandTop10(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyonline.top10"))
			throw new CrazyCommandPermissionException();
		int page;
		switch (args.length)
		{
			case 0:
				page = 1;
				break;
			case 1:
				try
				{
					page = Integer.parseInt(args[0]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(1, "Integer");
				}
				break;
			default:
				throw new CrazyCommandUsageException("/crazylist [Page]");
		}
		final ArrayList<OnlinePlayerData> list = new ArrayList<OnlinePlayerData>();
		list.addAll(datas.values());
		Collections.sort(list, new OnlinePlayerDataOnlineComperator());
		sendListMessage(sender, "COMMAND.TOP10.HEADER", 0, page, list, new OnlineTimeDataGetter(sender));
	}

	@Override
	public boolean commandMain(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		final String[] newArgs = ChatHelper.shiftArray(args, 1);
		if (commandLabel.equalsIgnoreCase("mode"))
		{
			commandMainMode(sender, newArgs);
			return true;
		}
		return false;
	}

	private void commandMainMode(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyonline.mode"))
			throw new CrazyCommandPermissionException();
		switch (args.length)
		{
			case 2:
				if (args[0].equalsIgnoreCase("showOnlineInfo"))
				{
					boolean newValue = false;
					if (args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("yes"))
						newValue = true;
					showOnlineInfo = newValue;
					sendLocaleMessage("MODE.CHANGE", sender, "showOnlineInfo", showOnlineInfo ? "True" : "False");
					saveConfiguration();
					return;
				}
				else if (args[0].equalsIgnoreCase("saveType"))
				{
					final String saveType = args[1];
					DatabaseType type = null;
					try
					{
						type = DatabaseType.valueOf(saveType.toUpperCase());
					}
					catch (final Exception e)
					{
						type = null;
					}
					if (type == null)
						throw new CrazyCommandNoSuchException("SaveType", saveType);
					sendLocaleMessage("MODE.CHANGE", sender, "saveType", saveType);
					if (type == database.getType())
						return;
					getConfig().set("database.saveType", type.toString());
					setupDatabase();
					save();
					return;
				}
				throw new CrazyCommandNoSuchException("Mode", args[0]);
			case 1:
				if (args[0].equalsIgnoreCase("showOnlineInfo"))
				{
					sendLocaleMessage("MODE.CHANGE", sender, "showOnlineInfo", showOnlineInfo ? "True" : "False");
					return;
				}
				else if (args[0].equalsIgnoreCase("saveType"))
				{
					sendLocaleMessage("MODE.CHANGE", sender, "saveType", database.getType().toString());
					return;
				}
				throw new CrazyCommandNoSuchException("Mode", args[0]);
			default:
				throw new CrazyCommandUsageException("/crazyonline mode <Mode> [Value]");
		}
	}

	public OnlinePlayerData getPlayerData(final OfflinePlayer player)
	{
		return datas.get(player.getName().toLowerCase());
	}

	public HashMap<String, OnlinePlayerData> getDatas()
	{
		return datas;
	}

	public boolean isShowOnlineInfoEnabled()
	{
		return showOnlineInfo;
	}

	public boolean deletePlayerData(String player)
	{
		return datas.remove(player.toLowerCase()) != null;
	}
}
