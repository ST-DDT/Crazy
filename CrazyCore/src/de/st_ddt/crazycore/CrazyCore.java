package de.st_ddt.crazycore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.messaging.Messenger;

import de.st_ddt.crazycore.commands.CrazyCoreCommandLanguageTree;
import de.st_ddt.crazycore.commands.CrazyCoreCommandList;
import de.st_ddt.crazycore.commands.CrazyCoreCommandPager;
import de.st_ddt.crazycore.commands.CrazyCoreCommandPipe;
import de.st_ddt.crazycore.commands.CrazyCoreCommandPlayerAssociates;
import de.st_ddt.crazycore.commands.CrazyCoreCommandPlayerDelete;
import de.st_ddt.crazycore.commands.CrazyCoreCommandPlayerIPSearch;
import de.st_ddt.crazycore.commands.CrazyCoreCommandPlayerInfo;
import de.st_ddt.crazycore.commands.CrazyCoreCommandPlayerWipeCommands;
import de.st_ddt.crazycore.commands.CrazyCoreCommandPlayerWipeFilePaths;
import de.st_ddt.crazycore.commands.CrazyCoreCommandUpdateCheck;
import de.st_ddt.crazycore.listener.CrazyListener;
import de.st_ddt.crazycore.listener.MessageListener;
import de.st_ddt.crazycore.tasks.PluginUpdateCheckTask;
import de.st_ddt.crazycore.tasks.ScheduledPermissionAllTask;
import de.st_ddt.crazyplugin.CrazyLightPlugin;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutor;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyPipe;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.metrics.Metrics;
import de.st_ddt.crazyutil.metrics.Metrics.Graph;
import de.st_ddt.crazyutil.metrics.Metrics.Plotter;
import de.st_ddt.crazyutil.modes.BooleanFalseMode;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.LocalizedVariable;

@LocalizedVariable(variables = "CRAZYPLUGIN", values = "CRAZYCORE")
public final class CrazyCore extends CrazyPlugin
{

	private static CrazyCore plugin;
	private final Set<String> preloadedLanguages = new HashSet<String>();
	private final Set<String> loadedLanguages = new HashSet<String>();
	private final List<String> wipePlayerFilePaths = new ArrayList<String>();
	private final List<String> wipePlayerCommands = new ArrayList<String>();
	private final SortedSet<String> protectedPlayers = new TreeSet<String>();
	private boolean wipePlayerWorldFiles;
	private boolean wipePlayerBans;
	private boolean loadUserLanguages;
	private boolean checkForUpdates;

	public static CrazyCore getPlugin()
	{
		return plugin;
	}

	public CrazyCore()
	{
		super();
		registerModes();
	}

	private void registerModes()
	{
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
		mainCommand.addSubCommand(new CrazyCoreCommandPlayerWipeFilePaths(plugin), "wipefilepaths", "wipepaths");
		mainCommand.addSubCommand(new CrazyCoreCommandPlayerWipeCommands(plugin), "wipecommands", "wipecmd");
		mainCommand.addSubCommand(new CrazyCoreCommandUpdateCheck(this), "updatecheck");
		players.addSubCommand(new CrazyCoreCommandPlayerInfo(this), "i", "info");
		players.addSubCommand(new CrazyCoreCommandPlayerAssociates(this), "a", "associates");
		players.addSubCommand(new CrazyCoreCommandPlayerIPSearch(this), "ip", "ipsearch");
		players.addSubCommand(new CrazyCoreCommandPlayerDelete(this), "delete", "remove");
		final CrazyCommandTreeExecutor<CrazyCore> languages = new CrazyCoreCommandLanguageTree(this);
		getCommand("language").setExecutor(languages);
		mainCommand.addSubCommand(languages, "language");
		getCommand("crazylist").setExecutor(new CrazyCoreCommandList(this));
		getCommand("crazypage").setExecutor(new CrazyCoreCommandPager(this));
		getCommand("crazypipe").setExecutor(new CrazyCoreCommandPipe(this));
	}

	private void registerHooks()
	{
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new CrazyListener(this), this);
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
		final List<String> protectedList = config.getStringList("protectedPlayers");
		if (protectedList != null)
			for (final String name : protectedList)
				protectedPlayers.add(name.toLowerCase());
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
		config.set("protectedPlayers", new ArrayList<String>(protectedPlayers));
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

	public Set<String> getPreloadedLanguages()
	{
		return preloadedLanguages;
	}

	public boolean isLoadingUserLanguagesEnabled()
	{
		return loadUserLanguages;
	}
}
