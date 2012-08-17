package de.st_ddt.crazyonline;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyonline.data.OnlineData;
import de.st_ddt.crazyonline.data.OnlineDataComparator;
import de.st_ddt.crazyonline.data.OnlineDataFirstLoginComperator;
import de.st_ddt.crazyonline.data.OnlineDataIPComparator;
import de.st_ddt.crazyonline.data.OnlineDataLastLoginComperator;
import de.st_ddt.crazyonline.data.OnlineDataLastLogoutComperator;
import de.st_ddt.crazyonline.data.OnlineDataNameComparator;
import de.st_ddt.crazyonline.data.OnlineDataOnlineComperator;
import de.st_ddt.crazyonline.data.OnlinePlayerData;
import de.st_ddt.crazyonline.data.OnlineTimeDataGetter;
import de.st_ddt.crazyonline.databases.CrazyOnlineConfigurationDatabase;
import de.st_ddt.crazyonline.databases.CrazyOnlineFlatDatabase;
import de.st_ddt.crazyonline.databases.CrazyOnlineMySQLDatabase;
import de.st_ddt.crazyonline.listener.CrazyOnlineCrazyListener;
import de.st_ddt.crazyonline.listener.CrazyOnlinePlayerListener;
import de.st_ddt.crazyonline.tasks.DropInactiveAccountsTask;
import de.st_ddt.crazyplugin.CrazyPlayerDataPlugin;
import de.st_ddt.crazyplugin.data.ParameterData;
import de.st_ddt.crazyplugin.events.CrazyPlayerRemoveEvent;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandErrorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyPipe;
import de.st_ddt.crazyutil.ToStringDataGetter;
import de.st_ddt.crazyutil.databases.DatabaseType;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class CrazyOnline extends CrazyPlayerDataPlugin<OnlineData, OnlinePlayerData> implements OnlinePlugin<OnlinePlayerData>
{

	private static CrazyOnline plugin;
	protected CrazyOnlinePlayerListener playerListener = null;
	protected CrazyOnlineCrazyListener crazyListener = null;
	protected boolean showOnlineInfo;
	protected boolean deleteShortVisitors;
	protected int autoDelete;

	public static CrazyOnline getPlugin()
	{
		return plugin;
	}

	@Override
	protected String getShortPluginName()
	{
		return "co";
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		registerHooks();
		super.onEnable();
		for (final OnlinePlayerData data : getOnlinePlayerDatas())
			data.join();
	}

	@Override
	public void onDisable()
	{
		for (final OnlinePlayerData data : getOnlinePlayerDatas())
		{
			data.quit();
			database.save(data);
		}
		super.onDisable();
	}

	@Override
	public void load()
	{
		super.load();
		final ConfigurationSection config = getConfig();
		showOnlineInfo = config.getBoolean("showOnlineInfo", true);
		deleteShortVisitors = config.getBoolean("deleteShortVisitors", deleteShortVisitors);
		autoDelete = Math.max(config.getInt("autoDelete", -1), -1);
		if (autoDelete != -1)
			getServer().getScheduler().scheduleAsyncRepeatingTask(this, new DropInactiveAccountsTask(this), 20 * 60 * 60, 20 * 60 * 60 * 6);
		// Logger
		logger.createLogChannels(config.getConfigurationSection("logs"), "Join", "Quit");
		// Database
		setupDatabase();
		dropInactiveAccounts();
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
		try
		{
			if (type == DatabaseType.CONFIG)
			{
				database = new CrazyOnlineConfigurationDatabase(tableName, config, this);
			}
			else if (type == DatabaseType.MYSQL)
			{
				database = new CrazyOnlineMySQLDatabase(tableName, config);
			}
			else if (type == DatabaseType.FLAT)
			{
				final File file = new File(getDataFolder().getPath() + "/" + tableName + ".db");
				database = new CrazyOnlineFlatDatabase(tableName, config, file);
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			database = null;
		}
		finally
		{
			if (database == null)
				broadcastLocaleMessage(true, "crazyonline.warndatabase", "DATABASE.ACCESSWARN", saveType);
			else
			{
				database.loadAllEntries();
				sendLocaleMessage("DATABASE.LOADED", Bukkit.getConsoleSender(), database.getAllEntries().size());
			}
		}
	}

	public int dropInactiveAccounts()
	{
		if (autoDelete != -1)
			return dropInactiveAccounts(autoDelete);
		return -1;
	}

	protected int dropInactiveAccounts(final long age)
	{
		final Date compare = new Date();
		compare.setTime(compare.getTime() - age * 1000 * 60 * 60 * 24);
		return dropInactiveAccounts(compare);
	}

	protected synchronized int dropInactiveAccounts(final Date limit)
	{
		final LinkedList<String> deletions = new LinkedList<String>();
		final Iterator<OnlinePlayerData> it = database.getAllEntries().iterator();
		while (it.hasNext())
		{
			final OnlinePlayerData data = it.next();
			if (data.getLastLogin().before(limit) && data.getLastLogout().before(limit))
				deletions.add(data.getName());
		}
		for (final String name : deletions)
		{
			database.deleteEntry(name);
			new CrazyPlayerRemoveEvent(this, name).callAsyncEvent();
		}
		return deletions.size();
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		config.set("showOnlineInfo", showOnlineInfo);
		config.set("deleteShortVisitors", deleteShortVisitors);
		config.set("autoDelete", autoDelete);
		logger.save(config, "logs.");
		super.saveConfiguration();
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
		if (commandLabel.equals("pinfo"))
		{
			commandPlayerInfo(sender, args);
			return true;
		}
		if (commandLabel.equals("ponlines") || commandLabel.equals("list"))
		{
			commandOnlines(sender);
			return true;
		}
		if (commandLabel.equals("psince"))
		{
			switch (args.length)
			{
				case 0:
					if (sender instanceof Player)
					{
						commandSince((Player) sender);
						return true;
					}
					throw new CrazyCommandUsageException("/psince <yyyy.MM.dd> [HH:mm:ss]");
				case 1:
					commandSince(sender, args[0] + " 00:00:00");
					return true;
				case 2:
					commandSince(sender, args[0] + " " + args[1]);
					return true;
				default:
					throw new CrazyCommandUsageException("/psince <yyyy.MM.dd> [HH:mm:ss]");
			}
		}
		if (commandLabel.equals("pbefore"))
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
		if (commandLabel.equals("ptop10"))
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
		for (final OnlinePlayerData data : database.getAllEntries())
			if (data.getLastLogin().after(date))
				list.add(data);
		Collections.sort(list, new OnlineDataLastLoginComperator());
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
		for (final OnlinePlayerData data : database.getAllEntries())
			if (data.getLastLogin().before(date))
				list.add(data);
		Collections.sort(list, new OnlineDataLastLoginComperator());
		sendLocaleMessage("MESSAGE.BEFORE.HEADER", sender, DateFormat.format(date));
		sendLocaleMessage("MESSAGE.SEPERATOR", sender);
		for (final OnlinePlayerData data : list)
			sendLocaleMessage("MESSAGE.LIST", sender, data.getName(), data.getLastLoginString());
	}

	private void commandTop10(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyonline.top10"))
			throw new CrazyCommandPermissionException();
		int page = 1;
		final int length = args.length;
		String[] pipe = null;
		for (int i = 0; i < length; i++)
		{
			final String arg = args[i];
			if (arg.equals(">"))
			{
				pipe = ChatHelper.shiftArray(args, i + 1);
				break;
			}
			else if (i != 0)
				throw new CrazyCommandUsageException("/ptop10 [page] [> Pipe]");
			else if (arg.equals("*"))
				page = Integer.MIN_VALUE;
			else
				try
				{
					page = Integer.parseInt(args[0]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(1, "Integer");
				}
		}
		final ArrayList<OnlinePlayerData> dataList = new ArrayList<OnlinePlayerData>();
		dataList.addAll(database.getAllEntries());
		Collections.sort(dataList, new OnlineDataOnlineComperator());
		if (pipe != null)
		{
			final ArrayList<ParameterData> datas = new ArrayList<ParameterData>(dataList);
			CrazyPipe.pipe(sender, datas, pipe);
			return;
		}
		sendListMessage(sender, "COMMAND.TOP10.HEADER", 0, page, dataList, new OnlineTimeDataGetter(sender));
	}

	@Override
	public boolean commandMain(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		if (commandLabel.equals("list"))
		{
			commandMainList(sender, args);
			return true;
		}
		if (commandLabel.equals("mode"))
		{
			commandMainMode(sender, args);
			return true;
		}
		return super.commandMain(sender, commandLabel, args);
	}

	private void commandMainList(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		{
			if (!sender.hasPermission("crazyonline.list"))
				throw new CrazyCommandPermissionException();
			int page = 1;
			int amount = 10;
			final int length = args.length;
			String nameFilter = null;
			String IPFilter = null;
			Boolean onlineFilter = null;
			OnlineDataComparator comparator = new OnlineDataNameComparator();
			String[] pipe = null;
			for (int i = 0; i < length; i++)
			{
				final String arg = args[i].toLowerCase();
				if (arg.startsWith("page:"))
					try
					{
						page = Integer.parseInt(arg.substring(5));
						if (page < 0)
							throw new CrazyCommandParameterException(i, "positive Integer");
					}
					catch (final NumberFormatException e)
					{
						throw new CrazyCommandParameterException(i, "page:Integer");
					}
				else if (arg.startsWith("amount:"))
				{
					if (arg.substring(7).equals("*"))
						amount = -1;
					else
						try
						{
							amount = Integer.parseInt(arg.substring(7));
						}
						catch (final NumberFormatException e)
						{
							throw new CrazyCommandParameterException(i, "amount:Integer");
						}
				}
				else if (arg.startsWith("name:"))
				{
					if (arg.substring(5).equals("*"))
						nameFilter = null;
					else
						nameFilter = arg.substring(5).toLowerCase();
				}
				else if (arg.startsWith("ip:"))
				{
					if (arg.substring(3).equals("*"))
						IPFilter = null;
					else
						IPFilter = arg.substring(3);
				}
				else if (arg.startsWith("online:"))
				{
					final String temp = arg.substring(7);
					if (temp.equals("*"))
						onlineFilter = null;
					else if (temp.equalsIgnoreCase("true") || temp.equals("1"))
						onlineFilter = true;
					else
						onlineFilter = false;
				}
				else if (arg.startsWith("sort:"))
				{
					final String temp = arg.substring(5);
					if (temp.equals("name"))
						comparator = new OnlineDataNameComparator();
					else if (temp.equals("ip"))
						comparator = new OnlineDataIPComparator();
					else if (temp.equals("firstlogin"))
						comparator = new OnlineDataFirstLoginComperator();
					else if (temp.equals("login") || temp.equals("lastlogin"))
						comparator = new OnlineDataLastLoginComperator();
					else if (temp.equals("logout") || temp.equals("lastlogout"))
						comparator = new OnlineDataLastLogoutComperator();
					else
						throw new CrazyCommandParameterException(i, "sortType", "sort:Name/IP/Date");
				}
				else if (arg.equals(">"))
				{
					pipe = ChatHelper.shiftArray(args, i + 1);
					break;
				}
				else if (arg.equals("*"))
				{
					page = Integer.MIN_VALUE;
				}
				else
					try
					{
						page = Integer.parseInt(arg);
					}
					catch (final NumberFormatException e)
					{
						throw new CrazyCommandUsageException("/crazyonline list [name:Player] [ip:IP] [online:True/False/*] [amount:Integer] [sort:Name/IP/FirstJoin/LastJoin/LastLogout/TimeTotal] [[page:]Integer] [> Pipe]");
					}
			}
			final ArrayList<OnlinePlayerData> dataList = new ArrayList<OnlinePlayerData>();
			if (IPFilter != null)
			{
				final Iterator<OnlinePlayerData> it = dataList.iterator();
				while (it.hasNext())
					if (!it.next().getLatestIP().equals(IPFilter))
						it.remove();
			}
			if (nameFilter != null)
			{
				Pattern pattern = null;
				try
				{
					pattern = Pattern.compile(nameFilter);
				}
				catch (final PatternSyntaxException e)
				{
					throw new CrazyCommandErrorException(e);
				}
				final Iterator<OnlinePlayerData> it = dataList.iterator();
				while (it.hasNext())
					if (!pattern.matcher(it.next().getName().toLowerCase()).matches())
						it.remove();
			}
			if (onlineFilter != null)
			{
				final Iterator<OnlinePlayerData> it = dataList.iterator();
				while (it.hasNext())
					if (!onlineFilter.equals(it.next().isOnline()))
						it.remove();
			}
			Collections.sort(dataList, comparator);
			if (pipe != null)
			{
				final ArrayList<ParameterData> datas = new ArrayList<ParameterData>(dataList);
				CrazyPipe.pipe(sender, datas, pipe);
				return;
			}
			sendListMessage(sender, "PLAYERDATA.LISTHEAD", amount, page, dataList, new ToStringDataGetter());
		}
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
						throw new CrazyCommandNoSuchException("saveType", saveType);
					sendLocaleMessage("MODE.CHANGE", sender, "saveType", saveType);
					if (type == database.getType())
						return;
					final Collection<OnlinePlayerData> datas = database.getAllEntries();
					getConfig().set("database.saveType", type.toString());
					setupDatabase();
					database.saveAll(datas);
					save();
					return;
				}
				else if (args[0].equalsIgnoreCase("autoDelete"))
				{
					int time = autoDelete;
					try
					{
						time = Integer.parseInt(args[1]);
					}
					catch (final NumberFormatException e)
					{
						throw new CrazyCommandParameterException(1, "Integer", "-1 = disabled", "Time in Days");
					}
					autoDelete = Math.max(time, -1);
					sendLocaleMessage("MODE.CHANGE", sender, "autoDelete", autoDelete == -1 ? "disabled" : autoDelete + " days");
					saveConfiguration();
					if (autoDelete != -1)
						getServer().getScheduler().scheduleAsyncRepeatingTask(this, new DropInactiveAccountsTask(this), 20 * 60 * 60, 20 * 60 * 60 * 6);
					return;
				}
				else if (args[0].equalsIgnoreCase("deleteShortVisitors"))
				{
					boolean newValue = false;
					if (args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("yes"))
						newValue = true;
					deleteShortVisitors = newValue;
					sendLocaleMessage("MODE.CHANGE", sender, "deleteShortVisitors", deleteShortVisitors ? "True" : "False");
					saveConfiguration();
					return;
				}
				else if (args[0].equalsIgnoreCase("saveDatabaseOnShutdown"))
				{
					boolean newValue = false;
					if (args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("yes"))
						newValue = true;
					saveDatabaseOnShutdown = newValue;
					sendLocaleMessage("MODE.CHANGE", sender, "saveDatabaseOnShutdown", saveDatabaseOnShutdown ? "True" : "False");
					saveConfiguration();
					return;
				}
				throw new CrazyCommandNoSuchException("Mode", args[0], "showOnlineInfo", "saveType", "autoDelete", "deleteShortVisitors", "saveDatabaseOnShutdown");
			case 1:
				if (args[0].equalsIgnoreCase("showOnlineInfo"))
				{
					sendLocaleMessage("MODE.CHANGE", sender, "showOnlineInfo", showOnlineInfo ? "True" : "False");
					return;
				}
				else if (args[0].equalsIgnoreCase("autoDelete"))
				{
					sendLocaleMessage("MODE.CHANGE", sender, "autoDelete", autoDelete == -1 ? "disabled" : autoDelete + " days");
					return;
				}
				else if (args[0].equalsIgnoreCase("deleteShortVisitors"))
				{
					sendLocaleMessage("MODE.CHANGE", sender, "deleteShortVisitors", deleteShortVisitors ? "True" : "False");
					return;
				}
				else if (args[0].equalsIgnoreCase("saveType"))
				{
					sendLocaleMessage("MODE.CHANGE", sender, "saveType", database.getType().toString());
					return;
				}
				else if (args[0].equalsIgnoreCase("saveDatabaseOnShutdown"))
				{
					sendLocaleMessage("MODE.CHANGE", sender, "saveDatabaseOnShutdown", saveDatabaseOnShutdown ? "True" : "False");
					return;
				}
				throw new CrazyCommandNoSuchException("Mode", args[0], "showOnlineInfo", "saveType", "autoDelete", "deleteShortVisitors", "saveDatabaseOnShutdown");
			default:
				throw new CrazyCommandUsageException("/crazyonline mode <Mode> [Value]");
		}
	}

	@Override
	public boolean commandPlayer(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		if (commandLabel.equals("reset"))
		{
			commandPlayerReset(sender, args);
			return true;
		}
		return super.commandPlayer(sender, commandLabel, args);
	}

	private void commandPlayerReset(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazyonline.admin"))
			throw new CrazyCommandPermissionException();
		if (args.length != 1)
			throw new CrazyCommandUsageException("/crazyonline player reset <Name>");
		final String name = args[0];
		final OnlinePlayerData data = getPlayerData(name);
		if (data == null)
			throw new CrazyCommandNoSuchException("Player", name);
		data.resetOnlineTime();
		sendLocaleMessage("COMMAND.RESET", sender, data.getName());
		database.save(data);
	}

	public boolean isShowOnlineInfoEnabled()
	{
		return showOnlineInfo;
	}

	@Override
	public int getAutoDelete()
	{
		return autoDelete;
	}

	public boolean isDeletingShortVisitorsEnabled()
	{
		return deleteShortVisitors;
	}

	@Override
	public Set<OnlinePlayerData> getPlayerDatasPerIP(final String IP)
	{
		final HashSet<OnlinePlayerData> res = new HashSet<OnlinePlayerData>();
		for (final OnlinePlayerData data : database.getAllEntries())
			if (data.getLatestIP().equals(IP))
				res.add(data);
		return res;
	}
}
