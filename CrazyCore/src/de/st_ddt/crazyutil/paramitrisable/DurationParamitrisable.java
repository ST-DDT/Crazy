package de.st_ddt.crazyutil.paramitrisable;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatConverter;

public class DurationParamitrisable extends LongParamitrisable
{

	protected final static Pattern PATTERN_SPACE = Pattern.compile(" ");
	protected final static Pattern PATTERN_NUMERIC = Pattern.compile("[+-]?[0-9]+");

	public DurationParamitrisable(final Long defaultValue)
	{
		super(defaultValue);
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		try
		{
			value = Long.parseLong(parameter);
		}
		catch (final NumberFormatException e)
		{
			value = ChatConverter.stringToDuration(PATTERN_SPACE.split(parameter));
		}
	}

	public final Long getMilliseconds()
	{
		if (value == null)
			return null;
		else
			return value;
	}

	public final long getMilliseconds(final long defaultMilliseconds)
	{
		if (value == null)
			return defaultMilliseconds;
		else
			return value;
	}

	public final Long getTicks()
	{
		if (value == null)
			return null;
		else
			return value / 50;
	}

	public final long getTicks(final long defaultTicks)
	{
		if (value == null)
			return defaultTicks;
		else
			return value / 50;
	}

	public final Long getSeconds()
	{
		if (value == null)
			return null;
		else
			return value / 1000;
	}

	public final long getSeconds(final long defaultSeconds)
	{
		if (value == null)
			return defaultSeconds;
		else
			return value / 1000;
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return tabHelp(parameter);
	}

	public static List<String> tabHelp(final String parameter)
	{
		final List<String> res = new LinkedList<String>();
		final String[] split = PATTERN_SPACE.split(parameter);
		final String part = split[split.length - 1];
		if (PATTERN_NUMERIC.matcher(part).matches())
			for (final String unit : new String[] { "Y", "M", "W", "D", "h", "m", "s", "t" })
				res.add(part + unit);
		if (parameter.length() == 0)
			for (final String unit : new String[] { "Y", "M", "W", "D", "h", "m", "s", "t" })
				res.add(part + "1" + unit);
		return res;
	}
}
