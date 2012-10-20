package de.st_ddt.crazyutil.paramitrisable;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable.TypedParamitrisable;
import de.st_ddt.crazyweather.Weather;

public class WeatherParamitrisable extends TypedParamitrisable<Weather>
{

	public WeatherParamitrisable(final Weather defaultValue)
	{
		super(defaultValue);
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		value = Weather.getWeather(parameter);
		if (value == null)
			throw new CrazyCommandNoSuchException("Weather", parameter, Weather.getWeatherNames());
	}
}
