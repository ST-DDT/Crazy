package de.st_ddt.crazyplugin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyplugin.tasks.LanguageLoadTask;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.CrazyLogger;
import de.st_ddt.crazyutil.EntryDataGetter;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public abstract class CrazyPlugin extends CrazyLightPlugin implements CrazyPluginInterface
{

	private static final LinkedHashMap<Class<? extends CrazyPlugin>, CrazyPlugin> plugins = new LinkedHashMap<Class<? extends CrazyPlugin>, CrazyPlugin>();
	protected final CrazyLogger logger = new CrazyLogger(this);
	protected CrazyLocale locale = null;
	protected boolean isUpdated = false;
	protected boolean isInstalled = false;

	public static Collection<CrazyPlugin> getCrazyPlugins()
	{
		return plugins.values();
	}

	public final static CrazyPlugin getPlugin(final Class<? extends CrazyPlugin> plugin)
	{
		return plugins.get(plugin);
	}

	public final static CrazyPlugin getPlugin(final String name)
	{
		for (final CrazyPlugin plugin : plugins.values())
			if (plugin.getName().equalsIgnoreCase(name))
				return plugin;
		return null;
	}

	public final boolean isInstalled()
	{
		return isInstalled;
	}

	@Override
	public final boolean isUpdated()
	{
		return isUpdated;
	}

	@Override
	public final boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args)
	{
		try
		{
			if (command(sender, commandLabel.toLowerCase(), args))
				return true;
			if (getDescription().getName().equalsIgnoreCase(commandLabel) || (commandLabel.equalsIgnoreCase(getShortPluginName())))
			{
				try
				{
					if (args.length == 0)
					{
						commandInfo(sender, new String[0]);
						return true;
					}
					final String[] newArgs = ChatHelper.shiftArray(args, 1);
					String commandString = args[0].toLowerCase();
					if (commandMain(sender, commandString, newArgs))
						return true;
					if (commandString.equals("info"))
					{
						commandInfo(sender, newArgs);
						return true;
					}
					if (commandString.equals("reload"))
					{
						commandReload(sender, newArgs);
						return true;
					}
					if (commandString.equals("save"))
					{
						commandSave(sender, newArgs);
						return true;
					}
					if (commandString.equals("help"))
					{
						commandHelp(sender, newArgs);
						return true;
					}
					throw new CrazyCommandNoSuchException("Function", args[0]);
				}
				catch (final CrazyCommandException e)
				{
					e.shiftCommandIndex();
					throw e;
				}
			}
		}
		catch (final CrazyCommandException e)
		{
			e.setCommand(commandLabel, args);
			e.print(sender, getChatHeader());
			return true;
		}
		catch (final CrazyException e)
		{
			e.print(sender, getChatHeader());
			return true;
		}
		return super.onCommand(sender, command, commandLabel, args);
	}

	@Override
	public boolean command(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		return false;
	}

	@Override
	public boolean commandMain(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		return false;
	}

	protected void commandInfo(final CommandSender sender, final String[] newArgs)
	{
		show(sender, getChatHeader(), true);
	}

	private final void commandReload(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission(getDescription().getName().toLowerCase() + ".reload"))
			throw new CrazyCommandPermissionException();
		if (args.length != 0)
			throw new CrazyCommandUsageException("/" + getDescription().getName().toLowerCase() + " reload");
		reloadConfig();
		load();
		sendLocaleMessage("COMMAND.CONFIG.RELOADED", sender);
	}

	private final void commandSave(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (!sender.hasPermission(getDescription().getName().toLowerCase() + ".save"))
			throw new CrazyCommandPermissionException();
		if (args.length != 0)
			throw new CrazyCommandUsageException("/" + getDescription().getName().toLowerCase() + " save");
		save();
		sendLocaleMessage("COMMAND.CONFIG.SAVED", sender);
	}

	public void commandHelp(final CommandSender sender, final String[] args)
	{
		sendLocaleMessage("COMMAND.HELP.NOHELP", sender);
	}

	protected String getShortPluginName()
	{
		return null;
	}

	@Override
	public void onLoad()
	{
		plugins.put(this.getClass(), this);
		getDataFolder().mkdir();
		new File(getDataFolder().getPath() + "/lang").mkdirs();
		checkLocale();
		final ConfigurationSection config = getConfig();
		isInstalled = config.getString("version", "").equals("");
		isUpdated = !config.getString("version", "").equals(getDescription().getVersion());
		config.set("version", getDescription().getVersion());
		super.onLoad();
	}

	@Override
	public void onEnable()
	{
		if (isUpdated)
			broadcastLocaleRootMessage("CRAZYPLUGIN.UPDATED", getName(), getDescription().getVersion());
		load();
		if (isUpdated)
			save();
		super.onEnable();
	}

	@Override
	public void onDisable()
	{
		save();
		super.onDisable();
	}

	public void save()
	{
		saveConfig();
	}

	public void load()
	{
	}

	public void checkLocale()
	{
		locale = CrazyLocale.getPluginHead(this);
		locale.setAlternative(CrazyLocale.getLocaleHead().getLanguageEntry("CRAZYPLUGIN"));
	}

	@Override
	public final void sendLocaleMessage(final String localepath, final CommandSender target, final Object... args)
	{
		sendLocaleMessage(getLocale().getLanguageEntry(localepath), target, args);
	}

	@Override
	public final void sendLocaleRootMessage(final String localepath, final CommandSender target, final Object... args)
	{
		sendLocaleMessage(CrazyLocale.getLocaleHead().getLanguageEntry(localepath), target, args);
	}

	@Override
	public final void sendLocaleMessage(final CrazyLocale locale, final CommandSender target, final Object... args)
	{
		ChatHelper.sendMessage(target, getChatHeader(), locale, args);
	}

	@Override
	public final void sendLocaleMessage(final String localepath, final CommandSender[] targets, final Object... args)
	{
		sendLocaleMessage(getLocale().getLanguageEntry(localepath), targets, args);
	}

	@Override
	public final void sendLocaleRootMessage(final String localepath, final CommandSender[] targets, final Object... args)
	{
		sendLocaleMessage(CrazyLocale.getLocaleHead().getLanguageEntry(localepath), targets, args);
	}

	@Override
	public final void sendLocaleMessage(final CrazyLocale locale, final CommandSender[] targets, final Object... args)
	{
		ChatHelper.sendMessage(targets, getChatHeader(), locale, args);
	}

	@Override
	public final void sendLocaleMessage(final String localepath, final Collection<CommandSender> targets, final Object... args)
	{
		sendLocaleMessage(getLocale().getLanguageEntry(localepath), targets, args);
	}

	@Override
	public final void sendLocaleRootMessage(final String localepath, final Collection<CommandSender> targets, final Object... args)
	{
		sendLocaleMessage(CrazyLocale.getLocaleHead().getLanguageEntry(localepath), targets, args);
	}

	@Override
	public final void sendLocaleMessage(final CrazyLocale locale, final Collection<CommandSender> targets, final Object... args)
	{
		ChatHelper.sendMessage(targets, getChatHeader(), locale, args);
	}

	@Override
	public final <E> void sendListMessage(final CommandSender target, final String headLocale, final int amount, final int page, final List<? extends E> datas, final EntryDataGetter<E> getter)
	{
		ChatHelper.sendListMessage(target, this, headLocale, null, null, null, amount, page, datas, getter);
	}

	@Override
	public final <E> void sendListMessage(final CommandSender target, final String headLocale, final String seperator, final String entry, final String emptyPage, final int amount, final int page, final List<? extends E> datas, final EntryDataGetter<E> getter)
	{
		ChatHelper.sendListMessage(target, this, headLocale, seperator, entry, emptyPage, amount, page, datas, getter);
	}

	@Override
	public final <E> void sendListRootMessage(final CommandSender target, final String headLocale, final String seperator, final String entry, final String emptyPage, final int amount, final int page, final List<? extends E> datas, final EntryDataGetter<E> getter)
	{
		ChatHelper.sendListMessage(target, this.getChatHeader(), CrazyLocale.getLocaleHead().getLanguageEntry(headLocale), seperator == null ? null : CrazyLocale.getLocaleHead().getLanguageEntry(seperator), entry == null ? null : CrazyLocale.getLocaleHead().getLanguageEntry(entry), emptyPage == null ? null : CrazyLocale.getLocaleHead().getLanguageEntry(emptyPage), amount, page, datas, getter);
	}

	@Override
	public final <E> void sendListMessage(final CommandSender target, final CrazyLocale headLocale, final CrazyLocale seperator, final CrazyLocale entry, final CrazyLocale emptyPage, final int amount, final int page, final List<? extends E> datas, final EntryDataGetter<E> getter)
	{
		ChatHelper.sendListMessage(target, this.getChatHeader(), headLocale, seperator, entry, emptyPage, amount, page, datas, getter);
	}

	@Override
	public final void broadcastLocaleMessage(final String localepath, final Object... args)
	{
		broadcastLocaleMessage(getLocale().getLanguageEntry(localepath), args);
	}

	@Override
	public final void broadcastLocaleRootMessage(final String localepath, final Object... args)
	{
		broadcastLocaleMessage(CrazyLocale.getLocaleHead().getLanguageEntry(localepath), args);
	}

	@Override
	public final void broadcastLocaleMessage(final CrazyLocale locale, final Object... args)
	{
		sendLocaleMessage(locale, getServer().getConsoleSender(), args);
		sendLocaleMessage(locale, getServer().getOnlinePlayers(), args);
	}

	@Override
	public final void broadcastLocaleMessage(final boolean console, final String permission, final String localepath, final Object... args)
	{
		broadcastLocaleMessage(console, permission, getLocale().getLanguageEntry(localepath), args);
	}

	@Override
	public final void broadcastLocaleRootMessage(final boolean console, final String permission, final String localepath, final Object... args)
	{
		broadcastLocaleMessage(console, permission, CrazyLocale.getLocaleHead().getLanguageEntry(localepath), args);
	}

	@Override
	public final void broadcastLocaleMessage(final boolean console, final String permission, final CrazyLocale locale, final Object... args)
	{
		if (console)
			sendLocaleMessage(locale, Bukkit.getConsoleSender(), args);
		for (final Player player : Bukkit.getOnlinePlayers())
			if (permission != null)
				if (player.hasPermission(permission))
					sendLocaleMessage(locale, player, args);
	}

	@Override
	public final CrazyLogger getCrazyLogger()
	{
		return logger;
	}

	@Override
	public final CrazyLocale getLocale()
	{
		return locale;
	}

	protected boolean isSupportingLanguages()
	{
		return true;
	}

	public final void loadLanguage(final String language)
	{
		loadLanguage(language, Bukkit.getConsoleSender());
	}

	public void loadLanguageDelayed(final String language, final CommandSender sender)
	{
		getServer().getScheduler().scheduleAsyncDelayedTask(this, new LanguageLoadTask(this, language, sender));
	}

	public void loadLanguage(final String language, final CommandSender sender)
	{
		if (!isSupportingLanguages())
			return;
		// default files
		File file = new File(getDataFolder().getPath() + "/lang/" + language + ".lang");
		if (!file.exists())
		{
			downloadLanguage(language);
			if (!file.exists())
			{
				unpackLanguage(language);
				if (!file.exists())
				{
					sendLocaleMessage("LANGUAGE.ERROR.AVAILABLE", sender, language);
					return;
				}
			}
		}
		try
		{
			loadLanguageFile(language, file);
		}
		catch (final IOException e)
		{
			sendLocaleMessage("LANGUAGE.ERROR.READ", sender, language);
		}
		// Custom files:
		file = new File(getDataFolder().getPath() + "/lang/custom_" + language + ".lang");
		if (file.exists())
		{
			try
			{
				loadLanguageFile(language, file);
			}
			catch (final IOException e)
			{
				sendLocaleMessage("LANGUAGE.ERROR.READ", sender, language + " (Custom)");
			}
		}
	}

	public String getMainDownloadLocation()
	{
		return "https://raw.github.com/ST-DDT/Crazy/master/" + getDescription().getName() + "/src/resource";
	}

	public final void downloadLanguage(final String language)
	{
		downloadLanguage(language, Bukkit.getConsoleSender());
	}

	public void downloadLanguage(final String language, final CommandSender sender)
	{
		try
		{
			InputStream stream = null;
			BufferedInputStream in = null;
			FileOutputStream out = null;
			try
			{
				stream = new URL(getMainDownloadLocation() + "/lang/" + language + ".lang").openStream();
				if (stream == null)
					return;
				in = new BufferedInputStream(stream);
				out = new FileOutputStream(getDataFolder().getPath() + "/lang/" + language + ".lang");
				final byte data[] = new byte[1024];
				int count;
				while ((count = in.read(data, 0, 1024)) != -1)
					out.write(data, 0, count);
				out.flush();
			}
			finally
			{
				if (in != null)
					in.close();
				if (stream != null)
					stream.close();
				if (out != null)
					out.close();
			}
		}
		catch (final IOException e)
		{
			sendLocaleMessage("LANGUAGE.ERROR.DOWNLOAD", sender, language);
		}
	}

	public final void updateLanguage(final String language, final boolean reload)
	{
		updateLanguage(language, Bukkit.getConsoleSender(), reload);
	}

	public void updateLanguage(final String language, final CommandSender sender, final boolean reload)
	{
		if (!isSupportingLanguages())
			return;
		final File file = new File(getDataFolder().getPath() + "/lang/" + language + ".lang");
		downloadLanguage(language);
		if (!file.exists())
		{
			unpackLanguage(language);
			if (!file.exists())
			{
				sendLocaleMessage("LANGUAGE.ERROR.AVAILABLE", sender, language);
				return;
			}
		}
		if (reload)
			try
			{
				loadLanguageFile(language, file);
			}
			catch (final IOException e)
			{
				sendLocaleMessage("LANGUAGE.ERROR.READ", sender, language);
			}
	}

	public void unpackLanguage(final String language)
	{
		try
		{
			InputStream stream = null;
			InputStream in = null;
			OutputStream out = null;
			try
			{
				stream = getClass().getResourceAsStream("/resource/lang/" + language + ".lang");
				if (stream == null)
					return;
				in = new BufferedInputStream(stream);
				out = new BufferedOutputStream(new FileOutputStream(getDataFolder().getPath() + "/lang/" + language + ".lang"));
				final byte data[] = new byte[1024];
				int count;
				while ((count = in.read(data, 0, 1024)) != -1)
					out.write(data, 0, count);
				out.flush();
			}
			finally
			{
				if (out != null)
					out.close();
				if (stream != null)
					stream.close();
				if (in != null)
					in.close();
			}
		}
		catch (final IOException e)
		{
			sendLocaleMessage("LANGUAGE.ERROR.EXPORT", getServer().getConsoleSender(), language);
		}
	}

	public void loadLanguageFile(final String language, final File file) throws IOException
	{
		InputStream stream = null;
		InputStreamReader reader = null;
		try
		{
			stream = new FileInputStream(file);
			reader = new InputStreamReader(stream, "UTF-8");
			CrazyLocale.readFile(language, reader);
			// sendLocaleMessage("LANGUAGE.LOADED", sender, language);
		}
		finally
		{
			if (reader != null)
				reader.close();
			if (stream != null)
				stream.close();
		}
	}
}
