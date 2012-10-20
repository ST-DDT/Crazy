package de.st_ddt.crazyweather.commands;

import de.st_ddt.crazyplugin.commands.CrazyCommandExecutor;
import de.st_ddt.crazyweather.CrazyWeather;

public abstract class CrazyWeatherCommandExecutor extends CrazyCommandExecutor<CrazyWeather>
{

	public CrazyWeatherCommandExecutor(final CrazyWeather plugin)
	{
		super(plugin);
	}
}
