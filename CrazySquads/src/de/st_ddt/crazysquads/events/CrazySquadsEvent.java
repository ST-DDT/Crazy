package de.st_ddt.crazysquads.events;

import org.bukkit.event.HandlerList;

import de.st_ddt.crazyplugin.events.CrazyEvent;

public class CrazySquadsEvent extends CrazyEvent
{

	private static final HandlerList handlers = new HandlerList();

	public CrazySquadsEvent()
	{
		super();
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
