package de.st_ddt.crazyweather;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ConfigurationSaveable;

public class WorldWeather implements ConfigurationSaveable, WeatherData
{

	private final CrazyWeather plugin = CrazyWeather.getPlugin();
	private Weather staticWeather = null;
	private boolean onLoad = false;
	private final World world;

	public WorldWeather(final World world)
	{
		this.world = world;
	}

	public void load(final ConfigurationSection config)
	{
		if (config == null)
			return;
		staticWeather = Weather.getWeather(config.getString("static"));
		onLoad = config.getBoolean("onLoad", false);
	}

	@Override
	public String getWorldName()
	{
		return world.getName();
	}

	@Override
	public World getWorld()
	{
		return world;
	}

	public Weather getStaticWeather()
	{
		return staticWeather;
	}

	@Override
	public boolean isStaticWeatherEnabled()
	{
		return staticWeather != null;
	}

	@Override
	public boolean isOnLoadEnabled()
	{
		return onLoad;
	}

	@Override
	public void setWeather(final Weather weather, final boolean keepStatic, final boolean keepLoad, int duration)
	{
		staticWeather = null;
		onLoad = keepLoad;
		if (weather == null)
			return;
		duration *= 20;
		if (keepStatic)
			duration = Integer.MAX_VALUE;
		world.setStorm(weather.isRainEnabled());
		world.setThundering(weather.isThunderEnabled());
		world.setWeatherDuration(duration);
		if (weather.isThunderEnabled())
			world.setThunderDuration(duration);
		plugin.sendLocaleMessage("WEATHER." + weather.toString(), Bukkit.getConsoleSender(), world.getName());
		if (keepStatic)
			staticWeather = weather;
		else
			staticWeather = null;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		if (staticWeather == null)
			return;
		config.set(path + "static", staticWeather.toString());
		config.set(path + "onLoad", onLoad);
	}
}
