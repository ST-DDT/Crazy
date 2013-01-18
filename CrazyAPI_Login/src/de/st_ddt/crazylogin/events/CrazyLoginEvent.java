package de.st_ddt.crazylogin.events;

import org.bukkit.event.HandlerList;

import de.st_ddt.crazyplugin.events.CrazyEvent;

public class CrazyLoginEvent extends CrazyEvent
{

	private static final HandlerList handlers = new HandlerList();

	public CrazyLoginEvent()
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
