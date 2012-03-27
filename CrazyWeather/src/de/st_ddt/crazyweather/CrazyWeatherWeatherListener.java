package de.st_ddt.crazyweather;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class CrazyWeatherWeatherListener implements Listener
{

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event)
	{
		WorldWeather weather = CrazyWeather.getPlugin().getWorldWeather(event.getWorld());
		if (weather != null)
			if (weather.isStatic())
				event.setCancelled(true);
	}

	@EventHandler
	public void onThunderChange(ThunderChangeEvent event)
	{
		WorldWeather weather = CrazyWeather.getPlugin().getWorldWeather(event.getWorld());
		if (weather != null)
			if (weather.isStatic())
				event.setCancelled(true);
	}

	@EventHandler
	public void onLightningStrike(LightningStrikeEvent event)
	{
		if (CrazyWeather.getPlugin() == null)
			return;
		if (CrazyWeather.getPlugin().isLightningdisabled())
		{
			event.setCancelled(true);
			return;
		}
		if (CrazyWeather.getPlugin().isLightningdamagedisabled())
		{
			event.setCancelled(true);
			event.getWorld().strikeLightningEffect(event.getLightning().getLocation());
			return;
		}
	}
}
