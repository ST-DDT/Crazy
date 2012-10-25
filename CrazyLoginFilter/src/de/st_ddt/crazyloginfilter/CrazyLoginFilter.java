package de.st_ddt.crazyloginfilter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyloginfilter.commands.CrazyLoginFilterCommandConnection;
import de.st_ddt.crazyloginfilter.commands.CrazyLoginFilterCommandCreate;
import de.st_ddt.crazyloginfilter.commands.CrazyLoginFilterCommandDelete;
import de.st_ddt.crazyloginfilter.commands.CrazyLoginFilterCommandIP;
import de.st_ddt.crazyloginfilter.commands.CrazyLoginFilterCommandServerFilter;
import de.st_ddt.crazyloginfilter.commands.CrazyLoginFilterCommandShow;
import de.st_ddt.crazyloginfilter.data.PlayerAccessFilter;
import de.st_ddt.crazyloginfilter.databases.CrazyLoginFilterConfigurationDatabase;
import de.st_ddt.crazyloginfilter.listener.CrazyLoginFilterPlayerListener;
import de.st_ddt.crazyplugin.CrazyPlayerDataPlugin;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.databases.DatabaseType;
import de.st_ddt.crazyutil.locales.Localized;

;
public class CrazyLoginFilter extends CrazyPlayerDataPlugin<PlayerAccessFilter, PlayerAccessFilter>
{

	private static CrazyLoginFilter plugin;
	private final CrazyPluginCommandMainMode modeCommand = new CrazyPluginCommandMainMode(this);
	private CrazyLoginFilterPlayerListener playerListener;
	private PlayerAccessFilter serverFilter;
	private String filterNames;
	private boolean blockDifferentNameCases;
	private int minNameLength;
	private int maxNameLength;

	public static CrazyLoginFilter getPlugin()
	{
		return plugin;
	}

	public CrazyLoginFilter()
	{
		super();
		registerModes();
	}

	@Localized("CRAZYLOGINFILTER.MODE.CHANGE")
	private void registerModes()
	{
		modeCommand.addMode(modeCommand.new Mode<String>("filterNames", String.class)
		{

			@Override
			public String getValue()
			{
				return filterNames;
			}

			@Override
			public void showValue(final CommandSender sender)
			{
				plugin.sendLocaleMessage("MODE.CHANGE", sender, "filterNames", filterNames.equals(".") ? "disabled" : filterNames);
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
				showValue(sender);
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
			public void setValue(final Boolean newValue) throws CrazyException
			{
				blockDifferentNameCases = newValue;
				saveConfiguration();
			}

			@Override
			public Boolean getValue()
			{
				return blockDifferentNameCases;
			}
		});
		modeCommand.addMode(modeCommand.new IntegerMode("minNameLength")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				plugin.sendLocaleMessage("MODE.CHANGE", sender, name, getValue() + " characters");
			}

			@Override
			public void setValue(final Integer newValue) throws CrazyException
			{
				minNameLength = Math.min(Math.max(newValue, 1), 16);
				saveConfiguration();
			}

			@Override
			public Integer getValue()
			{
				return minNameLength;
			}
		});
		modeCommand.addMode(modeCommand.new IntegerMode("maxNameLength")
		{

			@Override
			public void showValue(final CommandSender sender)
			{
				plugin.sendLocaleMessage("MODE.CHANGE", sender, name, getValue() + " characters");
			}

			@Override
			public void setValue(final Integer newValue) throws CrazyException
			{
				maxNameLength = Math.min(Math.max(newValue, 1), 255);
				saveConfiguration();
			}

			@Override
			public Integer getValue()
			{
				return maxNameLength;
			}
		});
		modeCommand.addMode(modeCommand.new BooleanFalseMode("saveDatabaseOnShutdown")
		{

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				saveDatabaseOnShutdown = newValue;
			}

			@Override
			public Boolean getValue()
			{
				return saveDatabaseOnShutdown;
			}
		});
	}

	private void registerCommands()
	{
		mainCommand.addSubCommand(new CrazyLoginFilterCommandCreate(this), "create");
		CrazyLoginFilterCommandShow showCommand = new CrazyLoginFilterCommandShow(this);
		mainCommand.addSubCommand(showCommand, "show", "list");
		CrazyLoginFilterCommandIP ipCommand = new CrazyLoginFilterCommandIP(this);
		mainCommand.addSubCommand(ipCommand, "ip", "ips");
		CrazyLoginFilterCommandConnection connectionCommand = new CrazyLoginFilterCommandConnection(this);
		mainCommand.addSubCommand(connectionCommand, "connection", "connections");
		mainCommand.addSubCommand(new CrazyLoginFilterCommandServerFilter(this, showCommand, ipCommand, connectionCommand), "serverfilter");
		mainCommand.addSubCommand(new CrazyLoginFilterCommandDelete(this), "delete", "remove");
	}

	private void registerHooks()
	{
		this.playerListener = new CrazyLoginFilterPlayerListener(this);
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
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
	}

	@Override
	public void loadConfiguration()
	{
		super.loadConfiguration();
		final ConfigurationSection config = getConfig();
		if (config.getConfigurationSection("serverFilter") == null)
			serverFilter = new PlayerAccessFilter("serverFilter");
		else
			try
			{
				serverFilter = new PlayerAccessFilter(config.getConfigurationSection("serverFilter"));
			}
			catch (final Exception e)
			{
				System.out.println("Invalid Server Access Filter config!");
				serverFilter = new PlayerAccessFilter("serverFilter");
			}
		// PlayerNames
		filterNames = config.getString("filterNames", "false");
		if (filterNames.equals("false"))
			filterNames = ".";
		else if (filterNames.equals("true"))
			filterNames = "[a-zA-Z0-9_]";
		blockDifferentNameCases = config.getBoolean("blockDifferentNameCases", blockDifferentNameCases);
		minNameLength = Math.min(Math.max(config.getInt("minNameLength", 3), 1), 16);
		maxNameLength = Math.min(Math.max(config.getInt("maxNameLength", 16), minNameLength), 255);
	}

	@Override
	@Localized({ "CRAZYLOGINFILTER.DATABASE.ACCESSWARN $SaveType$", "CRAZYLOGINFILTER.DATABASE.LOADED $EntryCount$" })
	public void loadDatabase()
	{
		final ConfigurationSection config = getConfig();
		final String saveType = config.getString("database.saveType", "CONFIG").toUpperCase();
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
			database = new CrazyLoginFilterConfigurationDatabase(this, config.getConfigurationSection("database.CONFIG"));
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
						broadcastLocaleMessage(true, "crazyloginfilter.warndatabase", "DATABASE.ACCESSWARN", saveType);
				}
			}, 600, 600);
		}
		else
		{
			sendLocaleMessage("DATABASE.LOADED", Bukkit.getConsoleSender(), database.getAllEntries().size());
		}
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		serverFilter.saveToConfigDatabase(config, "serverFilter", new String[] { "name", "checkIP", "whitelistIP", "ips", "checkConnection", "whitelistConnection", "connections" });
		if (filterNames.equals("."))
			config.set("filterNames", false);
		else
			config.set("filterNames", filterNames);
		config.set("blockDifferentNameCases", blockDifferentNameCases);
		config.set("minNameLength", minNameLength);
		config.set("maxNameLength", maxNameLength);
		super.saveConfiguration();
	}

	public boolean checkIP(final Player player)
	{
		return checkIP(player.getName(), player.getAddress().getAddress().getHostAddress());
	}

	public boolean checkIP(final Player player, final String IP)
	{
		return checkIP(player.getName(), IP);
	}

	public boolean checkIP(final String player, final String IP)
	{
		final PlayerAccessFilter filter = getPlayerData(player);
		if (filter == null)
			return serverFilter.checkIP(IP);
		else
			return filter.checkIP(IP) && serverFilter.checkIP(IP);
	}

	public boolean checkConnection(final Player player)
	{
		return checkConnection(player.getName(), player.getAddress().getHostName());
	}

	public boolean checkConnection(final Player player, final String connection)
	{
		return checkConnection(player.getName(), connection);
	}

	public boolean checkConnection(final String player, final String connection)
	{
		final PlayerAccessFilter filter = getPlayerData(player);
		if (filter == null)
			return serverFilter.checkConnection(connection);
		return filter.checkConnection(connection) && serverFilter.checkConnection(connection);
	}

	public PlayerAccessFilter getServerAccessFilter()
	{
		return serverFilter;
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
			final PlayerAccessFilter data = getPlayerData(name);
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
}
