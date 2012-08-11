package de.st_ddt.crazyweather;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class CrazyWeatherWeatherListener implements Listener
{

	protected final CrazyWeather plugin;

	public CrazyWeatherWeatherListener(final CrazyWeather plugin)
	{
		super();
		this.plugin = plugin;
	}

	public void WorldLoad(final WorldLoadEvent event)
	{
		plugin.loadWorld(event.getWorld());
	}

	@EventHandler(ignoreCancelled = true)
	public void WeatherChange(final WeatherChangeEvent event)
	{
		final WorldWeather weather = CrazyWeather.getPlugin().getWorldWeather(event.getWorld());
		if (weather != null)
			if (weather.isStaticWeatherEnabled())
				event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void ThunderChange(final ThunderChangeEvent event)
	{
		final WorldWeather weather = CrazyWeather.getPlugin().getWorldWeather(event.getWorld());
		if (weather != null)
			if (weather.isStaticWeatherEnabled())
				event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void LightningStrike(final LightningStrikeEvent event)
	{
		if (plugin.isLightningDisabled())
		{
			event.setCancelled(true);
			return;
		}
		if (plugin.isLightningDamageDisabled())
		{
			event.setCancelled(true);
			event.getWorld().strikeLightningEffect(event.getLightning().getLocation());
			return;
		}
	}
}
