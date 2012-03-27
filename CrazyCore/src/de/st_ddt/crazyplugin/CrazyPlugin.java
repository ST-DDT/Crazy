package de.st_ddt.crazyplugin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.PairList;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public abstract class CrazyPlugin extends JavaPlugin
{

	private String ChatHeader = null;
	protected CrazyLocale locale = null;
	private static PairList<Class<? extends CrazyPlugin>, CrazyPlugin> plugins = new PairList<Class<? extends CrazyPlugin>, CrazyPlugin>();

	public final String getChatHeader()
	{
		if (ChatHeader == null)
			ChatHeader = ChatColor.RED + "[" + ChatColor.GREEN + getDescription().getName() + ChatColor.RED + "] " + ChatColor.WHITE;
		return ChatHeader;
	}

	public static ArrayList<CrazyPlugin> getCrazyPlugins()
	{
		return plugins.getData2List();
	}

	public final static CrazyPlugin getPlugin(Class<? extends CrazyPlugin> plugin)
	{
		return plugins.findDataVia1(plugin);
	}

	@Override
	public final boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		try
		{
			if (Command(sender, commandLabel, args))
				return true;
			if (getDescription().getName().equalsIgnoreCase(commandLabel) || (commandLabel.equalsIgnoreCase(getShortPluginName())))
			{
				try
				{
					if (args.length == 0)
					{
						CommandInfo(sender, new String[0]);
						return true;
					}
					String[] newArgs = ChatHelper.shiftArray(args, 1);
					if (CommandMain(sender, args[0], newArgs))
						return true;
					if (args[0].equalsIgnoreCase("info"))
					{
						CommandInfo(sender, newArgs);
						return true;
					}
					if (args[0].equalsIgnoreCase("reload"))
					{
						CommandReload(sender, newArgs);
						return true;
					}
					if (args[0].equalsIgnoreCase("save"))
					{
						CommandSave(sender, newArgs);
						return true;
					}
					if (args[0].equalsIgnoreCase("help"))
					{
						CommandHelp(sender, newArgs);
						return true;
					}
					throw new CrazyCommandNoSuchException("Function", args[0]);
				}
				catch (CrazyCommandException e)
				{
					e.shiftCommandIndex();
					throw e;
				}
			}
		}
		catch (CrazyCommandException e)
		{
			e.setCommand(commandLabel, args);
			e.print(sender, ChatHeader);
			return true;
		}
		catch (CrazyException e)
		{
			e.print(sender, ChatHeader);
			return true;
		}
		return super.onCommand(sender, command, commandLabel, args);
	}

	public boolean Command(CommandSender sender, String commandLabel, String[] args) throws CrazyException
	{
		return false;
	}

	public boolean CommandMain(CommandSender sender, String commandLabel, String[] args) throws CrazyException
	{
		return false;
	}

	public void CommandInfo(CommandSender sender, String[] newArgs)
	{
		sender.sendMessage(ChatHeader + "Version " + getDescription().getVersion());
		sender.sendMessage(ChatHeader + "Authors " + getDescription().getAuthors().toString());
	}

	public final void CommandReload(CommandSender sender, String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission(getDescription().getName().toLowerCase() + ".reload"))
			throw new CrazyCommandPermissionException();
		if (args.length != 0)
			throw new CrazyCommandUsageException("/" + getDescription().getName().toLowerCase() + " reload");
		load();
		sendLocaleMessage(CrazyLocale.getLocaleHead().getLanguageEntry("CRAZYPLUGIN.COMMAND.CONFIG.RELOADED"), sender);
	}

	private final void CommandSave(CommandSender sender, String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission(getDescription().getName().toLowerCase() + ".save"))
			throw new CrazyCommandPermissionException();
		if (args.length != 0)
			throw new CrazyCommandUsageException("/" + getDescription().getName().toLowerCase() + " save");
		save();
		sendLocaleMessage(CrazyLocale.getLocaleHead().getLanguageEntry("CRAZYPLUGIN.COMMAND.CONFIG.SAVED"), sender);
	}

	public void CommandHelp(CommandSender sender, String[] args)
	{
		sendLocaleMessage(CrazyLocale.getLocaleHead().getLanguageEntry("CRAZYPLUGIN.COMMAND.HELP.NOHELP"), sender);
	}

	protected String getShortPluginName()
	{
		return null;
	}

	@Override
	public void onEnable()
	{
		plugins.setDataVia1(this.getClass(), this);
		getDataFolder().mkdir();
		new File(getDataFolder().getPath() + "/lang").mkdirs();
		for (String language : CrazyLocale.getLoadedLanguages())
			loadLanguage(language);
		load();
		locale = CrazyLocale.getLocaleHead().getLanguageEntry(getDescription().getName());
		ConsoleLog("Version " + getDescription().getVersion() + " enabled");
	}

	@Override
	public void onDisable()
	{
		save();
		ConsoleLog("disabled");
	}

	public void save()
	{
		saveConfig();
	}

	public void load()
	{
	}

	public final void ConsoleLog(String message)
	{
		getServer().getConsoleSender().sendMessage(getChatHeader() + message);
	}

	public final void sendLocaleMessage(String localepath, CommandSender target, String... args)
	{
		sendLocaleMessage(getLocale().getLanguageEntry(localepath), target, args);
	}

	public final void sendLocaleMessage(CrazyLocale locale, CommandSender target, String... args)
	{
		target.sendMessage(getChatHeader() + ChatHelper.putArgs(locale.getLanguageText(target), args));
	}

	public final void sendLocaleMessage(String localepath, CommandSender[] targets, String... args)
	{
		sendLocaleMessage(getLocale().getLanguageEntry(localepath), targets, args);
	}

	public final void sendLocaleMessage(CrazyLocale locale, CommandSender[] targets, String... args)
	{
		for (CommandSender target : targets)
			target.sendMessage(getChatHeader() + ChatHelper.putArgs(locale.getLanguageText(target), args));
	}

	public final void broadcastLocaleMessage(String localepath, String... args)
	{
		broadcastLocaleMessage(getLocale().getLanguageEntry(localepath), args);
	}

	public final void broadcastLocaleMessage(CrazyLocale locale, String... args)
	{
		sendLocaleMessage(locale, getServer().getConsoleSender(), args);
		sendLocaleMessage(locale, getServer().getOnlinePlayers(), args);
	}

	public final CrazyLocale getLocale()
	{
		return locale;
	}

	public void loadLanguage(String language)
	{
		loadLanguage(language, getServer().getConsoleSender());
	}

	public void loadLanguage(String language, CommandSender sender)
	{
		File file = new File(getDataFolder().getPath() + "/lang/" + language + ".lang");
		if (!file.exists())
		{
			unpackLanguage(language);
			if (!file.exists())
			{
				downloadLanguage(language);
				if (!file.exists())
				{
					sender.sendMessage("Language " + language + " not availiable for " + getDescription().getName() + "!");
					return;
				}
			}
		}
		try
		{
			FileReader reader = null;
			try
			{
				reader = new FileReader(file);
				CrazyLocale.readFile(language, reader);
			}
			finally
			{
				if (reader != null)
					reader.close();
			}
		}
		catch (IOException e)
		{
			sender.sendMessage("Failed reading " + language + " languagefile for " + getDescription().getName() + "!");
		}
	}

	public void unpackLanguage(String language)
	{
		try
		{
			InputStream in = null;
			OutputStream out = null;
			try
			{
				InputStream stream = getClass().getResourceAsStream("/resource/lang/" + language + ".lang");
				if (stream == null)
					return;
				in = new BufferedInputStream(stream);
				out = new BufferedOutputStream(new FileOutputStream(getDataFolder().getPath() + "/lang/" + language + ".lang"));
				byte data[] = new byte[1024];
				int count;
				while ((count = in.read(data, 0, 1024)) != -1)
					out.write(data, 0, count);
				out.flush();
			}
			finally
			{
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			}
		}
		catch (IOException e)
		{
			System.err.println("Error exporting " + language + " language file");
			// e.printStackTrace();
		}
	}

	protected String getMainDownloadLocation()
	{
		return "http://dl.dropbox.com/u/16999313/Bukkit/" + getDescription().getName();
	}

	public void downloadLanguage(String language)
	{
		try
		{
			BufferedInputStream in = null;
			FileOutputStream out = null;
			try
			{
				in = new BufferedInputStream(new URL(getMainDownloadLocation() + "/lang/" + language + ".lang").openStream());
				out = new FileOutputStream(getDataFolder().getPath() + "/lang/" + language + ".lang");
				byte data[] = new byte[1024];
				int count;
				while ((count = in.read(data, 0, 1024)) != -1)
					out.write(data, 0, count);
				out.flush();
			}
			finally
			{
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			}
		}
		catch (IOException e)
		{
			System.err.println("Error downloading " + language + " language file");
			// e.printStackTrace();
		}
	}
}
