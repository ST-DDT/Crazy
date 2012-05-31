package de.st_ddt.crazycore;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.events.CrazyPlayerRemoveEvent;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class CrazyCore extends CrazyPlugin
{

	protected static CrazyCore plugin;
	protected static final ArrayList<String> defaultLanguages = new ArrayList<String>();
	protected static String defaultLanguage;
	private CrazyCoreMessageListener messageListener;
	private CrazyCoreCrazyListener crazylistener;

	public static CrazyCore getPlugin()
	{
		return plugin;
	}

	@Override
	public void onLoad()
	{
		defaultLanguage = getConfig().getString("defaultLanguage", "en_en");
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
		this.crazylistener = new CrazyCoreCrazyListener(this);
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
		int page;
		switch (args.length)
		{
			case 0:
				page = 1;
				break;
			case 1:
				try
				{
					page = Integer.parseInt(args[0]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(1, "Integer");
				}
				break;
			default:
				throw new CrazyCommandUsageException("/crazylist [Page]");
		}
		final ArrayList<JavaPlugin> list = new ArrayList<JavaPlugin>();
		list.addAll(getCrazyLightPlugins());
		sendListMessage(sender, "COMMAND.PLUGINLIST.HEADER", page, list, new PluginDataGetter());
	}

	private void commandLanguage(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		switch (args.length)
		{
			case 0:
				sendLocaleMessage("COMMAND.LANGUAGE.CURRENT", sender, CrazyLocale.getLanguageName(), CrazyLocale.getUserLanguage(sender));
				sendLocaleMessage("COMMAND.LANGUAGE.DEFAULT", sender, CrazyLocale.getLanguageName().getDefaultLanguageText(), getDefaultLanguage());
				sendLocaleMessage("COMMAND.LANGUAGE.LIST.HEADER", sender);
				final String languages = ChatHelper.listingString(CrazyLocale.getActiveLanguagesNames(true));
				sendLocaleMessage("COMMAND.LANGUAGE.LIST.ENTRY", sender, languages);
				return;
			case 1:
				CrazyLocale.setUserLanguage(sender, args[0]);
				save();
				sendLocaleMessage("COMMAND.LANGUAGE.CHANGED", sender, args[0]);
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
				else if (args[0].equalsIgnoreCase("setdefault"))
				{
					defaultLanguage = args[1];
					CrazyLocale.loadLanguage(defaultLanguage);
					sendLocaleMessage("COMMAND.LANGUAGE.DEFAULT.SET", sender, defaultLanguage);
					return;
				}
				else if (args[0].equalsIgnoreCase("adddefault"))
				{
					defaultLanguages.add(args[1]);
					CrazyLocale.loadLanguage(args[1]);
					sendLocaleMessage("COMMAND.LANGUAGE.DEFAULT.ADDED", sender, args[1]);
					return;
				}
				else if (args[0].equalsIgnoreCase("removedefault"))
				{
					defaultLanguages.remove(args[1]);
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
		if (commandLabel.equalsIgnoreCase("delete"))
		{
			commanMainDelete(sender, args);
			return true;
		}
		return false;
	}

	private void commanMainDelete(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("/crazycore delete <Player>");
		String name = args[0];
		final Player player = getServer().getPlayer(name);
		final OfflinePlayer plr = getServer().getOfflinePlayer(name);
		if (player != null)
			name = player.getName();
		if (sender.getName().equalsIgnoreCase(name))
			if (!sender.hasPermission("crazycore.deleteplayer.self"))
				throw new CrazyCommandPermissionException();
			else if (!sender.hasPermission("crazycore.deleteplayer.other"))
				throw new CrazyCommandPermissionException();
		final CrazyPlayerRemoveEvent event = new CrazyPlayerRemoveEvent(plugin, name);
		getServer().getPluginManager().callEvent(event);
		sendLocaleMessage("COMMAND.DELETE.HEAD", sender, name);
		sendLocaleMessage("COMMAND.DELETE.AMOUNT", sender, event.getDeletionsCount());
		sendLocaleMessage("COMMAND.DELETE.LISTHEAD", sender, event.getDeletionsList());
		sendLocaleMessage("COMMAND.DELETE.LIST", sender, event.getDeletionsList());
		if (player != null)
			if (player.isOnline())
			{
				player.leaveVehicle();
				player.getInventory().clear();
				player.setGameMode(getServer().getDefaultGameMode());
				player.setExp(0);
				player.setFoodLevel(20);
				player.setHealth(20);
				player.setFireTicks(0);
				player.resetPlayerTime();
				final Location spawn = getServer().getWorlds().get(0).getSpawnLocation();
				player.setCompassTarget(spawn);
				player.teleport(spawn);
				player.setBedSpawnLocation(spawn);
				player.saveData();
				player.kickPlayer(locale.getLocaleMessage(player, "COMMAND.DELETE.KICK"));
			}
		if (plr != null)
		{
			plr.setBanned(false);
			plr.setOp(false);
			plr.setWhitelisted(false);
		}
	}

	@Override
	public void load()
	{
		super.load();
		final ConfigurationSection config = getConfig();
		for (final String language : config.getStringList("defaultLanguages"))
		{
			defaultLanguages.add(language);
			CrazyLocale.loadLanguage(language);
		}
		defaultLanguage = config.getString("defaultLanguage", "en_en");
		CrazyLocale.loadLanguage(defaultLanguage);
		CrazyLocale.load(config.getConfigurationSection("players"));
	}

	@Override
	public void save()
	{
		final ConfigurationSection config = getConfig();
		config.set("defaultLanguage", defaultLanguage);
		config.set("defaultLanguages", defaultLanguages);
		CrazyLocale.save(config, "players.");
		super.save();
	}

	public static String getDefaultLanguage()
	{
		return defaultLanguage;
	}

	public static ArrayList<String> getDefaultlanguages()
	{
		return defaultLanguages;
	}
}
