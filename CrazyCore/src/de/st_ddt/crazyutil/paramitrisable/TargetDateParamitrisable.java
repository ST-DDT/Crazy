package de.st_ddt.crazyutil.paramitrisable;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatConverter;

public class TargetDateParamitrisable extends DateParamitrisable
{

	protected final static Pattern PATTERN_SPACE = Pattern.compile(" ");
	protected final static Pattern PATTERN_NUMERIC = Pattern.compile("[+-]?[0-9]*");

	public TargetDateParamitrisable(final Date defaultValue)
	{
		super(defaultValue);
	}

	public TargetDateParamitrisable(final long offset)
	{
		super(new Date(new Date().getTime() + offset));
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		try
		{
			if (parameter.contains(" "))
				value = CrazyLightPluginInterface.DATETIMEFORMAT.parse(parameter);
			else
				value = CrazyLightPluginInterface.DATEFORMAT.parse(parameter);
		}
		catch (final ParseException e)
		{
			value = new Date();
			try
			{
				value.setTime(value.getTime() + ChatConverter.stringToDuration(PATTERN_SPACE.split(parameter)));
			}
			catch (final CrazyCommandException ce)
			{
				throw new CrazyCommandParameterException(0, "Date (YYYY.MM.DD [hh:mm:ss])/Duration (2Y 1M -3W 9D 5h 70m -100s 4t)");
			}
		}
	}

	@Override
	public List<String> tab(final String parameter)
	{
		final List<String> res = super.tab(parameter);
		if (res.size() == 0)
		{
			final String[] split = PATTERN_SPACE.split(parameter);
			final String part = split[split.length - 1];
			if (!PATTERN_NUMERIC.matcher(part).matches())
				return res;
			for (final String unit : new String[] { "Y", "M", "W", "D", "h", "m", "s", "t" })
				res.add(part + unit);
		}
		return res;
	}
}
