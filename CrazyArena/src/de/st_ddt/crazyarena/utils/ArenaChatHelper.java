package de.st_ddt.crazyarena.utils;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class ArenaChatHelper extends ChatHelperExtended
{

	protected ArenaChatHelper()
	{
		super();
	}

	/**
	 * Converts a time into a string.
	 * 
	 * @param timeTotal
	 *            Time in milliseconds.
	 * @param target
	 *            The target who should recieve the message.
	 * @return A time/duration formated like this 2 days 5:12:3.100
	 */
	public static String timeConverter(final long timeTotal, final CommandSender target)
	{
		final StringBuilder res = new StringBuilder();
		long time = timeTotal;
		if (timeTotal > 86400000)
		{
			final long unit = time / 86400000;
			time %= 86400000;
			res.append(unit + " " + CrazyLocale.getUnitText("TIME.DAYS", target) + " ");
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
