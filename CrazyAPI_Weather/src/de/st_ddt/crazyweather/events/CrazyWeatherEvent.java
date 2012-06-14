package de.st_ddt.crazyweather.events;

import org.bukkit.event.HandlerList;

import de.st_ddt.crazyplugin.events.CrazyEvent;
import de.st_ddt.crazyweather.WeatherPlugin;

public class CrazyWeatherEvent extends CrazyEvent<WeatherPlugin>
{

	private static final HandlerList handlers = new HandlerList();

	public CrazyWeatherEvent(final WeatherPlugin plugin)
	{
		super(plugin);
		// EDIT Auto-generated constructor stub
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
