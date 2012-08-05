package de.st_ddt.crazyplugin.events;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;

class CrazyPlayerInfoEvent extends CrazyEvent<CrazyLightPluginInterface>
{

	private static final HandlerList handlers = new HandlerList();
	protected final String name;
	protected final HashMap<JavaPlugin, String[]> data = new HashMap<JavaPlugin, String[]>();

	public CrazyPlayerInfoEvent(CrazyLightPluginInterface plugin, String name)
	{
		super(plugin);
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void addPlayerData(JavaPlugin plugin, String[] datas)
	{
		data.put(plugin, datas);
	}

	public Set<JavaPlugin> getPlugins()
	{
		return data.keySet();
	}

	public HashMap<JavaPlugin, String[]> getData()
	{
		return data;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
