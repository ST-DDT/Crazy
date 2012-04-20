package de.st_ddt.crazyweather;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class WorldWeather
{

	private String staticWeather = "none";
	private boolean onLoad = false;
	private final World world;

	public boolean equals(World world)
	{
		return this.world == world;
	}

	public String getName()
	{
		return world.getName();
	}

	public boolean isStatic()
	{
		return !staticWeather.equalsIgnoreCase("none");
	}

	public boolean getOnLoad()
	{
		return onLoad;
	}

	public void load(ConfigurationSection config)
	{
		if (config == null)
			return;
		staticWeather = config.getString("static", "none");
		onLoad = config.getBoolean("onLoad", false);
		if (onLoad)
		{
			setWeather(staticWeather, true, true);
		}
	}

	public void save(ConfigurationSection config, String worldData)
	{
		config.set(worldData + ".static", staticWeather);
		config.set(worldData + ".onLoad", onLoad);
	}

	public WorldWeather(World world)
	{
		this.world = world;
	}

	public void setWeather(String weather, boolean keepStatic, boolean keepLoad)
	{
		staticWeather = "none";
		onLoad = keepLoad;
		int duration = (int) (20 * 60 * Math.round(10 + Math.random() * 20));
		if (weather.equals("sun"))
		{
			world.setThundering(false);
			world.setStorm(false);
			if (CrazyWeather.getPlugin().getLocale() != null)
				CrazyWeather.getPlugin().sendLocaleMessage("WEATHER.SUN", Bukkit.getConsoleSender(), world.getName());
		}
		else if (weather.equals("rain"))
		{
			world.setThundering(false);
			world.setStorm(true);
			if (CrazyWeather.getPlugin().getLocale() != null)
				CrazyWeather.getPlugin().sendLocaleMessage("WEATHER.SUN", Bukkit.getConsoleSender(), world.getName());
		}
		else if (weather.equals("thunder"))
		{
			world.setThundering(true);
			world.setStorm(true);
			world.setThunderDuration(duration);
			if (CrazyWeather.getPlugin().getLocale() != null)
				CrazyWeather.getPlugin().sendLocaleMessage("WEATHER.SUN", Bukkit.getConsoleSender(), world.getName());
		}
		world.setWeatherDuration(duration);
		if (keepStatic)
			staticWeather = weather;
		else
			staticWeather = "none";
	}
}
