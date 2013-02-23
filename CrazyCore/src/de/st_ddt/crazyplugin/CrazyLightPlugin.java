package de.st_ddt.crazyplugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.modes.Mode;
import de.st_ddt.crazyutil.source.Localized;

public abstract class CrazyLightPlugin extends JavaPlugin implements CrazyLightPluginInterface
{

	private static final Map<Class<? extends CrazyLightPlugin>, CrazyLightPlugin> lightplugins = new LinkedHashMap<Class<? extends CrazyLightPlugin>, CrazyLightPlugin>();
	private String chatHeader = null;

	@Override
	public final String getChatHeader()
	{
		if (chatHeader == null)
			chatHeader = getDefaultChatHeader();
		return chatHeader;
	}

	protected String getDefaultChatHeader()
	{
		return ChatColor.RED + "[" + ChatColor.GREEN + getName() + ChatColor.RED + "] " + ChatColor.WHITE;
	}

	public final static Collection<CrazyLightPlugin> getCrazyLightPlugins()
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
		final ConfigurationSection config = getConfig();
		chatHeader = ChatHelper.colorise(config.getString("chatHeader", getDefaultChatHeader()));
		config.set("chatHeader", ChatHelper.decolorise(chatHeader));
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

	public boolean showMetrics()
	{
		return true;
	}

	public final void consoleLog(final String message)
	{
		getServer().getConsoleSender().sendMessage(getChatHeader() + message);
	}

	@Override
	public String getParameter(final CommandSender sender, final int index)
	{
		switch (index)
		{
			case 0:
				return getName();
			case 1:
				return getChatHeader();
			case 2:
				return getVersion();
			default:
				return "";
		}
	}

	@Override
	public int getParameterCount()
	{
		return 3;
	}

	@Override
	public final String getVersion()
	{
		return getDescription().getVersion();
	}

	public String getBukkitURL()
	{
		return "http://dev.bukkit.org/server-mods/" + getName().toLowerCase() + "/";
	}

	@Override
	public void show(final CommandSender target)
	{
		show(target, getChatHeader(), false);
	}

	@Override
	@Localized({ "CRAZYPLUGIN.PLUGININFO.HEAD", "CRAZYPLUGIN.PLUGININFO.NAME", "CRAZYPLUGIN.PLUGININFO.DESCRIPTION", "CRAZYPLUGIN.PLUGININFO.VERSION", "CRAZYPLUGIN.PLUGININFO.AUTHORS", "CRAZYPLUGIN.PLUGININFO.DEPENCIES", "CRAZYPLUGIN.PLUGININFO.URL" })
	public void show(final CommandSender target, final String chatHeader, final boolean showDetailed)
	{
		final CrazyLocale locale = CrazyLocale.getLocaleHead().getSecureLanguageEntry("CRAZYPLUGIN.PLUGININFO");
		ChatHelper.sendMessage(target, chatHeader, locale.getLanguageEntry("HEAD"), DATETIMEFORMAT.format(new Date()));
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
		return getName() + " (v" + getDescription().getVersion() + ")";
	}

	@Override
	public int compareTo(final CrazyLightPluginInterface o)
	{
		return getName().compareTo(o.getName());
	}

	final Mode<String> getChatHeaderMode()
	{
		if (this instanceof CrazyPluginInterface)
			return new Mode<String>((CrazyPluginInterface) this, "chatHeader", String.class)
			{

				@Override
				public String getValue()
				{
					return chatHeader;
				}

				@Override
				public void setValue(final CommandSender sender, final String... args) throws CrazyException
				{
					setValue(ChatHelper.colorise(ChatHelper.listingString(" ", args)));
					showValue(sender);
				}

				@Override
				public void setValue(final String newValue) throws CrazyException
				{
					chatHeader = newValue;
					getConfig().set("chatHeader", ChatHelper.decolorise(chatHeader));
					saveConfig();
				}

				@Override
				public List<String> tab(final String... args)
				{
					if (args.length != 1 && args[0].length() != 0)
						return null;
					final List<String> res = new ArrayList<String>(1);
					res.add(ChatHelper.decolorise(chatHeader));
					return res;
				}
			};
		else
			return null;
	}
}
