package de.st_ddt.crazycore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.messaging.Messenger;

import de.st_ddt.crazycore.data.PseudoPlayerData;
import de.st_ddt.crazycore.listener.CrazyCoreCrazyListener;
import de.st_ddt.crazycore.listener.CrazyCoreMessageListener;
import de.st_ddt.crazycore.tasks.ScheduledPermissionAllTask;
import de.st_ddt.crazyplugin.CrazyLightPlugin;
import de.st_ddt.crazyplugin.CrazyPlayerDataPlugin;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.PluginDataGetter;
import de.st_ddt.crazyplugin.data.ParameterData;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyplugin.events.CrazyPlayerRemoveEvent;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyPipe;
import de.st_ddt.crazyutil.DepenciesComparator;
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

	public static CrazyCore getPlugin()
	{
		return plugin;
	}

	@Override
	public void onLoad()
	{
		CrazyLocale.setDefaultLanguage(getConfig().getString("defaultLanguage", "en_en"));
		super.onLoad();
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		registerHooks();
		getServer().getScheduler().scheduleAsyncDelayedTask(this, new ScheduledPermissionAllTask(), 20);
		super.onEnable();
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
	public boolean command(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		if (commandLabel.equalsIgnoreCase("crazylist"))
		{
			commandList(sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("crazylanguage") || commandLabel.equalsIgnoreCase("language"))
		{
			commandLanguage(sender, args);
			return true;
		}
		return false;
	}

	private void commandList(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazycore.list"))
			throw new CrazyCommandPermissionException();
		int page = 1;
		int amount = 10;
		String[] pipe = null;
		final int length = args.length;
		for (int i = 0; i < length; i++)
		{
			final String arg = args[i].toLowerCase();
			if (arg.startsWith("page:"))
				try
				{
					page = Integer.parseInt(arg.substring(5));
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(i, "page:Integer");
				}
			else if (arg.toLowerCase().startsWith("amount:"))
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
					throw new CrazyCommandUsageException("/crazylist [amount:Integer] [[page:]Integer]");
				}
		}
		final ArrayList<CrazyLightPlugin> dataList = new ArrayList<CrazyLightPlugin>();
		dataList.addAll(getCrazyPlugins());
		Collections.sort(dataList, new DepenciesComparator());
		if (pipe != null)
		{
			final ArrayList<ParameterData> datas = new ArrayList<ParameterData>(dataList);
			CrazyPipe.pipe(sender, datas, pipe);
			return;
		}
		sendListMessage(sender, "COMMAND.PLUGINLIST.HEADER", amount, page, dataList, new PluginDataGetter());
	}

	private void commandLanguage(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		switch (args.length)
		{
			case 1:
				if (!args[0].equalsIgnoreCase("list"))
				{
					final String language = args[0].toLowerCase();
					if (!language.matches("[a-z][a-z]_[a-z][a-z]"))
						throw new CrazyCommandNoSuchException("Language", args[0], CrazyLocale.getActiveLanguagesNames(true));
					loadLanguageFiles(language, false);
					CrazyLocale.setUserLanguage(sender, language);
					save();
					sendLocaleMessage("COMMAND.LANGUAGE.CHANGED", sender, CrazyLocale.getLanguageName(), language);
					return;
				}
			case 0:
				sendLocaleMessage("COMMAND.LANGUAGE.CURRENT", sender, CrazyLocale.getLanguageName(), CrazyLocale.getUserLanguage(sender));
				sendLocaleMessage("COMMAND.LANGUAGE.DEFAULT", sender, CrazyLocale.getLanguageName().getDefaultLanguageText(), CrazyLocale.getDefaultLanguage());
				sendLocaleMessage("COMMAND.LANGUAGE.LIST.HEADER", sender);
				final String languages = ChatHelper.listingString(CrazyLocale.getActiveLanguagesNames(true));
				sendLocaleMessage("COMMAND.LANGUAGE.LIST.ENTRY", sender, languages);
				return;
			case 2:
				if (!sender.hasPermission("crazylanguage.advanced"))
					throw new CrazyCommandPermissionException();
				if (args[0].equalsIgnoreCase("print"))
				{
					if (args[1].equalsIgnoreCase("*"))
					{
						CrazyLocale.printAll(sender);
						return;
					}
					CrazyLocale.getLocaleHead().getLanguageEntry(args[1]).print(sender);
					return;
				}
				if (args[0].equalsIgnoreCase("link"))
				{
					final CrazyLocale locale = CrazyLocale.getLocaleHead().getLanguageEntry(args[1]);
					if (locale.getAlternative() == null)
						sender.sendMessage(locale.getPath() + " <= (null)");
					else
						sender.sendMessage(locale.getPath() + " <= " + locale.getAlternative().getPath());
					return;
				}
				else if (args[0].equalsIgnoreCase("setdefault"))
				{
					final String newDefault = args[1].toLowerCase();
					if (!CrazyLocale.getDefaultLanguage().equals(newDefault))
					{
						CrazyLocale.setDefaultLanguage(args[1]);
						loadLanguageFiles(args[1], true);
					}
					sendLocaleMessage("COMMAND.LANGUAGE.DEFAULT.SET", sender, args[1]);
					return;
				}
				else if (args[0].equalsIgnoreCase("addpreloaded"))
				{
					if (preloadedLanguages.add(args[1].toLowerCase()))
						loadLanguageFiles(args[1], true);
					sendLocaleMessage("COMMAND.LANGUAGE.DEFAULT.ADDED", sender, args[1]);
					return;
				}
				else if (args[0].equalsIgnoreCase("removepreloaded"))
				{
					preloadedLanguages.remove(args[1]);
					sendLocaleMessage("COMMAND.LANGUAGE.DEFAULT.REMOVED", sender, args[1]);
					return;
				}
				else if (args[0].equalsIgnoreCase("download"))
				{
					final String download = args[1];
					if (download.equalsIgnoreCase("*"))
					{
						for (final String language : CrazyLocale.getLoadedLanguages())
						{
							for (final CrazyPlugin plugin : getCrazyPlugins())
							{
								plugin.updateLanguage(language, sender, true);
								plugin.checkLocale();
							}
							sendLocaleMessage("COMMAND.LANGUAGE.DEFAULT.RELOADED", sender, language);
						}
						return;
					}
					final CrazyPlugin plugin = CrazyPlugin.getPlugin(download);
					if (plugin != null)
					{
						for (final String language : CrazyLocale.getLoadedLanguages())
						{
							plugin.updateLanguage(language, sender, true);
							plugin.checkLocale();
							sendLocaleMessage("COMMAND.LANGUAGE.DEFAULT.RELOADED.PLUGIN", sender, language, plugin.getName());
						}
						return;
					}
					for (final CrazyPlugin plugin2 : getCrazyPlugins())
					{
						plugin2.updateLanguage(download, sender, true);
						plugin2.checkLocale();
					}
					sendLocaleMessage("COMMAND.LANGUAGE.DEFAULT.DOWNLOADED", sender, download);
					return;
				}
				else if (args[0].equalsIgnoreCase("reload"))
				{
					final String reload = args[1];
					if (reload.equalsIgnoreCase("*"))
					{
						for (final String language : CrazyLocale.getLoadedLanguages())
						{
							for (final CrazyPlugin plugin : getCrazyPlugins())
							{
								plugin.loadLanguage(language, sender);
								plugin.checkLocale();
							}
							sendLocaleMessage("COMMAND.LANGUAGE.DEFAULT.RELOADED", sender, language);
						}
						return;
					}
					final CrazyPlugin plugin = getPlugin(reload);
					if (plugin != null)
					{
						for (final String language : CrazyLocale.getLoadedLanguages())
						{
							plugin.loadLanguage(language, sender);
							plugin.checkLocale();
							sendLocaleMessage("COMMAND.LANGUAGE.DEFAULT.RELOADED.PLUGIN", sender, language, plugin.getName());
						}
						return;
					}
					for (final CrazyPlugin plugin2 : getCrazyPlugins())
					{
						plugin2.loadLanguage(reload, sender);
						plugin2.checkLocale();
					}
					sendLocaleMessage("COMMAND.LANGUAGE.DEFAULT.RELOADED", sender, reload);
					return;
				}
				else
					throw new CrazyCommandUsageException("/crazylanguage setdefault <Language>", "/crazylanguage adddefault <Language>", "/crazylanguage removedefault <Language>", "/crazylanguage download <Landuage>", "/crazylanguage reload <Landuage>");
			default:
				throw new CrazyCommandUsageException("/crazylanguage [Language]");
		}
	}

	@Override
	public boolean commandMain(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		if (commandLabel.equals("info"))
		{
			commandMainInfo(sender, args);
			return true;
		}
		if (commandLabel.equals("delete"))
		{
			commandMainDelete(sender, args);
			return true;
		}
		return false;
	}

	private void commandMainInfo(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("/crazycore info <Player>");
		commandMainInfo(sender, args[0]);
	}

	private void commandMainInfo(final CommandSender sender, final String name) throws CrazyCommandException
	{
		if (!sender.hasPermission("crazycore.infoplayer"))
			throw new CrazyCommandPermissionException();
		OfflinePlayer player = Bukkit.getPlayer(name);
		if (player == null)
			player = Bukkit.getOfflinePlayer(name);
		if (player == null)
			throw new CrazyCommandNoSuchException("Player", name);
		new PseudoPlayerData(player.getName()).show(sender);
		for (final CrazyPlayerDataPlugin<? extends PlayerDataInterface, ? extends PlayerDataInterface> plugin : CrazyPlayerDataPlugin.getCrazyPlayerDataPlugins())
		{
			final PlayerDataInterface data = plugin.getAvailablePlayerData(name);
			if (data != null)
			{
				sendLocaleMessage("PLAYERINFO.SEPERATOR", sender);
				data.showDetailed(sender, plugin.getChatHeader());
			}
		}
	}

	private void commandMainDelete(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("/crazycore delete <Player>");
		commandMainDelete(sender, args[0]);
	}

	private void commandMainDelete(final CommandSender sender, String name) throws CrazyCommandException
	{
		final Player player = Bukkit.getPlayer(name);
		if (player != null)
			name = player.getName();
		if (sender.getName().equalsIgnoreCase(name))
			if (!sender.hasPermission("crazycore.deleteplayer.self"))
				throw new CrazyCommandPermissionException();
			else if (!sender.hasPermission("crazycore.deleteplayer.other"))
				throw new CrazyCommandPermissionException();
		final CrazyPlayerRemoveEvent event = new CrazyPlayerRemoveEvent(plugin, name);
		event.callEvent();
		sendLocaleMessage("COMMAND.DELETE.HEAD", sender, name);
		sendLocaleMessage("COMMAND.DELETE.AMOUNT", sender, event.getDeletionsCount());
		if (event.getDeletionsCount() != 0)
		{
			sendLocaleMessage("COMMAND.DELETE.LISTHEAD", sender, event.getDeletionsList());
			sendLocaleMessage("COMMAND.DELETE.LIST", sender, event.getDeletionsList());
		}
		save();
	}

	@Override
	public void load()
	{
		super.load();
		final ConfigurationSection config = getConfig();
		wipePlayerFiles = config.getBoolean("wipePlayerFiles", true);
		wipePlayerBans = config.getBoolean("wipePlayerBans", false);
		playerWipeCommands.clear();
		List<String> list = config.getStringList("playerWipeCommands");
		if (list != null)
			playerWipeCommands.addAll(list);
		CrazyPipe.setDisabled(config.getBoolean("disablePipes", false));
		ChatHelper.setShowChatHeaders(config.getBoolean("showChatHeaders", true));
		final String defaultLanguage = config.getString("defaultLanguage", "en_en");
		CrazyLocale.setDefaultLanguage(defaultLanguage);
		preloadedLanguages.add(defaultLanguage);
		for (final String language : config.getStringList("preloadedLanguages"))
			preloadedLanguages.add(language);
		preloadedLanguages.addAll(CrazyLocale.load(config.getConfigurationSection("players")));
		for (final String language : preloadedLanguages)
			loadLanguageFiles(language, false);
	}

	private void loadLanguageFiles(final String language, final boolean forceUpdate)
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
		config.set("wipePlayerFiles", wipePlayerFiles);
		config.set("wipePlayerBans", wipePlayerBans);
		config.set("playerWipeCommands", playerWipeCommands);
		config.set("disablePipes", CrazyPipe.isDisabled());
		config.set("showChatHeaders", ChatHelper.isShowingChatHeadersEnabled());
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
}
