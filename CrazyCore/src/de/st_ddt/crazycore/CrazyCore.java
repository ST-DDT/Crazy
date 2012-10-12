package de.st_ddt.crazycore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.messaging.Messenger;

import de.st_ddt.crazycore.commands.CrazyCoreCommandLanguageTree;
import de.st_ddt.crazycore.commands.CrazyCoreCommandList;
import de.st_ddt.crazycore.commands.CrazyCoreCommandPager;
import de.st_ddt.crazycore.commands.CrazyCoreCommandPlayerInfo;
import de.st_ddt.crazycore.commands.CrazyCoreCommandPlayerDelete;
import de.st_ddt.crazycore.listener.CrazyCoreCrazyListener;
import de.st_ddt.crazycore.listener.CrazyCoreMessageListener;
import de.st_ddt.crazycore.tasks.ScheduledPermissionAllTask;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutor;
import de.st_ddt.crazyplugin.commands.CrazyCoreCommandPipe;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyPipe;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class CrazyCore extends CrazyPlugin
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
		registerHooks();
		getServer().getScheduler().scheduleAsyncDelayedTask(this, new ScheduledPermissionAllTask(), 20);
		super.onEnable();
		registerCommands();
	}

	@Override
	public void load()
	{
		super.load();
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
		String systemLanguage = System.getProperty("user.language").toLowerCase();
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
	public void save()
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
		super.save();
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
