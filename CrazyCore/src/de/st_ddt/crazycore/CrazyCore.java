package de.st_ddt.crazycore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.messaging.Messenger;

import de.st_ddt.crazycore.commands.CrazyCoreCommandLanguageTree;
import de.st_ddt.crazycore.commands.CrazyCoreCommandList;
import de.st_ddt.crazycore.commands.CrazyCoreCommandPager;
import de.st_ddt.crazycore.commands.CrazyCoreCommandPipe;
import de.st_ddt.crazycore.commands.CrazyCoreCommandPlayerDelete;
import de.st_ddt.crazycore.commands.CrazyCoreCommandPlayerInfo;
import de.st_ddt.crazycore.commands.CrazyCoreCommandPlayerWipeCommands;
import de.st_ddt.crazycore.commands.CrazyCoreCommandUpdateCheck;
import de.st_ddt.crazycore.listener.CrazyCoreCrazyListener;
import de.st_ddt.crazycore.listener.CrazyCoreMessageListener;
import de.st_ddt.crazycore.tasks.PluginUpdateCheckTask;
import de.st_ddt.crazycore.tasks.ScheduledPermissionAllTask;
import de.st_ddt.crazyplugin.CrazyLightPlugin;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutor;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyPipe;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.metrics.Metrics;
import de.st_ddt.crazyutil.metrics.Metrics.Graph;
import de.st_ddt.crazyutil.metrics.Metrics.Plotter;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public final class CrazyCore extends CrazyPlugin
{

	private static CrazyCore plugin;
	private final HashSet<String> preloadedLanguages = new HashSet<String>();
	private final HashSet<String> loadedLanguages = new HashSet<String>();
	private CrazyCoreMessageListener messageListener;
	private CrazyCoreCrazyListener crazylistener;
	private boolean wipePlayerFiles;
	private boolean wipePlayerBans;
	private final ArrayList<String> playerWipeCommands = new ArrayList<String>();
	private final TreeSet<String> protectedPlayers = new TreeSet<String>();

	public static CrazyCore getPlugin()
	{
		return plugin;
	}

	private void registerCommands()
	{
		final CrazyCommandTreeExecutor<CrazyCore> players = new CrazyCommandTreeExecutor<CrazyCore>(this);
		mainCommand.addSubCommand(players, "player", "players");
		mainCommand.addSubCommand(new CrazyCoreCommandPlayerWipeCommands(plugin), "wipecommands", "wipecmd");
		mainCommand.addSubCommand(new CrazyCoreCommandUpdateCheck(this), "updatecheck");
		players.addSubCommand(new CrazyCoreCommandPlayerInfo(this), "info");
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
		crazylistener = new CrazyCoreCrazyListener(this);
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(crazylistener, this);
		messageListener = new CrazyCoreMessageListener(this);
		final Messenger ms = getServer().getMessenger();
		ms.registerIncomingPluginChannel(this, "CrazyCore", messageListener);
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

	@Override
	public void onEnable()
	{
		PermissionModule.init(getChatHeader(), Bukkit.getConsoleSender());
		registerHooks();
		Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new ScheduledPermissionAllTask(), 20);
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
		// PlayerWipe
		wipePlayerFiles = config.getBoolean("wipePlayerFiles", true);
		wipePlayerBans = config.getBoolean("wipePlayerBans", false);
		playerWipeCommands.clear();
		final List<String> commandList = config.getStringList("playerWipeCommands");
		if (commandList != null)
			playerWipeCommands.addAll(commandList);
		final List<String> protectedList = config.getStringList("protectedPlayers");
		if (protectedList != null)
			for (final String name : protectedList)
				protectedPlayers.add(name.toLowerCase());
		// Pipes
		CrazyPipe.setDisabled(config.getBoolean("disablePipes", false));
		// ChatHeader
		ChatHelper.setShowChatHeaders(config.getBoolean("showChatHeaders", true));
		// Language
		final String systemLanguage = System.getProperty("user.language").toLowerCase();
		final String defaultLanguage = config.getString("defaultLanguage", systemLanguage + "_" + systemLanguage);
		CrazyLocale.setDefaultLanguage(defaultLanguage);
		preloadedLanguages.add(defaultLanguage);
		if (isInstalled)
			if (!defaultLanguage.equals("en_en"))
				preloadedLanguages.add("en_en");
		for (final String language : config.getStringList("preloadedLanguages"))
			preloadedLanguages.add(language);
		preloadedLanguages.addAll(CrazyLocale.load(config.getConfigurationSection("players")));
		for (final String language : preloadedLanguages)
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
		// Player Wipe
		config.set("wipePlayerFiles", wipePlayerFiles);
		config.set("wipePlayerBans", wipePlayerBans);
		config.set("playerWipeCommands", playerWipeCommands);
		final ArrayList<String> protectedList = new ArrayList<String>(protectedPlayers);
		config.set("protectedPlayers", protectedList);
		// Pipes
		config.set("disablePipes", CrazyPipe.isDisabled());
		// ChatHeader
		config.set("showChatHeaders", ChatHelper.isShowingChatHeadersEnabled());
		// Language
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

	public boolean isWipingPlayerFilesEnabled()
	{
		return wipePlayerFiles;
	}

	public boolean isWipingPlayerBansEnabled()
	{
		return wipePlayerBans;
	}

	public ArrayList<String> getPlayerWipeCommands()
	{
		return playerWipeCommands;
	}

	public TreeSet<String> getProtectedPlayers()
	{
		return protectedPlayers;
	}

	public HashSet<String> getPreloadedLanguages()
	{
		return preloadedLanguages;
	}
}
