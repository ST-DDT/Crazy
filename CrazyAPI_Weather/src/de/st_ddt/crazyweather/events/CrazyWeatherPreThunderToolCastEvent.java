package de.st_ddt.crazyweather.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import de.st_ddt.crazyweather.WeatherPlugin;

public class CrazyWeatherPreThunderToolCastEvent extends CrazyWeatherEvent implements Cancellable
{

	final protected Player player;
	final protected Location location;
	protected boolean cancelled;

	public CrazyWeatherPreThunderToolCastEvent(final WeatherPlugin plugin, final Player player, final Location location)
	{
		super(plugin);
		this.player = player;
		this.location = location;
	}

	public Player getPlayer()
	{
		return player;
	}

	public Location getLocation()
	{
		return location;
	}

	@Override
	public boolean isCancelled()
	{
		return cancelled;
	}

	@Override
	public void setCancelled(final boolean cancel)
	{
		this.cancelled = cancel;
	}
}
