package de.st_ddt.crazyonline;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.entity.Player;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyonline.databases.CrazyOnlineConfigurationDatabase;
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
import de.st_ddt.crazyutil.PairList;
import de.st_ddt.crazyutil.databases.Database;
import de.st_ddt.crazyutil.databases.MySQLConnection;

public class CrazyOnline extends CrazyPlugin
{

	private static CrazyOnline plugin;
	protected PairList<String, OnlinePlayerData> datas = new PairList<String, OnlinePlayerData>();
	private CrazyOnlinePlayerListener playerListener = null;
	public static final SimpleDateFormat DateFormat = CrazyCore.DateFormat;
	protected String saveType;
	protected String tableName;
	protected Database<OnlinePlayerData> database;

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
		FileConfiguration config = getConfig();
		saveType = config.getString("database.saveType", "flat").toLowerCase();
		tableName = config.getString("database.tableName", "players");
		if (saveType.equals("flat"))
		{
			database = new CrazyOnlineConfigurationDatabase(config, tableName);
		}
		else if (saveType.equals("mysql"))
		{
			String host = config.getString("database.host", "localhost");
			String port = config.getString("database.port", "3306");
			String databasename = config.getString("database.dbname", "Crazy");
			String user = config.getString("database.user", "root");
			String password = config.getString("database.password", "");
			MySQLConnection connection = new MySQLConnection(host, port, databasename, user, password);
			database = new CrazyOnlineMySQLDatabase(connection, tableName);
		}
		if (database != null)
			for (OnlinePlayerData data : database.getAllEntries())
				datas.setDataVia1(data.getName().toLowerCase(), data);
	}

	@Override
	public void save()
	{
		FileConfiguration config = getConfig();
		config.set("database.saveType", saveType);
		config.set("database.tableName", tableName);
		if (database != null)
			database.saveAll(datas.getData2List());
		super.save();
	}

	public void registerHooks()
	{
		this.playerListener = new CrazyOnlinePlayerListener(this);
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
	}

	@Override
	public boolean command(CommandSender sender, String commandLabel, String[] args) throws CrazyCommandException
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
		return false;
	}

	private void commandOnlines(CommandSender sender) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyonline.online"))
			throw new CrazyCommandPermissionException();
		sendLocaleMessage("MESSAGE.ONLINES.HEADER", sender);
		sendLocaleMessage("MESSAGE.SEPERATOR", sender);
		for (Player player : getServer().getOnlinePlayers())
			sendLocaleMessage("MESSAGE.LIST", sender, player.getName(), getPlayerData(player).getLastLoginString());
	}

	public void commandInfo(CommandSender sender, OfflinePlayer player) throws CrazyCommandException
	{
		if (sender == player)
		{
			if (!sender.hasPermission("crazyonline.info.self"))
				throw new CrazyCommandPermissionException();
		}
		else if (!sender.hasPermission("crazyonline.info.other"))
			throw new CrazyCommandPermissionException();
		OnlinePlayerData data = getPlayerData(player);
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

	public String timeOutputConverter(long time, CommandSender sender)
	{
		if (time > 2880)
		{
			long days = time / 60 / 24;
			long hours = time / 60 % 24;
			return days + " " + locale.getLocaleMessage(sender, "TIME.UNIT.DAYS") + " " + hours + " " + locale.getLocaleMessage(sender, "TIME.UNIT.HOURS");
		}
		else if (time > 120)
		{
			long hours = time / 60;
			long minutes = time % 60;
			return hours + " " + locale.getLocaleMessage(sender, "TIME.UNIT.HOURS") + " " + minutes + " " + locale.getLocaleMessage(sender, "TIME.UNIT.MINUTES");
		}
		else
			return time + " " + locale.getLocaleMessage(sender, "TIME.UNIT.MINUTES");
	}

	public void commandSince(Player player) throws CrazyCommandException
	{
		if (getPlayerData(player) == null)
			throw new CrazyCommandCircumstanceException("when joined at least for the second time!");
		commandSince(player, getPlayerData(player).getLastLogout());
	}

	public void commandSince(CommandSender sender, String date) throws CrazyCommandException
	{
		try
		{
			commandSince(sender, DateFormat.parse(date));
		}
		catch (ParseException e)
		{
			throw new CrazyCommandParameterException(1, "Date");
		}
	}

	public void commandSince(CommandSender sender, Date date) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyonline.since"))
			throw new CrazyCommandPermissionException();
		ArrayList<OnlinePlayerData> list = new ArrayList<OnlinePlayerData>();
		for (OnlinePlayerData data : datas.getData2List())
			if (data.getLastLogin().after(date))
				list.add(data);
		Collections.sort(list, new OnlinePlayerDataLoginComperator());
		sendLocaleMessage("MESSAGE.SINCE.HEADER", sender, DateFormat.format(date));
		sendLocaleMessage("MESSAGE.SEPERATOR", sender);
		for (OnlinePlayerData data : list)
			sendLocaleMessage("MESSAGE.LIST", sender, data.getName(), data.getLastLoginString());
	}

	public void commandBefore(Player player) throws CrazyCommandException
	{
		commandSince(player, getPlayerData(player).getLastLogin());
	}

	public void commandBefore(CommandSender sender, String date) throws CrazyCommandException
	{
		try
		{
			commandBefore(sender, DateFormat.parse(date));
		}
		catch (ParseException e)
		{
			throw new CrazyCommandParameterException(1, "Date");
		}
	}

	public void commandBefore(CommandSender sender, Date date) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyonline.before"))
			throw new CrazyCommandPermissionException();
		ArrayList<OnlinePlayerData> list = new ArrayList<OnlinePlayerData>();
		for (OnlinePlayerData data : datas.getData2List())
			if (data.getLastLogin().after(date))
				list.add(data);
		Collections.sort(list, new OnlinePlayerDataLoginComperator());
		sendLocaleMessage("MESSAGE.BEFORE.HEADER", sender, DateFormat.format(date));
		sendLocaleMessage("MESSAGE.SEPERATOR", sender);
		for (OnlinePlayerData data : list)
			sendLocaleMessage("MESSAGE.LIST", sender, data.getName(), data.getLastLoginString());
	}

	@Override
	public boolean commandMain(CommandSender sender, String commandLabel, String[] args) throws CrazyException
	{
		String[] newArgs = ChatHelper.shiftArray(args, 1);
		if (commandLabel.equalsIgnoreCase("mode"))
		{
			commandMainMode(sender, newArgs);
			return true;
		}
		return false;
	}

	private void commandMainMode(CommandSender sender, String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyonline.mode"))
			throw new CrazyCommandPermissionException();
		switch (args.length)
		{
			case 2:
				if (args[0].equalsIgnoreCase("saveType"))
				{
					String newValue = args[1];
					if (newValue.equalsIgnoreCase("flat"))
						saveType = newValue;
					else
						throw new CrazyCommandNoSuchException("SaveType", newValue);
					sendLocaleMessage("MODE.CHANGE", sender, "saveType", saveType);
					save();
					return;
				}
				throw new CrazyCommandNoSuchException("Mode", args[0]);
			case 1:
				if (args[0].equalsIgnoreCase("alwaysNeedPassword"))
				{
					sendLocaleMessage("MODE.CHANGE", sender, "saveType", saveType);
					return;
				}
				throw new CrazyCommandNoSuchException("Mode", args[0]);
			default:
				throw new CrazyCommandUsageException("/crazyonline mode <Mode> <Value>");
		}
	}

	public OnlinePlayerData getPlayerData(OfflinePlayer player)
	{
		return datas.findDataVia1(player.getName().toLowerCase());
	}

	public PairList<String, OnlinePlayerData> getDatas()
	{
		return datas;
	}

	public static SimpleDateFormat getDateFormat()
	{
		return DateFormat;
	}
}
