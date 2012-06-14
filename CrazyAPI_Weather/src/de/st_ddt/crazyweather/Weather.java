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

	public static Weather getWeather(final String weather)
	{
		if (weather == null)
			return null;
		if (weather.equals("0") || weather.equalsIgnoreCase("sun") || weather.equalsIgnoreCase("sunny") || weather.equalsIgnoreCase("dry") || weather.equalsIgnoreCase("clear"))
			return SUN;
		if (weather.equals("1") || weather.equalsIgnoreCase("rain") || weather.equalsIgnoreCase("rainy") || weather.equalsIgnoreCase("wet") || weather.equalsIgnoreCase("snow") || weather.equalsIgnoreCase("storm"))
			return RAIN;
		if (weather.equals("2") || weather.equalsIgnoreCase("thunder") || weather.equalsIgnoreCase("lightning"))
			return THUNDER;
		if (weather.equals("3") || weather.equalsIgnoreCase("thunderrain") || weather.equalsIgnoreCase("lightningrain"))
			return THUNDER;
		return null;
	}
}
