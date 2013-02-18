package de.st_ddt.crazyweather.data;

import org.bukkit.World;

import de.st_ddt.crazyweather.Weather;

public interface WeatherData
{

	public abstract String getWorldName();

	public abstract World getWorld();

	public abstract boolean isStaticWeatherEnabled();

	public abstract boolean isOnLoadEnabled();

	public abstract void setWeather(final Weather weather, final boolean keepStatic, final boolean keepLoad, int duration);
}
