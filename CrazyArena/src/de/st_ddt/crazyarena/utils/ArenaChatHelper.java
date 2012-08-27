package de.st_ddt.crazyarena.utils;

public class ArenaChatHelper
{

	private ArenaChatHelper()
	{
		super();
	}

	public static String timeConverter(long timeTotal)
	{
		final StringBuilder res = new StringBuilder();
		long time = timeTotal;
		if (timeTotal > 86400000)
		{
			final long unit = time / 86400;
			time %= 86400000;
			res.append(unit + " days ");
		}
		if (timeTotal > 3600000)
		{
			final long unit = time / 3600000;
			time %= 3600000;
			res.append(unit + ":");
		}
		if (timeTotal > 60000)
		{
			final long unit = time / 60000;
			time %= 60000;
			res.append(unit + ":");
		}
		final long unit = time / 1000;
		time %= 1000;
		res.append(unit + "." + time);
		return res.toString();
	}
}
