package de.st_ddt.crazyplugin;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public abstract class CrazyLightPlugin extends JavaPlugin implements CrazyLightPluginInterface
{

	private static final HashMap<Class<? extends CrazyLightPlugin>, CrazyLightPlugin> lightplugins = new HashMap<Class<? extends CrazyLightPlugin>, CrazyLightPlugin>();
	private String chatHeader = null;

	@Override
	public final String getChatHeader()
	{
		if (chatHeader == null)
			chatHeader = ChatColor.RED + "[" + ChatColor.GREEN + getDescription().getName() + ChatColor.RED + "] " + ChatColor.WHITE;
		return chatHeader;
	}

	public static Collection<CrazyLightPlugin> getCrazyLightPlugins()
	{
		return lightplugins.values();
	}

	public final static CrazyLightPlugin getLightPlugin(final Class<? extends CrazyLightPlugin> plugin)
	{
		return lightplugins.get(plugin);
	}

	public final static CrazyLightPlugin getLightPlugin(final String name)
	{
		for (final CrazyLightPlugin plugin : getCrazyLightPlugins())
			if (plugin.getName().equalsIgnoreCase(name))
				return plugin;
		return null;
	}

	@Override
	public void onLoad()
	{
		lightplugins.put(this.getClass(), this);
		super.onLoad();
	}

	@Override
	public void onEnable()
	{
		consoleLog("Version " + getDescription().getVersion() + " enabled");
	}

	@Override
	public void onDisable()
	{
		consoleLog("disabled");
	}

	public final void consoleLog(final String message)
	{
		getServer().getConsoleSender().sendMessage(getChatHeader() + message);
	}

	@Override
	public String getParameter(int index)
	{
		switch (index)
		{
			case 0:
				return getName();
			case 1:
				return getChatHeader();
			case 2:
				return getDescription().getVersion();
			default:
				return "";
		}
	}

	@Override
	public int getParameterCount()
	{
		return 3;
	}

	public String getVersion()
	{
		return getDescription().getVersion();
	}

	public String getBukkitURL()
	{
		return "http://dev.bukkit.org/server-mods/" + getName().toLowerCase() + "/";
	}

	@Override
	public void show(CommandSender target)
	{
		show(target, getChatHeader(), false);
	}

	@Override
	public void show(CommandSender target, String chatHeader, boolean showDetailed)
	{
		final CrazyLocale locale = CrazyLocale.getLocaleHead().getSecureLanguageEntry("CRAZYPLUGIN.PLUGININFO");
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("HEAD"), CrazyPluginInterface.DateFormat.format(new Date()));
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("NAME"), getName());
		if (showDetailed)
			if (getDescription().getDescription() != null)
				ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("DESCRIPTION"), getDescription().getDescription());
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("VERSION"), getVersion());
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("AUTHORS"), ChatHelper.listingString(getDescription().getAuthors()));
		if (showDetailed)
		{
			if (getDescription().getDepend() != null)
				ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("DEPENCIES"), ChatHelper.listingString(getDescription().getDepend()));
			ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("URL"), getBukkitURL());
		}
	}

	@Override
	public String getShortInfo()
	{
		return getName() + " Version " + getDescription().getVersion();
	}
}
