package de.st_ddt.crazyplugin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
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
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutor;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainTree;
import de.st_ddt.crazyplugin.tasks.LanguageLoadTask;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.CrazyLogger;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.VersionComparator;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.LocalizedVariable;

@LocalizedVariable(variables = "CRAZYPLUGIN", values = "CRAZYPLUGIN")
public abstract class CrazyPlugin extends CrazyLightPlugin implements CrazyPluginInterface
{

	private static final LinkedHashMap<Class<? extends CrazyPlugin>, CrazyPlugin> plugins = new LinkedHashMap<Class<? extends CrazyPlugin>, CrazyPlugin>();
	protected final CrazyLogger logger = new CrazyLogger(this);
	protected final CrazyPluginCommandMainTree mainCommand = new CrazyPluginCommandMainTree(this);
	protected final CrazyPluginCommandMainMode modeCommand = new CrazyPluginCommandMainMode(this);
	protected CrazyLocale locale = null;
	protected String previousVersion = "0";
	protected String updateVersion = "0";
	protected boolean isUpdated = false;
	protected boolean isInstalled = false;
	protected String updateURL = null;

	public final static Collection<CrazyPlugin> getCrazyPlugins()
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

	@Override
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
	public final CrazyCommandTreeExecutor<CrazyPluginInterface> getMainCommand()
	{
		return mainCommand;
	}

	private void registerCommands()
	{
		final PluginCommand command = getCommand(getName());
		if (command != null)
			command.setExecutor(mainCommand);
		mainCommand.addSubCommand(modeCommand, "mode");
		modeCommand.addMode(getChatHeaderMode());
	}

	@Override
	public void onLoad()
	{
		plugins.put(this.getClass(), this);
		getDataFolder().mkdir();
		new File(getDataFolder().getPath() + "/lang").mkdirs();
		checkLocale();
		final ConfigurationSection config = getConfig();
		previousVersion = config.getString("version", "0");
		isInstalled = previousVersion.equals("0");
		isUpdated = !previousVersion.equals(getDescription().getVersion());
		config.set("version", getDescription().getVersion());
		super.onLoad();
	}

	@Override
	@Localized("CRAZYPLUGIN.UPDATED $Name$ $Version$")
	public void onEnable()
	{
		if (isUpdated)
			broadcastLocaleMessage("UPDATED", getName(), getDescription().getVersion());
		load();
		if (isUpdated)
			save();
		super.onEnable();
		registerCommands();
	}

	@Override
	public void onDisable()
	{
		save();
		super.onDisable();
	}

	@Override
	public void load()
	{
		loadConfiguration();
	}

	@Override
	public void loadConfiguration()
	{
	}

	@Override
	public void save()
	{
		saveConfiguration();
	}

	@Override
	public void saveConfiguration()
	{
		logger.save(getConfig(), "logs.");
		saveConfig();
	}

	@SuppressWarnings("deprecation")
	@Override
	@Localized("CRAZYPLUGIN.PLUGININFO.UPDATE $NewVersion$")
	public void show(final CommandSender target, final String chatHeader, final boolean showDetailed)
	{
		super.show(target, chatHeader, showDetailed);
		final CrazyLocale locale = CrazyLocale.getLocaleHead().getSecureLanguageEntry("CRAZYPLUGIN.PLUGININFO");
		Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new Runnable()
		{

			@Override
			public void run()
			{
				if (checkForUpdate(false))
					ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("UPDATE"), updateVersion);
			}
		});
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
	public final void sendLocaleMessage(final CrazyLocale locale, final CommandSender[] targets, final Object... args)
	{
		ChatHelper.sendMessage(targets, getChatHeader(), locale, args);
	}

	@Override
	public final void sendLocaleMessage(final String localepath, final Collection<? extends CommandSender> targets, final Object... args)
	{
		sendLocaleMessage(getLocale().getLanguageEntry(localepath), targets, args);
	}

	@Override
	public final void sendLocaleMessage(final CrazyLocale locale, final Collection<? extends CommandSender> targets, final Object... args)
	{
		ChatHelper.sendMessage(targets, getChatHeader(), locale, args);
	}

	@Override
	public void sendLocaleList(final CommandSender target, final ListFormat format, final int amount, final int page, final List<?> datas)
	{
		ChatHelperExtended.sendList(target, getChatHeader(), format, amount, page, datas);
	}

	@Override
	public final void sendLocaleList(final CommandSender target, final String formatPath, final int amount, final int page, final List<?> datas)
	{
		sendLocaleList(target, formatPath + ".HEADER", formatPath + ".LISTFORMAT", formatPath + ".ENTRYFORMAT", amount, page, datas);
	}

	@Override
	public final void sendLocaleList(final CommandSender target, final String headFormatPath, final String listFormatPath, final String entryFormatPath, final int amount, final int page, final List<?> datas)
	{
		CrazyLocale headFormat = null;
		if (headFormatPath != null)
			headFormat = getLocale().getLanguageEntry(headFormatPath);
		CrazyLocale listFormat = null;
		if (listFormatPath != null)
			listFormat = getLocale().getLanguageEntry(listFormatPath);
		CrazyLocale entryFormat = null;
		if (entryFormatPath != null)
			entryFormat = getLocale().getLanguageEntry(entryFormatPath);
		sendLocaleList(target, headFormat, listFormat, entryFormat, amount, page, datas);
	}

	@Override
	@Localized({ "CRAZYPLUGIN.LIST.HEADER $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$", "CRAZYPLUGIN.LIST.LISTFORMAT $Index$ $Entry$ $ChatHeader$", "CRAZYPLUGIN.LIST.ENTRYFORMAT $Name$ ..." })
	public final void sendLocaleList(final CommandSender target, CrazyLocale headFormat, CrazyLocale listFormat, CrazyLocale entryFormat, final int amount, final int page, final List<?> datas)
	{
		if (headFormat == null)
			headFormat = getLocale().getLanguageEntry("LIST.HEADER");
		if (listFormat == null)
			listFormat = getLocale().getLanguageEntry("LIST.LISTFORMAT");
		if (entryFormat == null)
			entryFormat = getLocale().getLanguageEntry("LIST.ENTRYFORMAT");
		ChatHelperExtended.sendList(target, getChatHeader(), headFormat.getLanguageText(target), listFormat.getLanguageText(target), entryFormat.getLanguageText(target), amount, page, datas);
	}

	@Override
	public final void broadcastLocaleMessage(final String localepath, final Object... args)
	{
		broadcastLocaleMessage(getLocale().getLanguageEntry(localepath), args);
	}

	@Override
	public final void broadcastLocaleMessage(final CrazyLocale locale, final Object... args)
	{
		sendLocaleMessage(locale, Bukkit.getConsoleSender(), args);
		sendLocaleMessage(locale, Bukkit.getOnlinePlayers(), args);
	}

	@Override
	public final void broadcastLocaleMessage(final boolean console, final String permission, final String localepath, final Object... args)
	{
		broadcastLocaleMessage(console, permission, getLocale().getLanguageEntry(localepath), args);
	}

	@Override
	public final void broadcastLocaleMessage(final boolean console, final String permission, final CrazyLocale locale, final Object... args)
	{
		if (permission == null)
			broadcastLocaleMessage(console, new String[] {}, locale, args);
		else
			broadcastLocaleMessage(console, new String[] { permission }, locale, args);
	}

	@Override
	public final void broadcastLocaleMessage(final boolean console, final String[] permissions, final String localepath, final Object... args)
	{
		broadcastLocaleMessage(console, permissions, getLocale().getLanguageEntry(localepath), args);
	}

	@Override
	public final void broadcastLocaleMessage(final boolean console, final String[] permissions, final CrazyLocale locale, final Object... args)
	{
		if (console)
			sendLocaleMessage(locale, Bukkit.getConsoleSender(), args);
		Player: for (final Player player : Bukkit.getOnlinePlayers())
		{
			for (final String permission : permissions)
				if (!PermissionModule.hasPermission(player, permission))
					continue Player;
			sendLocaleMessage(locale, player, args);
		}
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

	@SuppressWarnings("deprecation")
	public void loadLanguageDelayed(final String language, final CommandSender sender)
	{
		getServer().getScheduler().scheduleAsyncDelayedTask(this, new LanguageLoadTask(this, language, sender));
	}

	@Localized({ "CRAZYPLUGIN.LANGUAGE.ERROR.AVAILABLE $Language$ CRAZYPLUGIN.LANGUAGE", "CRAZYPLUGIN.LANGUAGE.ERROR.READ $Language$ $Plugin$" })
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
					sendLocaleMessage("LANGUAGE.ERROR.AVAILABLE", sender, language, getName());
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
			sendLocaleMessage("LANGUAGE.ERROR.READ", sender, language, getName());
		}
		// Custom files:
		file = new File(getDataFolder().getPath() + "/lang/custom_" + language + ".lang");
		if (file.exists())
			try
			{
				loadLanguageFile(language, file);
			}
			catch (final IOException e)
			{
				sendLocaleMessage("LANGUAGE.ERROR.READ", sender, language + " (Custom)", getName());
			}
	}

	public String getMainDownloadLocation()
	{
		return "https://raw.github.com/ST-DDT/Crazy/master/" + getName() + "/src/resource";
	}

	public final void downloadLanguage(final String language)
	{
		downloadLanguage(language, Bukkit.getConsoleSender());
	}

	@Localized("CRAZYPLUGIN.LANGUAGE.ERROR.DOWNLOAD $Language$ $Plugin$")
	public void downloadLanguage(final String language, final CommandSender sender)
	{
		try
		{
			BufferedInputStream in = null;
			FileOutputStream out = null;
			try
			{
				final InputStream stream = new URL(getMainDownloadLocation() + "/lang/" + language + ".lang").openStream();
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
				if (out != null)
					out.close();
			}
		}
		catch (final IOException e)
		{
			sendLocaleMessage("LANGUAGE.ERROR.DOWNLOAD", sender, language, getName());
		}
	}

	public final void updateLanguage(final String language, final boolean reload)
	{
		updateLanguage(language, Bukkit.getConsoleSender(), reload);
	}

	@Localized({ "CRAZYPLUGIN.LANGUAGE.ERROR.AVAILABLE $Language$ $Plugin$", "CRAZYPLUGIN.LANGUAGE.ERROR.READ $Language$ $Plugin$" })
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
				sendLocaleMessage("LANGUAGE.ERROR.AVAILABLE", sender, language, getName());
				return;
			}
		}
		if (reload)
		{
			try
			{
				loadLanguageFile(language, file);
			}
			catch (final IOException e)
			{
				sendLocaleMessage("LANGUAGE.ERROR.READ", sender, language, getName());
			}
			final File customFile = new File(getDataFolder().getPath() + "/lang/custom_" + language + ".lang");
			if (customFile.exists())
				try
				{
					loadLanguageFile(language, customFile);
				}
				catch (final IOException e)
				{
					sendLocaleMessage("LANGUAGE.ERROR.READ", sender, language + " (Custom)", getName());
				}
		}
	}

	public final void unpackLanguage(final String language)
	{
		unpackLanguage(language, Bukkit.getConsoleSender());
	}

	@Localized("CRAZYPLUGIN.LANGUAGE.ERROR.EXTRACT $Language$ $Plugin$")
	public void unpackLanguage(final String language, final CommandSender sender)
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
			sendLocaleMessage("LANGUAGE.ERROR.EXTRACT", sender, language, getName());
		}
	}

	public final void loadLanguageFile(final String language, final File file) throws IOException
	{
		InputStream stream = null;
		InputStreamReader reader = null;
		try
		{
			stream = new FileInputStream(file);
			reader = new InputStreamReader(stream, "UTF-8");
			CrazyLocale.readFile(language, reader);
		}
		finally
		{
			if (reader != null)
				reader.close();
			if (stream != null)
				stream.close();
		}
	}

	@Override
	public final String getUpdateVersion()
	{
		return updateVersion;
	}

	public final String getUpdateURL()
	{
		return updateURL;
	}

	@Override
	public boolean checkForUpdate(final boolean force)
	{
		if (!force)
		{
			if (updateVersion == null)
				return false;
			if (!updateVersion.equals("0"))
			{
				final int value = VersionComparator.compareVersions(this, updateVersion);
				if (value == 1)
					return false;
				else if (value == -1)
					return true;
			}
		}
		try
		{
			BufferedReader bufreader = null;
			try
			{
				final InputStream stream = new URL("http://dev.bukkit.org/server-mods/" + getName().toLowerCase() + "/files.rss").openStream();
				if (stream == null)
				{
					updateVersion = null;
					return false;
				}
				bufreader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
				String zeile = null;
				boolean active = false;
				while ((zeile = bufreader.readLine()) != null)
				{
					zeile = zeile.trim();
					if (active)
						if (zeile.startsWith("<title>"))
							updateVersion = zeile.substring(7 + getName().length() + 2).split("<")[0];
						else if (zeile.startsWith("<link>"))
							updateURL = zeile.substring(6).split("<")[0];
						else if (zeile.equals("<item>"))
							break;
						else
							continue;
					else if (zeile.equals("<item>"))
						active = true;
					else
						continue;
					if (updateVersion != null && updateURL != null)
						break;
				}
			}
			finally
			{
				if (bufreader != null)
					bufreader.close();
			}
		}
		catch (final Exception e)
		{
			updateVersion = null;
			return false;
		}
		return VersionComparator.compareVersions(this, updateVersion) == -1;
	}
}
