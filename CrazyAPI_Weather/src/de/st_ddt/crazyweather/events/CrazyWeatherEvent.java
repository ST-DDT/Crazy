package de.st_ddt.crazyweather.events;

import org.bukkit.event.HandlerList;

import de.st_ddt.crazyplugin.events.CrazyEvent;

public class CrazyWeatherEvent extends CrazyEvent
{

	private static final HandlerList handlers = new HandlerList();

	public CrazyWeatherEvent()
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
