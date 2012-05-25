package de.st_ddt.crazyplugin;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CrazyLightPlugin extends JavaPlugin
{

	private String chatHeader = null;
	private static final HashMap<Class<? extends CrazyLightPlugin>, CrazyLightPlugin> plugins = new HashMap<Class<? extends CrazyLightPlugin>, CrazyLightPlugin>();

	public final String getChatHeader()
	{
		if (chatHeader == null)
			chatHeader = ChatColor.RED + "[" + ChatColor.GREEN + getDescription().getName() + ChatColor.RED + "] " + ChatColor.WHITE;
		return chatHeader;
	}

	public static Collection<CrazyLightPlugin> getCrazyLightPlugins()
	{
		return plugins.values();
	}

	public final static CrazyLightPlugin getLightPlugin(final Class<? extends CrazyLightPlugin> plugin)
	{
		return plugins.get(plugin);
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
		plugins.put(this.getClass(), this);
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
}
