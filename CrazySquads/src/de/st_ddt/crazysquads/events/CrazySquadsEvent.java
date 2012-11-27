package de.st_ddt.crazysquads.events;

import org.bukkit.event.HandlerList;

import de.st_ddt.crazyplugin.events.CrazyEvent;
import de.st_ddt.crazysquads.CrazySquads;

public class CrazySquadsEvent extends CrazyEvent<CrazySquads>
{

	private static final HandlerList handlers = new HandlerList();

	public CrazySquadsEvent(final CrazySquads plugin)
	{
		super(plugin);
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
