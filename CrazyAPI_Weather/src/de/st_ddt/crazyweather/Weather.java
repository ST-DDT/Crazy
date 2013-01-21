package de.st_ddt.crazyweather;

public enum Weather
{
	SUN(false, false), RAIN(true, false), THUNDER(false, true), THUNDERRAIN(true, true);

	private final boolean rain;
	private final boolean thunder;

	private Weather(final boolean rain, final boolean thunder)
	{
		this.rain = rain;
		this.thunder = thunder;
	}

	public boolean isRainEnabled()
	{
		return rain;
	}

	public boolean isThunderEnabled()
	{
		return thunder;
	}

	public static Weather getWeather(String weather)
	{
		if (weather == null)
			return null;
		weather = weather.toLowerCase();
		if (weather.equals("0") || weather.equals("sun") || weather.equals("sunny") || weather.equals("dry") || weather.equals("clear"))
			return SUN;
		if (weather.equals("1") || weather.equals("rain") || weather.equals("rainy") || weather.equals("wet") || weather.equals("snow"))
			return RAIN;
		if (weather.equals("2") || weather.equals("thunder") || weather.equals("lightning"))
			return THUNDER;
		if (weather.equals("3") || weather.equals("thunderrain") || weather.equals("lightningrain") || weather.equals("storm"))
			return THUNDERRAIN;
		return null;
	}

	public static String[] getWeatherNames()
	{
		final Weather[] weathers = values();
		final int length = weathers.length;
		final String[] res = new String[length];
		for (int i = 0; i < length; i++)
			res[i] = weathers[i].toString();
		return res;
	}
}
