package de.st_ddt.crazycore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.messaging.Messenger;

import de.st_ddt.crazycore.commands.CommandLanguageTree;
import de.st_ddt.crazycore.commands.CommandList;
import de.st_ddt.crazycore.commands.CommandPager;
import de.st_ddt.crazycore.commands.CommandPipe;
import de.st_ddt.crazycore.commands.CommandPlayerAssociates;
import de.st_ddt.crazycore.commands.CommandPlayerDelete;
import de.st_ddt.crazycore.commands.CommandPlayerIPSearch;
import de.st_ddt.crazycore.commands.CommandPlayerInfo;
import de.st_ddt.crazycore.commands.CommandPlayerWipeCommands;
import de.st_ddt.crazycore.commands.CommandPlayerWipeFilePaths;
import de.st_ddt.crazycore.commands.CommandUpdateCheck;
import de.st_ddt.crazycore.listener.CrazyListener;
import de.st_ddt.crazycore.listener.MessageListener;
import de.st_ddt.crazycore.listener.PlayerListener;
import de.st_ddt.crazycore.tasks.PluginUpdateCheckTask;
import de.st_ddt.crazycore.tasks.ScheduledPermissionAllTask;
import de.st_ddt.crazyplugin.CrazyLightPlugin;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutor;
import de.st_ddt.crazyplugin.events.CrazyProtectedPlayerAccessEvent;
import de.st_ddt.crazyplugin.events.CrazyProtectedPlayerIllegalAccessEvent;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionProtectedPlayerException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyPipe;
import de.st_ddt.crazyutil.PreSetList;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.metrics.Metrics;
import de.st_ddt.crazyutil.metrics.Metrics.Graph;
import de.st_ddt.crazyutil.metrics.Metrics.Plotter;
import de.st_ddt.crazyutil.modes.BooleanFalseMode;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.LocalizedVariable;
import de.st_ddt.crazyutil.source.PermissionVariable;

@LocalizedVariable(variables = "CRAZYPLUGIN", values = "CRAZYCORE")
@PermissionVariable(variables = "CRAZYPLUGIN", values = "CRAZYCORE")
public final class CrazyCore extends CrazyPlugin
{

	private static CrazyCore plugin;
	private final List<String> wipePlayerFilePaths = new ArrayList<String>();
	private final List<String> wipePlayerCommands = new ArrayList<String>();
	private boolean wipePlayerWorldFiles;
	private boolean wipePlayerBans;
	private boolean protectedPlayersEnabled;
	private final SortedSet<String> protectedPlayers = new TreeSet<String>();
	private final List<String> protectedPlayersIllegalAccessCommands = new ArrayList<String>();
	private final Set<String> preloadedLanguages = new HashSet<String>();
	private final Set<String> loadedLanguages = new HashSet<String>();
	private boolean loadUserLanguages;
	private boolean checkForUpdates;

	public static CrazyCore getPlugin()
	{
		return plugin;
	}

	public CrazyCore()
	{
		super();
		registerPreSetLists();
		registerModes();
	}

	private void registerPreSetLists()
	{
		new PreSetList("core_protectedPlayersOnline")
		{

			@Override
			public List<String> getList()
			{
				final List<String> list = new ArrayList<String>();
				for (final OfflinePlayer player : Bukkit.getOnlinePlayers())
					if (protectedPlayers.contains(player.getName()))
						list.add(player.getName());
				return list;
			}
		};
		new PreSetList("core_protectedPlayers")
		{

			@Override
			public List<String> getList()
			{
				return new ArrayList<String>(protectedPlayers);
			}
		};
		new PreSetList("core_unprotectedPlayersOnline")
		{

			@Override
			public List<String> getList()
			{
				final List<String> list = new ArrayList<String>();
				for (final OfflinePlayer player : Bukkit.getOnlinePlayers())
					if (!protectedPlayers.contains(player.getName()))
						list.add(player.getName());
				return list;
			}
		};
		new PreSetList("core_unprotectedPlayers")
		{

			@Override
			public List<String> getList()
			{
				final List<String> list = new ArrayList<String>();
				for (final OfflinePlayer player : Bukkit.getOfflinePlayers())
					if (!protectedPlayers.contains(player.getName()))
						list.add(player.getName());
				return list;
			}
		};
	}

	private void registerModes()
	{
		modeCommand.addMode(new BooleanFalseMode(this, "wipePlayerWorldFiles")
		{

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				wipePlayerWorldFiles = newValue;
				saveConfiguration();
			}

			@Override
			public Boolean getValue()
			{
				return wipePlayerWorldFiles;
			}
		});
		modeCommand.addMode(new BooleanFalseMode(this, "wipePlayerBans")
		{

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				wipePlayerBans = newValue;
				saveConfiguration();
			}

			@Override
			public Boolean getValue()
			{
				return wipePlayerBans;
			}
		});
		modeCommand.addMode(new BooleanFalseMode(this, "loadUserLanguages")
		{

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				loadUserLanguages = newValue;
				saveConfiguration();
			}

			@Override
			public Boolean getValue()
			{
				return loadUserLanguages;
			}
		});
		modeCommand.addMode(new BooleanFalseMode(this, "checkForUpdates")
		{

			@Override
			public void setValue(final Boolean newValue) throws CrazyException
			{
				checkForUpdates = newValue;
				saveConfiguration();
			}

			@Override
			public Boolean getValue()
			{
				return checkForUpdates;
			}
		});
	}

	private void registerCommands()
	{
		final CrazyCommandTreeExecutor<CrazyCore> players = new CrazyCommandTreeExecutor<CrazyCore>(this);
		mainCommand.addSubCommand(players, "p", "plr", "player", "players");
		mainCommand.addSubCommand(new CommandPlayerWipeFilePaths(plugin), "wipefilepaths", "wipepaths");
		mainCommand.addSubCommand(new CommandPlayerWipeCommands(plugin), "wipecommands", "wipecmd");
		mainCommand.addSubCommand(new CommandUpdateCheck(this), "updatecheck");
		players.addSubCommand(new CommandPlayerInfo(this), "i", "info");
		players.addSubCommand(new CommandPlayerAssociates(this), "a", "associates");
		players.addSubCommand(new CommandPlayerIPSearch(this), "ip", "ipsearch");
		players.addSubCommand(new CommandPlayerDelete(this), "delete", "remove");
		final CrazyCommandTreeExecutor<CrazyCore> languages = new CommandLanguageTree(this);
		getCommand("language").setExecutor(languages);
		mainCommand.addSubCommand(languages, "language");
		getCommand("crazylist").setExecutor(new CommandList(this));
		getCommand("crazypage").setExecutor(new CommandPager(this));
		getCommand("crazypipe").setExecutor(new CommandPipe(this));
	}

	private void registerHooks()
	{
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new CrazyListener(this), this);
		pm.registerEvents(new PlayerListener(this), this);
		final Messenger ms = Bukkit.getMessenger();
		ms.registerIncomingPluginChannel(this, "CrazyCore", new MessageListener(this));
	}

	private void registerMetrics()
	{
		final boolean metricsEnabled = getConfig().getBoolean("metrics.enabled", true);
		getConfig().set("metrics.enabled", metricsEnabled);
		if (!metricsEnabled)
			return;
		try
		{
			final Metrics metrics = new Metrics(this);
			final Graph languageCount = metrics.createGraph("Number of loaded languages");
			for (int i = 1; i <= 10; i++)
			{
				final int j = i;
				languageCount.addPlotter(new Plotter(Integer.toString(j))
				{

					@Override
					public int getValue()
					{
						return (CrazyLocale.getActiveLanguages().size() == j) ? 1 : 0;
					}
				});
			}
			final Graph languages = metrics.createGraph("Loaded languages");
			for (final String language : CrazyLocale.getActiveShortLanguagesNames(true))
				languages.addPlotter(new Plotter(language)
				{

					@Override
					public int getValue()
					{
						return 1;
					}
				});
			final Graph crazyplugins = metrics.createGraph("CrazyPlugins");
			for (final CrazyLightPlugin plugin : CrazyLightPlugin.getCrazyLightPlugins())
				if (plugin.showMetrics())
					crazyplugins.addPlotter(new Plotter(plugin.getName())
					{

						@Override
						public int getValue()
						{
							return 1;
						}
					});
			metrics.start();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onLoad()
	{
		plugin = this;
		CrazyLocale.setDefaultLanguage(getConfig().getString("defaultLanguage", "en_en"));
		super.onLoad();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable()
	{
		PermissionModule.init(getChatHeader(), Bukkit.getConsoleSender());
		registerHooks();
		Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new ScheduledPermissionAllTask(), 20);
		if (checkForUpdates)
			Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new PluginUpdateCheckTask(), 6000, 432000);
		super.onEnable();
		registerCommands();
		registerMetrics();
	}

	@Override
	public void loadConfiguration()
	{
		super.loadConfiguration();
		final ConfigurationSection config = getConfig();
		checkForUpdates = config.getBoolean("checkForUpdates", true);
		// PlayerWipe
		wipePlayerWorldFiles = config.getBoolean("wipePlayerWorldFiles", config.getBoolean("wipePlayerFiles", true));
		wipePlayerFilePaths.clear();
		final List<String> filePathList = config.getStringList("wipePlayerFilePaths");
		if (filePathList != null)
			wipePlayerFilePaths.addAll(filePathList);
		wipePlayerCommands.clear();
		final List<String> commandList = config.getStringList("wipePlayerCommands");
		if (commandList != null)
			wipePlayerCommands.addAll(commandList);
		wipePlayerBans = config.getBoolean("wipePlayerBans", false);
		// Protected Players
		protectedPlayersEnabled = config.getBoolean("protectedPlayersEnabled", false);
		for (final String name : config.getStringList("protectedPlayers"))
			protectedPlayers.add(name.toLowerCase());
		protectedPlayersIllegalAccessCommands.addAll(config.getStringList("protectedPlayersIllegalAccessCommands"));
		// Pipes
		CrazyPipe.setDisabled(config.getBoolean("disablePipes", false));
		// ChatHeader
		ChatHelper.setShowChatHeaders(config.getBoolean("showChatHeaders", true));
		// Language
		consoleLog("Loading languages...");
		loadUserLanguages = config.getBoolean("loadUserLanguages", true);
		final String systemLanguage = System.getProperty("user.language").toLowerCase();
		String defaultLanguage = config.getString("defaultLanguage", systemLanguage + "_" + systemLanguage);
		if (defaultLanguage.startsWith("custom_"))
			defaultLanguage = defaultLanguage.substring(7);
		if (defaultLanguage.endsWith(".lang"))
			defaultLanguage = defaultLanguage.substring(0, defaultLanguage.length() - 5);
		CrazyLocale.setDefaultLanguage(defaultLanguage);
		preloadedLanguages.add(defaultLanguage);
		if (isInstalled)
			preloadedLanguages.add("en_en");
		final List<String> preloadedLanguagesList = config.getStringList("preloadedLanguages");
		for (final String language : preloadedLanguagesList)
			if (CrazyLocale.PATTERN_LANGUAGE.matcher(language).matches())
				preloadedLanguages.add(language);
		if (preloadedLanguages.size() == 0)
			preloadedLanguages.add("en_en");
		for (final String language : preloadedLanguages)
			loadLanguageFiles(language, false);
		final Set<String> userLanguages = CrazyLocale.load(config.getConfigurationSection("players"));
		if (loadUserLanguages)
			for (final String language : userLanguages)
				loadLanguageFiles(language, false);
		// Logger
		logger.createLogChannels(config.getConfigurationSection("logs"), "ProtectedPlayer");
	}

	public void loadLanguageFiles(final String language, final boolean forceUpdate)
	{
		if (!loadedLanguages.add(language))
			return;
		CrazyLocale.loadLanguage(language);
		for (final CrazyPlugin plugin : CrazyPlugin.getCrazyPlugins())
			if (plugin.isUpdated() || forceUpdate)
				plugin.updateLanguage(language, true);
			else
				plugin.loadLanguage(language);
	}

	@Override
	public void saveConfiguration()
	{
		final ConfigurationSection config = getConfig();
		config.set("checkForUpdates", checkForUpdates);
		// Player Wipe
		config.set("wipePlayerWorldFiles", wipePlayerWorldFiles);
		config.set("wipePlayerFilePaths", wipePlayerFilePaths);
		config.set("wipePlayerCommands", wipePlayerCommands);
		config.set("wipePlayerBans", wipePlayerBans);
		// Protected Players
		config.set("protectedPlayersEnabled", protectedPlayersEnabled);
		config.set("protectedPlayers", new ArrayList<String>(protectedPlayers));
		config.set("protectedPlayersIllegalAccessCommands", protectedPlayersIllegalAccessCommands);
		// Pipes
		config.set("disablePipes", CrazyPipe.isDisabled());
		// ChatHeader
		config.set("showChatHeaders", ChatHelper.isShowingChatHeadersEnabled());
		// Language
		config.set("loadUserLanguages", loadUserLanguages);
		config.set("defaultLanguage", CrazyLocale.getDefaultLanguage());
		config.set("preloadedLanguages", new ArrayList<String>(preloadedLanguages));
		config.set("players", null);
		CrazyLocale.save(config, "players.");
		super.saveConfiguration();
	}

	@Override
	public boolean showMetrics()
	{
		return false;
	}

	public boolean isWipingPlayerWorldFilesEnabled()
	{
		return wipePlayerWorldFiles;
	}

	public List<String> getWipePlayerFilePaths()
	{
		return wipePlayerFilePaths;
	}

	public List<String> getWipePlayerCommands()
	{
		return wipePlayerCommands;
	}

	public boolean isWipingPlayerBansEnabled()
	{
		return wipePlayerBans;
	}

	public SortedSet<String> getProtectedPlayers()
	{
		return protectedPlayers;
	}

	public boolean isProtectedPlayer(final OfflinePlayer player)
	{
		return isProtectedPlayer(player.getName());
	}

	public boolean isProtectedPlayer(final String name)
	{
		return protectedPlayers.contains(name.toLowerCase());
	}

	public void checkProtectedPlayer(final String accessedPlayer, final CommandSender accessor, final String permission, final String plugin, final String task) throws CrazyCommandException
	{
		if (accessor instanceof Player)
			checkProtectedPlayer(accessedPlayer, (Player) accessor, permission, plugin, task);
	}

	@Localized({ "CRAZYCORE.PROTECTEDPLAYER.ACCESSWARN $ProtectedPlayer$ $AccessingPlayer$ $AccessingPlayerIP$ $Plugin$ $Task$", "CRAZYCORE.PROTECTEDPLAYER.ILLEGALACCESSWARN.USER $ProtectedPlayer$ $AccessingPlayer$ $AccessingPlayerIP$ $Plugin$ $Task$", "CRAZYCORE.PROTECTEDPLAYER.ILLEGALACCESSWARN.STAFF $ProtectedPlayer$ $AccessingPlayer$ $AccessingPlayerIP$ $Plugin$ $Task$" })
	public void checkProtectedPlayer(final String accessedPlayer, final Player accessingPlayer, final String permission, final String plugin, final String task) throws CrazyCommandException
	{
		if (isProtectedPlayer(accessedPlayer))
		{
			final String accessingPlayerIP = accessingPlayer.getAddress().getAddress().getHostAddress();
			final Object[] args = new String[] { accessedPlayer, accessingPlayer.getName(), accessingPlayerIP, plugin, task };
			if (PermissionModule.hasPermission(accessingPlayer, permission))
			{
				final CrazyProtectedPlayerAccessEvent event = new CrazyProtectedPlayerAccessEvent(accessedPlayer, accessingPlayer, plugin, task);
				event.callEvent();
				if (event.isCancelled())
					throw new CrazyCommandPermissionException();
				logger.log("ProtectedPlayer", accessingPlayer.getName() + " @ " + accessingPlayerIP + "accessed a protected player (" + accessedPlayer + ")", plugin + " Task: " + task);
				broadcastLocaleMessage(true, "crazycore.protectedplayer.accesswarn", "PROTECTEDPLAYER.ACCESSWARN", args);
			}
			else
			{
				new CrazyProtectedPlayerIllegalAccessEvent(accessedPlayer, accessingPlayer, plugin, task).callEvent();
				logger.log("ProtectedPlayer", "WARNING: " + accessingPlayer.getName() + " @ " + accessingPlayerIP + "tried to access a protected player (" + accessedPlayer + ")", plugin + " Task: " + task);
				broadcastLocaleMessage(false, "crazycore.protectedplayer.illegalaccesswarn.user", "PROTECTEDPLAYER.ILLEGALACCESSWARN.USER", args);
				broadcastLocaleMessage(true, "crazycore.protectedplayer.illegalaccesswarn.staff", "PROTECTEDPLAYER.ILLEGALACCESSWARN.STAFF", args);
				final ConsoleCommandSender console = Bukkit.getConsoleSender();
				for (final String command : protectedPlayersIllegalAccessCommands)
					Bukkit.dispatchCommand(console, ChatHelper.putArgs(command, args));
				throw new CrazyCommandPermissionProtectedPlayerException(accessedPlayer);
			}
		}
	}

	public Set<String> getPreloadedLanguages()
	{
		return preloadedLanguages;
	}

	public boolean isLoadingUserLanguagesEnabled()
	{
		return loadUserLanguages;
	}
}
