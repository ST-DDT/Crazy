package de.st_ddt.crazyplugin.events;

import org.bukkit.event.Event;

import de.st_ddt.crazyplugin.CrazyPluginInterface;

public abstract class CrazyEvent<T extends CrazyPluginInterface> extends Event
{

	protected final T plugin;

	public CrazyEvent(final T plugin)
	{
		super();
		this.plugin = plugin;
	}

	public T getPlugin()
	{
		return plugin;
	}
}
