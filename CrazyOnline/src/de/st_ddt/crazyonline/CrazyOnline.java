package de.st_ddt.crazyonline;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyonline.commands.CrazyOnlineCommandBefore;
import de.st_ddt.crazyonline.commands.CrazyOnlineCommandExecutor;
import de.st_ddt.crazyonline.commands.CrazyOnlineCommandOnlines;
import de.st_ddt.crazyonline.commands.CrazyOnlineCommandPlayerReset;
import de.st_ddt.crazyonline.commands.CrazyOnlineCommandSince;
import de.st_ddt.crazyonline.commands.CrazyOnlineCommandTop10;
import de.st_ddt.crazyonline.data.OnlineData;
import de.st_ddt.crazyonline.data.OnlinePlayerData;
import de.st_ddt.crazyonline.data.comparator.OnlineDataComparator;
import de.st_ddt.crazyonline.data.comparator.OnlineDataFirstLoginComperator;
import de.st_ddt.crazyonline.data.comparator.OnlineDataIPComparator;
import de.st_ddt.crazyonline.data.comparator.OnlineDataLastLoginComperator;
import de.st_ddt.crazyonline.data.comparator.OnlineDataLastLogoutComperator;
import de.st_ddt.crazyonline.data.comparator.OnlineDataTimeCurrentOnlineComparator;
import de.st_ddt.crazyonline.data.comparator.OnlineDataTimeTotalOnlineComparator;
import de.st_ddt.crazyonline.databases.CrazyOnlineConfigurationDatabase;
import de.st_ddt.crazyonline.databases.CrazyOnlineFlatDatabase;
import de.st_ddt.crazyonline.databases.CrazyOnlineMySQLDatabase;
import de.st_ddt.crazyonline.databases.CrazyOnlineSQLiteDatabase;
import de.st_ddt.crazyonline.listener.CrazyOnlineCrazyListener;
import de.st_ddt.crazyonline.listener.CrazyOnlinePlayerListener;
import de.st_ddt.crazyonline.tasks.DropInactiveAccountsTask;
import de.st_ddt.crazyonline.tasks.StatsTask;
import de.st_ddt.crazyplugin.CrazyPlayerDataPlugin;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.data.PlayerDataFilter;
import de.st_ddt.crazyplugin.data.PlayerDataNameFilter;
import de.st_ddt.crazyplugin.events.CrazyPlayerRemoveEvent;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.databases.DatabaseType;
import de.st_ddt.crazyutil.databases.PlayerDataDatabase;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyOnline extends CrazyPlayerDataPlugin<OnlineData, OnlinePlayerData> implements OnlinePlugin<OnlinePlayerData>
{

	private static CrazyOnline plugin;
	private final CrazyPluginCommandMainMode modeCommand = new CrazyPluginCommandMainMode(this);
	private final CrazyOnlineCommandExecutor sinceCommand = new CrazyOnlineCommandSince(this);
	private final CrazyOnlineCommandExecutor onlinesCommand = new CrazyOnlineCommandOnlines(this);
	private CrazyOnlinePlayerListener playerListener = null;
	private CrazyOnlineCrazyListener crazyListener = null;
	private boolean showOnlineInfo;
	private boolean deleteShortVisitors;
	private int autoDelete;
	private boolean catchListCommand;
	private final String monthlyStatsPath = "stats/month/$0$.stats";
	private final String weeklyStatsPath = "stats/week/$0$.stats";
	private final String dailyStatsPath = "stats/daily/$0$.stats";

	public static CrazyOnline getPlugin()
	{
		return plugin;
	}

	public CrazyOnline()
	{
		super();
		registerModes();
		registerFilter();
		registerSorter();
	}

	@Localized("CRAZYONLINE.MODE.CHANGE $Name$ $Value$")
	private void registerModes()
	{
		modeCommand.addMode(modeCommand.new Mode<DatabaseType>("saveType", DatabaseType.class)
		{

			@Override
			public DatabaseType getValue()
			{
				return database.getType();
			}

			@Override
			public void setValue(final CommandSender sender, final String... args) throws CrazyException
			{
				if (args.length > 1)
					throw new CrazyCommandUsageException("[SaveType (MYSQL/FLAT/CONFIG)]");
				final String saveType = args[0];
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
					throw new CrazyCommandNoSuchException("SaveType", saveType, "MYSQL", "FLAT", "CONFIG");
				setValue(type);
				showValue(sender);
			}

			@Override
			public void setValue(final DatabaseType newValue) throws CrazyException
			{
				if (database != null)
					if (newValue == database.getType())
						return;
				final PlayerDataDatabase<OnlinePlayerData> oldDatabase = database;
				getConfig().set("database.saveType", newValue.toString());
				loadDatabase();
				if (database == null)
					database = oldDatabase;
				else if (oldDatabase != null)
					synchronized (oldDatabase.getDatabaseLock())
					{
						database.saveAll(oldDatabase.getAllEntries());
					}
				save();
			}
		});
		modeCommand.addMode(modeCommand.new BooleanFalseMode("saveDatabaseOnShutdown")
		{

			@Override
			public Boolean getValue()
			{
				return saveDatabaseOnShutdown;
			}

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				saveDatabaseOnShutdown = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new IntegerMode("autoDelete")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				sendLocaleMessage("MODE.CHANGE", sender, name, getValue() == -1 ? "disabled" : getValue() + " days");
			}

			@Override
			public Integer getValue()
			{
				return autoDelete;
			}

			@SuppressWarnings("deprecation")
			@Override
			public void setValue(final Integer newValue) throws CrazyException
			{
				autoDelete = Math.max(newValue, -1);
				saveConfiguration();
				if (autoDelete != -1)
					getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new DropInactiveAccountsTask(plugin), 20 * 60 * 60, 20 * 60 * 60 * 6);
			}
		});
		modeCommand.addMode(modeCommand.new BooleanFalseMode("showOnlineInfo")
		{

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				showOnlineInfo = newValue;
			}

			@Override
			public Boolean getValue()
			{
				return showOnlineInfo;
			}
		});
		modeCommand.addMode(modeCommand.new BooleanFalseMode("deleteShortVisitors")
		{

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				deleteShortVisitors = newValue;
			}

			@Override
			public Boolean getValue()
			{
				return deleteShortVisitors;
			}
		});
		modeCommand.addMode(modeCommand.new BooleanFalseMode("catchListCommand")
		{

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				catchListCommand = newValue;
			}

			@Override
			public Boolean getValue()
			{
				return catchListCommand;
			}
		});
	}

	private void registerFilter()
	{
		playerDataFilters.add(new PlayerDataNameFilter<OnlineData>());
		playerDataFilters.add(new PlayerDataFilter<OnlineData>("ip")
		{

			@Override
			public FilterInstance getInstance()
			{
				return new FilterInstance()
				{

					private String ip = null;

					@Override
					public void setParameter(final String parameter) throws CrazyException
					{
						ip = parameter;
					}

					@Override
					public void filter(final Collection<? extends OnlineData> datas)
					{
						if (ip != null)
							super.filter(datas);
					}

					@Override
					public boolean filter(final OnlineData data)
					{
						return data.getLatestIP().equals(ip);
					}
				};
			}
		});
		playerDataFilters.add(new PlayerDataFilter<OnlineData>("online", "on")
		{

			@Override
			public FilterInstance getInstance()
			{
				return new FilterInstance()
				{

					private Boolean online = null;

					@Override
					public void setParameter(String parameter) throws CrazyException
					{
						parameter = parameter.toLowerCase();
						if (parameter.equals("true"))
							online = true;
						else if (parameter.equals("1"))
							online = true;
						else if (parameter.equals("y"))
							online = true;
						else if (parameter.equals("yes"))
							online = true;
						else if (parameter.equals("false"))
							online = false;
						else if (parameter.equals("0"))
							online = false;
						else if (parameter.equals("n"))
							online = false;
						else if (parameter.equals("no"))
							online = false;
						else if (parameter.equals("*"))
							online = null;
						else
							throw new CrazyCommandParameterException(0, "Boolean (false/true/*)");
					}

					@Override
					public void filter(final Collection<? extends OnlineData> datas)
					{
						if (online != null)
							super.filter(datas);
					}

					@Override
					public boolean filter(final OnlineData data)
					{
						return online.equals(data.isOnline());
					}
				};
			}
		});
		playerDataFilters.add(new PlayerDataFilter<OnlineData>("since", "s")
		{

			@Override
			public FilterInstance getInstance()
			{
				return new FilterInstance()
				{

					private Date date = null;

					@Override
					public void setParameter(final String parameter) throws CrazyException
					{
						try
						{
							if (parameter.equalsIgnoreCase("TODAY"))
								date = DATEFORMAT.parse(DATEFORMAT.format(new Date()));
							else if (parameter.contains(" "))
								date = DATETIMEFORMAT.parse(parameter);
							else
								date = DATEFORMAT.parse(parameter);
						}
						catch (final ParseException e)
						{
							throw new CrazyCommandParameterException(0, "Date <JJJJ.MM.DD> [hh:mm:ss]/TODAY");
						}
					}

					@Override
					public void filter(final Collection<? extends OnlineData> datas)
					{
						if (date != null)
							super.filter(datas);
					}

					@Override
					public boolean filter(final OnlineData data)
					{
						return date.before(data.getLastLogin());
					}
				};
			}
		});
		playerDataFilters.add(new PlayerDataFilter<OnlineData>("before", "b")
		{

			@Override
			public FilterInstance getInstance()
			{
				return new FilterInstance()
				{

					private Date date = null;

					@Override
					public void setParameter(final String parameter) throws CrazyException
					{
						try
						{
							if (parameter.equalsIgnoreCase("TODAY"))
								date = DATEFORMAT.parse(DATEFORMAT.format(new Date()));
							else if (parameter.contains(" "))
								date = DATETIMEFORMAT.parse(parameter);
							else
								date = DATEFORMAT.parse(parameter);
						}
						catch (final ParseException e)
						{
							throw new CrazyCommandParameterException(0, "Date <JJJJ.MM.DD> [hh:mm:ss]/TODAY");
						}
					}

					@Override
					public void filter(final Collection<? extends OnlineData> datas)
					{
						if (date != null)
							super.filter(datas);
					}

					@Override
					public boolean filter(final OnlineData data)
					{
						return date.after(data.getLastLogin());
					}
				};
			}
		});
	}

	private void registerSorter()
	{
		playerDataSorters.put("ip", new OnlineDataIPComparator());
		final OnlineDataComparator totalTimeOnlineSorter = new OnlineDataTimeTotalOnlineComparator();
		playerDataSorters.put("time", totalTimeOnlineSorter);
		playerDataSorters.put("online", totalTimeOnlineSorter);
		playerDataSorters.put("lastonline", new OnlineDataTimeCurrentOnlineComparator());
		playerDataSorters.put("firstlogin", new OnlineDataFirstLoginComperator());
		playerDataSorters.put("lastlogin", new OnlineDataLastLoginComperator());
		playerDataSorters.put("lastlogout", new OnlineDataLastLogoutComperator());
	}

	private void registerCommands()
	{
		playerCommand.addSubCommand(new CrazyOnlineCommandPlayerReset(this), "reset");
		getCommand("psince").setExecutor(sinceCommand);
		getCommand("pbefore").setExecutor(new CrazyOnlineCommandBefore(this));
		getCommand("ptop10").setExecutor(new CrazyOnlineCommandTop10(this));
		getCommand("pinfo").setExecutor(playerCommand.getSubExecutors().get("info"));
		getCommand("ponlines").setExecutor(onlinesCommand);
		if (catchListCommand)
			getCommand("list").setExecutor(onlinesCommand);
	}

	private void registerHooks()
	{
		this.playerListener = new CrazyOnlinePlayerListener(this, sinceCommand);
		this.crazyListener = new CrazyOnlineCrazyListener(this);
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
		pm.registerEvents(crazyListener, this);
	}

	@Override
	public void onLoad()
	{
		OnlinePlugin.ONLINEPLUGINPROVIDER.setPlugin(this);
		plugin = this;
		super.onLoad();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable()
	{
		registerHooks();
		super.onEnable();
		registerCommands();
		new File(getDataFolder(), monthlyStatsPath).getAbsoluteFile().getParentFile().mkdirs();
		new File(getDataFolder(), weeklyStatsPath).getAbsoluteFile().getParentFile().mkdirs();
		new File(getDataFolder(), dailyStatsPath).getAbsoluteFile().getParentFile().mkdirs();
		// OnlinePlayer
		for (final Player player : Bukkit.getOnlinePlayers())
			playerListener.PlayerJoin(player);
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new StatsTask(this), 1728000 - new Date().getTime() / 50 % 1728000 + 5, 1728000);
	}

	@Override
	public void onDisable()
	{
		// OnlinePlayer
		for (final Player player : Bukkit.getOnlinePlayers())
			playerListener.PlayerQuit2(player);
		super.onDisable();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void loadConfiguration()
	{
		super.loadConfiguration();
		final ConfigurationSection config = getConfig();
		showOnlineInfo = config.getBoolean("showOnlineInfo", true);
		deleteShortVisitors = config.getBoolean("deleteShortVisitors", deleteShortVisitors);
		autoDelete = Math.max(config.getInt("autoDelete", -1), -1);
		if (autoDelete != -1)
			getServer().getScheduler().scheduleAsyncRepeatingTask(this, new DropInactiveAccountsTask(this), 20 * 60 * 60, 20 * 60 * 60 * 6);
		catchListCommand = config.getBoolean("catchListCommand", false);
		// Logger
		logger.createLogChannels(config.getConfigurationSection("logs"), "Join", "Quit");
	}

	@SuppressWarnings("deprecation")
	@Override
	@Localized({ "CRAZYONLINE.DATABASE.ACCESSWARN $SaveType$", "CRAZYONLINE.DATABASE.LOADED $EntryCount$" })
	public void loadDatabase()
	{
		final ConfigurationSection config = getConfig();
		final String saveType = config.getString("database.saveType", "FLAT").toUpperCase();
		try
		{
			final DatabaseType type = DatabaseType.valueOf(saveType);
			if (type == DatabaseType.CONFIG)
				database = new CrazyOnlineConfigurationDatabase(this, config.getConfigurationSection("database.CONFIG"));
			else if (type == DatabaseType.FLAT)
				database = new CrazyOnlineFlatDatabase(this, config.getConfigurationSection("database.FLAT"));
			else if (type == DatabaseType.MYSQL)
				database = new CrazyOnlineMySQLDatabase(config.getConfigurationSection("database.MYSQL"));
			else if (type == DatabaseType.SQLITE)
				database = new CrazyOnlineSQLiteDatabase(config.getConfigurationSection("database.SQLITE"));
		}
		catch (final Exception e)
		{
			consoleLog(ChatColor.RED + "NO SUCH SAVETYPE " + saveType);
		}
		if (database != null)
			try
			{
				database.save(config, "database.");
				database.initialize();
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				database = null;
			}
		if (database == null)
		{
			broadcastLocaleMessage(true, "crazyonline.warndatabase", "DATABASE.ACCESSWARN", saveType);
			Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable()
			{

				@Override
				public void run()
				{
					if (database == null)
						broadcastLocaleMessage(true, "crazyonline.warndatabase", "DATABASE.ACCESSWARN", saveType);
				}
			}, 600, 600);
		}
		else
		{
			dropInactiveAccounts();
			sendLocaleMessage("DATABASE.LOADED", Bukkit.getConsoleSender(), database.getAllEntries().size());
		}
	}

	@Override
	public void saveDatabase()
	{
		dropInactiveAccounts();
		super.saveDatabase();
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		config.set("showOnlineInfo", showOnlineInfo);
		config.set("deleteShortVisitors", deleteShortVisitors);
		config.set("autoDelete", autoDelete);
		config.set("catchListCommand", catchListCommand);
		super.saveConfiguration();
	}

	@Override
	public int dropInactiveAccounts()
	{
		if (autoDelete != -1)
			return dropInactiveAccounts(autoDelete);
		return -1;
	}

	public int dropInactiveAccounts(final long age)
	{
		final Date compare = new Date();
		compare.setTime(compare.getTime() - age * 1000 * 60 * 60 * 24);
		return dropInactiveAccounts(compare);
	}

	protected int dropInactiveAccounts(final Date limit)
	{
		if (database == null)
			return 0;
		final LinkedList<String> deletions = new LinkedList<String>();
		synchronized (database.getDatabaseLock())
		{
			for (final OnlinePlayerData data : database.getAllEntries())
				if (data.getLastLogout().before(limit) && data.getLastLogin().before(limit))
					if (!data.isOnline())
						deletions.add(data.getName());
		}
		for (final String name : deletions)
			new CrazyPlayerRemoveEvent(this, name).checkAndCallEvent();
		return deletions.size();
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
		final Set<OnlinePlayerData> res = new HashSet<OnlinePlayerData>();
		if (database == null)
			return res;
		synchronized (database.getDatabaseLock())
		{
			for (final OnlinePlayerData data : database.getAllEntries())
				if (data.getLatestIP().equals(IP))
					res.add(data);
		}
		return res;
	}

	public void dayChange()
	{
		for (final OnlinePlayerData data : getOnlinePlayerDatas())
			data.dayChange();
	}

	private static final DateFormat MONTHFORMAT = new SimpleDateFormat("yyyy-MM");
	private static final DateFormat WEEKFORMAT = new SimpleDateFormat("yyyy-ww");
	private static final DateFormat DAYFORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public void statsMonth(final String name, final Date date, final long time)
	{
		try
		{
			final FileWriter writer = new FileWriter(new File(plugin.getDataFolder(), ChatHelper.putArgs(monthlyStatsPath, name)), true);
			writer.write(MONTHFORMAT.format(date));
			writer.write(" - ");
			writer.write(Long.toString(time));
			writer.write('\n');
			writer.close();
		}
		catch (final IOException e)
		{}
	}

	public void statsWeek(final String name, final Date date, final long time)
	{
		try
		{
			final FileWriter writer = new FileWriter(new File(plugin.getDataFolder(), ChatHelper.putArgs(weeklyStatsPath, name)), true);
			writer.write(WEEKFORMAT.format(date));
			writer.write(" - ");
			writer.write(Long.toString(time));
			writer.write('\n');
			writer.close();
		}
		catch (final IOException e)
		{}
	}

	public void statsDay(final String name, final Date date, final long time)
	{
		try
		{
			final FileWriter writer = new FileWriter(new File(plugin.getDataFolder(), ChatHelper.putArgs(dailyStatsPath, name)), true);
			writer.write(DAYFORMAT.format(date));
			writer.write(" - ");
			writer.write(Long.toString(time));
			writer.write('\n');
			writer.close();
		}
		catch (final IOException e)
		{}
	}
}
