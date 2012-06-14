package de.st_ddt.crazyweather;

import org.bukkit.World;

public interface WeatherData
{

	public abstract String getWorldName();

	public abstract World getWorld();

	public abstract boolean isStaticWeatherEnabled();

	public abstract boolean isOnLoadEnabled();

	public abstract void setWeather(final Weather weather, final boolean keepStatic, final boolean keepLoad, int duration);
}
