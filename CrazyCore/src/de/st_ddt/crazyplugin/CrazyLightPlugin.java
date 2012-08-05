package de.st_ddt.crazyplugin;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

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

	@Override
	public void show(CommandSender target)
	{
		target.sendMessage(getShortInfo(new String[0]));
	}

	@Override
	public void show(CommandSender target, String... args)
	{
		target.sendMessage(getShortInfo(args));
	}

	@Override
	public String getShortInfo(String... args)
	{
		return getName() + " Version " + getDescription().getVersion();
	}
}
