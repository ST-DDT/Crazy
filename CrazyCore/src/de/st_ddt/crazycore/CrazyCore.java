package de.st_ddt.crazycore;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import de.st_ddt.crazyplugin.CrazyPlugin;
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

	public static CrazyCore getPlugin()
	{
		return plugin;
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		super.onEnable();
	}

	@Override
	public boolean Command(CommandSender sender, String commandLabel, String[] args) throws CrazyException
	{
		if (commandLabel.equalsIgnoreCase("crazylist"))
		{
			CommandList(sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("crazylanguage") || commandLabel.equalsIgnoreCase("language"))
		{
			CommandLanguage(sender, args);
			return true;
		}
		return false;
	}

	private void CommandList(CommandSender sender, String[] args) throws CrazyCommandException
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
				catch (NumberFormatException e)
				{
					throw new CrazyCommandParameterException(1, "Integer");
				}
				break;
			default:
				throw new CrazyCommandUsageException("/crazylist [Page]");
		}
		ArrayList<CrazyPlugin> list = getCrazyPlugins();
		int lastIndex = list.size();
		if (lastIndex + 9 < page * 10)
		{
			sendLocaleMessage("COMMAND.PLUGINLIST.EMPTYPAGE", sender, String.valueOf(page));
			return;
		}
		lastIndex = Math.min(lastIndex, page * 10);
		sendLocaleMessage("COMMAND.PLUGINLIST.HEADER", sender, String.valueOf(page));
		for (int i = page * 10 - 10; i < lastIndex; i++)
			sendLocaleMessage("COMMAND.PLUGINLIST.ENTRY", sender, String.valueOf(i + 1), list.get(i).getDescription().getName(), list.get(i).getDescription().getVersion());
	}

	private void CommandLanguage(CommandSender sender, String[] args) throws CrazyCommandException
	{
		switch (args.length)
		{
			case 0:
				String list = ChatHelper.listToString(CrazyLocale.getLoadedLanguages());
				sendLocaleMessage("COMMAND.LANGUAGE.CURRENT", sender, CrazyLocale.getUserLanguage(sender));
				sendLocaleMessage("COMMAND.LANGUAGE.LIST.HEADER", sender);
				sendLocaleMessage("COMMAND.LANGUAGE.LIST.ENTRY", sender, list);
				return;
			case 1:
				CrazyLocale.setUserLanguage(sender, args[0]);
				save();
				sendLocaleMessage("COMMAND.LANGUAGE.CHANGED", sender, args[0]);
				return;
			default:
				throw new CrazyCommandUsageException("/" + getDescription().getName().toLowerCase() + " language [Language]");
		}
	}

	@Override
	public void load()
	{
		super.load();
		FileConfiguration config = getConfig();
		for (String language : config.getStringList("defaultLanguages"))
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
		FileConfiguration config = getConfig();
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
