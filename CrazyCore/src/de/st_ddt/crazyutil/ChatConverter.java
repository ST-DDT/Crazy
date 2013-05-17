package de.st_ddt.crazyutil;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.source.Localized;

public class ChatConverter
{

	public final static char[] UNITS = new char[] { 'Y', 'M', 'W', 'D', 'h', 'm', 's', 't', 'i' };
	public final static List<String> UNITS2 = Arrays.asList("Y", "M", "W", "D", "h", "m", "s", "t", "i");
	public final static long[] DURATIONS = new long[] { 31556925216L, 2629743768L, 604800000L, 86400000L, 3600000L, 60000L, 1000L, 50L, 1L };

	private ChatConverter()
	{
	}

	public static Location stringToLocation(final CommandSender sender, final String[] args) throws CrazyCommandException
	{
		if (sender instanceof Player)
			return stringToLocation(((Player) sender).getLocation(), args);
		else
			return stringToLocation(null, null, args);
	}

	public static Location stringToLocation(final Location defaultLocation, final String[] args) throws CrazyCommandException
	{
		if (defaultLocation == null)
			return stringToLocation(null, null, args);
		else
			return stringToLocation(defaultLocation, defaultLocation.getWorld(), args);
	}

	public static Location stringToLocation(final Location defaultLocation, final World defaultworld, final String[] args) throws CrazyCommandException
	{
		Location location = defaultLocation;
		World world = defaultworld;
		switch (args.length)
		{
			case 0:
				if (location == null)
					throw new CrazyCommandUsageException("<World> <X> <Y> <Z>");
				break;
			// <World> <X> <Y> <Z>
			case 3:
				double x = 0;
				try
				{
					x = Double.parseDouble(args[0]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(0, "Number (Double)");
				}
				double y = 0;
				try
				{
					y = Double.parseDouble(args[1]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(1, "Number (Double)");
				}
				double z = 0;
				try
				{
					z = Double.parseDouble(args[2]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(2, "Number (Double)");
				}
				if (world == null)
					throw new CrazyCommandUsageException("<World> <X> <Y> <Z>");
				location = new Location(world, x, y, z);
				break;
			// <World> <X> <Y> <Z>
			case 4:
				world = Bukkit.getWorld(args[0]);
				if (world == null)
					throw new CrazyCommandNoSuchException("World", args[0]);
				x = 0;
				try
				{
					x = Double.parseDouble(args[1]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(1, "Number (Double)");
				}
				y = 0;
				try
				{
					y = Double.parseDouble(args[2]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(2, "Number (Double)");
				}
				z = 0;
				try
				{
					z = Double.parseDouble(args[3]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(3, "Number (Double)");
				}
				location = new Location(world, x, y, z);
				break;
			// <X> <Y> <Z> <Yaw> <Pitch>
			case 5:
				x = 0;
				try
				{
					x = Double.parseDouble(args[0]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(0, "Number (Double)");
				}
				y = 0;
				try
				{
					y = Double.parseDouble(args[1]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(1, "Number (Double)");
				}
				z = 0;
				try
				{
					z = Double.parseDouble(args[2]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(2, "Number (Double)");
				}
				float yaw = 0;
				try
				{
					yaw = Float.parseFloat(args[3]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(3, "Number (Double)");
				}
				float pitch = 0;
				try
				{
					pitch = Float.parseFloat(args[4]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(4, "Number (Double)");
				}
				if (world == null)
					throw new CrazyCommandUsageException("<World> <X> <Y> <Z> <Yaw> <Pitch>");
				location = new Location(world, x, y, z, yaw, pitch);
				break;
			// <World> <X> <Y> <Z> <Yaw> <Pitch>
			case 6:
				world = Bukkit.getWorld(args[0]);
				if (world == null)
					throw new CrazyCommandNoSuchException("World", args[0]);
				x = 0;
				try
				{
					x = Double.parseDouble(args[1]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(1, "Number (Double)");
				}
				y = 0;
				try
				{
					y = Double.parseDouble(args[2]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(2, "Number (Double)");
				}
				z = 0;
				try
				{
					z = Double.parseDouble(args[3]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(3, "Number (Double)");
				}
				yaw = 0;
				try
				{
					yaw = Float.parseFloat(args[4]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(4, "Number (Double)");
				}
				pitch = 0;
				try
				{
					pitch = Float.parseFloat(args[5]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(5, "Number (Double)");
				}
				location = new Location(world, x, y, z, yaw, pitch);
				break;
			default:
				throw new CrazyCommandUsageException("[[World] <X> <Y> <Z>]", "[World] <X> <Y> <Z> <Yaw> <Pitch>");
		}
		return location;
	}

	public static long stringToDuration(final String[] args) throws CrazyCommandException
	{
		long res = 0;
		final int length = args.length;
		for (int i = 0; i < length; i++)
		{
			final String arg = args[i];
			final int len = arg.length();
			final int unitIndex = UNITS2.indexOf(arg.substring(len - 1));
			if (unitIndex == -1)
				throw new CrazyCommandNoSuchException("TimeUnit", arg.substring(len - 1), UNITS2);
			try
			{
				res += Long.parseLong(arg.substring(0, len - 1)) * DURATIONS[unitIndex];
			}
			catch (final NumberFormatException e)
			{
				throw new CrazyCommandParameterException(i, "Number (Long)");
			}
		}
		return res;
	}

	/**
	 * Converts a time in readable information, splited up in units.
	 * 
	 * @param time
	 *            Time in seconds
	 * @param shift
	 *            Required amount of time for highest shown unit
	 * @param target
	 *            The CommandSender who shall recieve this message.
	 * @param units
	 *            How much time units shall be shown. Example: show Weeks and Days but skip Hours
	 * @param showWeeks
	 *            Use Weeks as a unit or not
	 * @return A String representing the time shown in its units. Example: 2 Days 3 Hours 2 Seconds
	 */
	@Localized({ "UNIT.TIME.YEARS", "UNIT.TIME.MONTHS", "UNIT.TIME.WEEKS", "UNIT.TIME.DAYS", "UNIT.TIME.HOURS", "UNIT.TIME.MINUTES", "UNIT.TIME.SECONDS" })
	public static String timeConverter(final long time, final float shift, final CommandSender target, final int units, final boolean showWeeks)
	{
		return timeConverter(time * 1000, shift, target, units, showWeeks, false);
	}

	/**
	 * Converts a time in readable information, splited up in units.
	 * 
	 * @param time
	 *            Time in Milliseconds
	 * @param shift
	 *            Required amount of time for highest shown unit
	 * @param target
	 *            The CommandSender who shall recieve this message.
	 * @param units
	 *            How much time units shall be shown. Example: show Weeks and Days but skip Hours
	 * @param showWeeks
	 *            Use Weeks as a unit or not
	 * @return A String representing the time shown in its units. Example: 2 Days 3 Hours 2 Seconds
	 */
	@Localized({ "UNIT.TIME.YEARS", "UNIT.TIME.MONTHS", "UNIT.TIME.WEEKS", "UNIT.TIME.DAYS", "UNIT.TIME.HOURS", "UNIT.TIME.MINUTES", "UNIT.TIME.SECONDS", "UNIT.TIME.TICKS" })
	public static String timeConverter(long time, float shift, final CommandSender target, int units, final boolean showWeeks, final boolean showTicks)
	{
		final StringBuilder res = new StringBuilder();
		if (time >= shift * DURATIONS[0])
		{
			final long unit = time / DURATIONS[0];
			shift = 1;
			units--;
			time %= DURATIONS[0];
			res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.YEARS", target));
			if (units == 0)
				return res.substring(1);
		}
		if (time >= shift * DURATIONS[1])
		{
			final long unit = time / DURATIONS[1];
			shift = 1;
			units--;
			time %= DURATIONS[1];
			res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.MONTHS", target));
			if (units == 0)
				return res.substring(1);
		}
		if (showWeeks && time >= shift * 604800000)
		{
			final long unit = time / 604800000;
			shift = 1;
			units--;
			time %= 604800000;
			res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.WEEKS", target));
			if (units == 0)
				return res.substring(1);
		}
		if (time >= shift * 86400000)
		{
			final long unit = time / 86400000;
			shift = 1;
			units--;
			time %= 86400000;
			res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.DAYS", target));
			if (units == 0)
				return res.substring(1);
		}
		if (time >= shift * 3600000)
		{
			final long unit = time / 3600000;
			shift = 1;
			units--;
			time %= 3600000;
			res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.HOURS", target));
			if (units == 0)
				return res.substring(1);
		}
		if (time >= shift * 60000)
		{
			final long unit = time / 60000;
			shift = 1;
			units--;
			time %= 60000;
			res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.MINUTES", target));
			if (units == 0)
				return res.substring(1);
		}
		if (showTicks)
		{
			if (time >= shift * 1000)
			{
				final long unit = time / 1000;
				shift = 1;
				units--;
				time %= 1000;
				res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.SECONDS", target));
				if (units == 0)
					return res.substring(1);
			}
			if (time >= shift * 50 || res.length() == 0)
			{
				final long unit = time / 50;
				// shift = 1;
				// units--;
				// time %= 20;
				res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.TICKS", target));
				// if (units == 0)
				// return res.substring(1);
			}
		}
		else if (time >= shift * 1000 || res.length() == 0)
		{
			final long unit = time / 1000;
			// shift = 1;
			// units--;
			// time %= 1000;
			res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.SECONDS", target));
			// if (units == 0)
			// return res.substring(1);
		}
		return res.substring(1);
	}
}
