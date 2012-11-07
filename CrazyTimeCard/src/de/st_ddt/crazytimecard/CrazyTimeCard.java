package de.st_ddt.crazytimecard;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyplugin.CrazyPlayerDataPlugin;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazytimecard.commands.CrazyTimeCardCommandActivate;
import de.st_ddt.crazytimecard.commands.CrazyTimeCardCommandMainCommands;
import de.st_ddt.crazytimecard.commands.CrazyTimeCardCommandRegister;
import de.st_ddt.crazytimecard.data.CardData;
import de.st_ddt.crazytimecard.data.PlayerTimeData;
import de.st_ddt.crazytimecard.databases.CrazyTimeCardCardConfigurationDatabase;
import de.st_ddt.crazytimecard.databases.CrazyTimeCardCardFlatDatabase;
import de.st_ddt.crazytimecard.databases.CrazyTimeCardCardMySQLDatabase;
import de.st_ddt.crazytimecard.databases.CrazyTimeCardConfigurationDatabase;
import de.st_ddt.crazytimecard.databases.CrazyTimeCardFlatDatabase;
import de.st_ddt.crazytimecard.databases.CrazyTimeCardMySQLDatabase;
import de.st_ddt.crazytimecard.listener.CrazyTimeCardCrazyListener;
import de.st_ddt.crazytimecard.listener.CrazyTimeCardPlayerListener;
import de.st_ddt.crazytimecard.listener.CrazyTimeCardPlayerListener_132;
import de.st_ddt.crazytimecard.listener.CrazyTimeCardPlayerListener_142;
import de.st_ddt.crazytimecard.listener.CrazyTimeCardVehicleListener;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.VersionComparator;
import de.st_ddt.crazyutil.databases.Database;
import de.st_ddt.crazyutil.databases.DatabaseType;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyTimeCard extends CrazyPlayerDataPlugin<PlayerTimeData, PlayerTimeData>
{

	private static CrazyTimeCard plugin;
	private final CrazyPluginCommandMainMode modeCommand = new CrazyPluginCommandMainMode(this);
	private Database<CardData> cardDatabase;
	private long startDuration;
	private long defaultDuration;
	private int defaultKeyLength;
	private int autoKick;
	private String filterNames;
	private boolean blockDifferentNameCases;
	private int minNameLength;
	private int maxNameLength;
	private List<String> commandWhiteList;
	private CrazyTimeCardPlayerListener playerListener;

	public static CrazyTimeCard getPlugin()
	{
		return plugin;
	}

	@Localized("CRAZYTIMECARD.MODE.CHANGE $Name$ $Value$")
	private void registerModes()
	{
		modeCommand.addMode(modeCommand.new DurationMode("startDuration")
		{

			@Override
			public Long getValue()
			{
				return startDuration;
			}

			@Override
			public void setValue(final Long newValue) throws CrazyException
			{
				startDuration = Math.max(0, newValue);
				saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new DurationMode("defaultDuration")
		{

			@Override
			public Long getValue()
			{
				return defaultDuration;
			}

			@Override
			public void setValue(final Long newValue) throws CrazyException
			{
				defaultDuration = Math.max(0, newValue);
				saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new IntegerMode("defaultKeyLength")
		{

			@Override
			public Integer getValue()
			{
				return defaultKeyLength;
			}

			@Override
			public void setValue(final Integer newValue) throws CrazyException
			{
				defaultKeyLength = Math.max(newValue, 1);
				saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new IntegerMode("autoKick")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				sendLocaleMessage("MODE.CHANGE", sender, name, getValue() == -1 ? "disabled" : getValue() + " seconds");
			}

			@Override
			public Integer getValue()
			{
				return autoKick;
			}

			@Override
			public void setValue(final Integer newValue) throws CrazyException
			{
				autoKick = Math.max(newValue, -1);
				saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new Mode<String>("filterNames", String.class)
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				sendLocaleMessage("MODE.CHANGE", sender, "filterNames", getValue().equals(".") ? "disabled" : getValue());
			}

			@Override
			public String getValue()
			{
				return filterNames;
			}

			@Override
			public void setValue(final CommandSender sender, final String... args) throws CrazyException
			{
				String newFilter = ChatHelper.listingString(" ", args);
				if (newFilter.equalsIgnoreCase("false") || newFilter.equalsIgnoreCase("0") || newFilter.equalsIgnoreCase("off"))
					newFilter = ".";
				else if (newFilter.equalsIgnoreCase("true") || newFilter.equalsIgnoreCase("1") || newFilter.equalsIgnoreCase("on"))
					newFilter = "[a-zA-Z0-9_]";
				setValue(newFilter);
			}

			@Override
			public void setValue(final String newValue) throws CrazyException
			{
				filterNames = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new BooleanFalseMode("blockDifferentNameCases")
		{

			@Override
			public Boolean getValue()
			{
				return blockDifferentNameCases;
			}

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				blockDifferentNameCases = newValue;
				saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new IntegerMode("minNameLength")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				sendLocaleMessage("MODE.CHANGE", sender, name, getValue() + " characters");
			}

			@Override
			public Integer getValue()
			{
				return minNameLength;
			}

			@Override
			public void setValue(final Integer newValue) throws CrazyException
			{
				minNameLength = Math.min(Math.max(newValue, 1), 16);
				saveConfiguration();
			}
		});
		modeCommand.addMode(modeCommand.new IntegerMode("maxNameLength")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				sendLocaleMessage("MODE.CHANGE", sender, name, getValue() + " characters");
			}

			@Override
			public Integer getValue()
			{
				return maxNameLength;
			}

			@Override
			public void setValue(final Integer newValue) throws CrazyException
			{
				maxNameLength = Math.min(Math.max(newValue, 1), 255);
				saveConfiguration();
			}
		});
	}

	private void registerCommands()
	{
		getCommand("registercard").setExecutor(new CrazyTimeCardCommandRegister(this));
		getCommand("activatecard").setExecutor(new CrazyTimeCardCommandActivate(this));
		mainCommand.addSubCommand(modeCommand, "mode");
		mainCommand.addSubCommand(new CrazyTimeCardCommandMainCommands(this), "commands");
	}

	private void registerHooks()
	{
		final String mcVersion = ChatHelper.getMinecraftVersion();
		if (VersionComparator.compareVersions(mcVersion, "1.3.2") == 1)
			this.playerListener = new CrazyTimeCardPlayerListener_142(this);
		else
			this.playerListener = new CrazyTimeCardPlayerListener_132(this);
		playerListener = new CrazyTimeCardPlayerListener(this);
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new CrazyTimeCardCrazyListener(this), this);
		pm.registerEvents(playerListener, this);
		pm.registerEvents(new CrazyTimeCardVehicleListener(this), this);
	}

	public Database<CardData> getCardDatabase()
	{
		return cardDatabase;
	}

	@Override
	public void onLoad()
	{
		plugin = this;
		super.onLoad();
	}

	@Override
	public void onEnable()
	{
		registerHooks();
		super.onEnable();
		registerCommands();
		// OnlinePlayer
		for (final Player player : Bukkit.getOnlinePlayers())
			playerListener.PlayerJoin(player);
	}

	@Override
	public void onDisable()
	{
		// OnlinePlayer
		for (final Player player : Bukkit.getOnlinePlayers())
			playerListener.PlayerQuit(player);
		super.onDisable();
	}

	@Override
	@Localized({ "CRAZYTIMECARD.DATABASE.ACCESSWARN $SaveType$", "CRAZYTIMECARD.DATABASE.LOADED $EntryCount$" })
	public void loadDatabase()
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
			consoleLog(ChatColor.RED + "NO SUCH SAVETYPE " + saveType);
		}
		if (type == DatabaseType.CONFIG)
			database = new CrazyTimeCardConfigurationDatabase(this, config.getConfigurationSection("database.CONFIG"));
		else if (type == DatabaseType.MYSQL)
			database = new CrazyTimeCardMySQLDatabase(config.getConfigurationSection("database.MYSQL"));
		else if (type == DatabaseType.FLAT)
			database = new CrazyTimeCardFlatDatabase(this, config.getConfigurationSection("database.FLAT"));
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
			broadcastLocaleMessage(true, "crazylogin.warndatabase", "DATABASE.ACCESSWARN", saveType);
			Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable()
			{

				@Override
				public void run()
				{
					if (database == null)
						broadcastLocaleMessage(true, "crazylogin.warndatabase", "DATABASE.ACCESSWARN", saveType);
				}
			}, 600, 600);
		}
		else
		{
			sendLocaleMessage("DATABASE.LOADED", Bukkit.getConsoleSender(), database.getAllEntries().size());
		}
		// Database Cards
		final String cardSaveType = config.getString("cardDatabase.saveType", "FLAT").toUpperCase();
		type = null;
		try
		{
			type = DatabaseType.valueOf(saveType);
		}
		catch (final Exception e)
		{
			consoleLog(ChatColor.RED + "NO SUCH SAVETYPE " + saveType);
		}
		if (type == DatabaseType.CONFIG)
			cardDatabase = new CrazyTimeCardCardConfigurationDatabase(this, config.getConfigurationSection("cardDatabase.CONFIG"));
		else if (type == DatabaseType.FLAT)
			cardDatabase = new CrazyTimeCardCardFlatDatabase(this, config.getConfigurationSection("cardDatabase.FLAT"));
		else if (type == DatabaseType.MYSQL)
			cardDatabase = new CrazyTimeCardCardMySQLDatabase(config.getConfigurationSection("cardDatabase.MYSQL"));
		if (cardDatabase != null)
			try
			{
				cardDatabase.save(config, "cardDatabase.");
				cardDatabase.initialize();
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				cardDatabase = null;
			}
		if (cardDatabase == null)
		{
			broadcastLocaleMessage(true, "crazytimecard.warndatabase", "DATABASE.ACCESSWARN", cardSaveType);
			Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable()
			{

				@Override
				public void run()
				{
					if (cardDatabase == null)
						broadcastLocaleMessage(true, "crazytimecard.warndatabase", "DATABASE.ACCESSWARN", cardSaveType);
				}
			}, 600, 600);
		}
		else
		{
			sendLocaleMessage("DATABASE.LOADED", Bukkit.getConsoleSender(), cardDatabase.getAllEntries().size());
		}
	}

	@Override
	public void loadConfiguration()
	{
		super.loadConfiguration();
		final ConfigurationSection config = getConfig();
		// 86400000 ms = 1 d
		startDuration = config.getLong("startDuration", 86400000L * 5);
		defaultDuration = config.getLong("defaultDuration", 86400000L * 30);
		defaultKeyLength = config.getInt("defaultKeyLength", 20);
		autoKick = Math.max(config.getInt("autoKick", -1), -1);
		commandWhiteList = config.getStringList("commandWhitelist");
		if (isInstalled)
			if (commandWhiteList.size() == 0)
			{
				commandWhiteList.add("/activatecard .*");
				commandWhiteList.add("/activate .*");
				if (Bukkit.getPluginManager().getPlugin("CrazyLogin") != null)
				{
					commandWhiteList.add("/login .*");
					commandWhiteList.add("/register .*");
				}
			}
		filterNames = config.getString("filterNames", "false");
		if (filterNames.equals("false"))
			filterNames = ".";
		else if (filterNames.equals("true"))
			filterNames = "[a-zA-Z0-9_]";
		blockDifferentNameCases = config.getBoolean("blockDifferentNameCases", false);
		minNameLength = Math.min(Math.max(config.getInt("minNameLength", 3), 1), 16);
		maxNameLength = Math.min(Math.max(config.getInt("maxNameLength", 16), minNameLength), 255);
	}

	@Override
	public void saveDatabase()
	{
		final ConfigurationSection config = getConfig();
		if (cardDatabase != null)
		{
			cardDatabase.save(config, "cardDatabase.");
			cardDatabase.saveDatabase();
		}
		super.saveDatabase();
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		if (cardDatabase != null)
			cardDatabase.save(config, "cardDatabase.");
		config.set("startDuration", startDuration);
		config.set("defaultDuration", defaultDuration);
		config.set("defaultKeyLength", defaultKeyLength);
		config.set("autoKick", autoKick);
		config.set("commandWhitelist", commandWhiteList);
		if (filterNames.equals("."))
			config.set("filterNames", false);
		else
			config.set("filterNames", filterNames);
		config.set("blockDifferentNameCases", blockDifferentNameCases);
		config.set("minNameLength", minNameLength);
		config.set("maxNameLength", maxNameLength);
		super.saveConfiguration();
	}

	public long getStartDuration()
	{
		return startDuration;
	}

	public long getDefaultDuration()
	{
		return defaultDuration;
	}

	public String genCardKey()
	{
		String seed = "";
		while (true)
		{
			final long value = Math.round(Math.random() * Long.MAX_VALUE);
			seed += String.valueOf(value);
			if (seed.length() > defaultKeyLength + 1)
			{
				final String key = seed.substring(1, defaultKeyLength);
				if (cardDatabase.hasEntry(key))
					return genCardKey();
				else
					return key;
			}
		}
	}

	public String getNameFilter()
	{
		return filterNames;
	}

	public boolean checkNameChars(final String name)
	{
		return name.matches(filterNames + "*");
	}

	public boolean isBlockingDifferentNameCasesEnabled()
	{
		return blockDifferentNameCases;
	}

	public boolean checkNameCase(final String name)
	{
		if (blockDifferentNameCases)
		{
			final PlayerTimeData data = getPlayerData(name);
			if (data == null)
				return true;
			else
				return data.getName().equals(name);
		}
		else
			return true;
	}

	public int getMinNameLength()
	{
		return minNameLength;
	}

	public int getMaxNameLength()
	{
		return maxNameLength;
	}

	public boolean checkNameLength(final String name)
	{
		final int length = name.length();
		if (length < minNameLength)
			return false;
		if (length > maxNameLength)
			return false;
		return true;
	}

	public boolean isActive(final Player player)
	{
		final PlayerTimeData data = database.getEntry(player);
		if (data == null)
			return false;
		else
			return data.isActive();
	}

	@Localized("CRAZYTIMECARD.ACTIVATE.REQUEST")
	public void requestActivation(final Player player)
	{
		sendLocaleMessage("ACTIVATE.REQUEST", player);
	}

	public List<String> getCommandWhiteList()
	{
		return commandWhiteList;
	}

	public int getAutoKick()
	{
		return autoKick;
	}
}
