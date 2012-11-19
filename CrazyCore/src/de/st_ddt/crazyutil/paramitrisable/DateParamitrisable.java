package de.st_ddt.crazyutil.paramitrisable;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class DateParamitrisable extends TypedParamitrisable<Date>
{

	protected final Pattern PATTERN_SPACE = Pattern.compile(" ");
	protected final Pattern PATTERN_DOT = Pattern.compile("\\.");
	protected final Pattern PATTERN_DATEPART1 = Pattern.compile("[0-9.]*");
	protected final Pattern PATTERN_DATEPART2 = Pattern.compile("[0-9:]*");

	public DateParamitrisable(final Date defaultValue)
	{
		super(defaultValue);
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
			throw new CrazyCommandParameterException(0, "Date (YYYY.MM.DD [hh:mm:ss])");
		}
	}

	@Override
	public List<String> tab(final String parameter)
	{
		final List<String> res = new LinkedList<String>();
		final String[] split = PATTERN_SPACE.split(parameter);
		if (split.length == 1)
		{
			final String part = split[0];
			if (!PATTERN_DATEPART1.matcher(part).matches())
				return res;
			switch (part.length())
			{
				case 0:
				case 1:
				case 2:
				case 3:
					final String[] dateSplit = PATTERN_DOT.split(CrazyLightPluginInterface.DATEFORMAT.format(new Date()));
					if (dateSplit[0].startsWith(part))
						res.add(dateSplit[0]);
					break;
				case 4:
					res.add(part + ".");
					break;
				case 5:
				case 6:
					for (int i = 1; i <= 12; i++)
						res.add(part + Integer.toString(i));
					break;
				case 7:
					res.add(part + ".");
					break;
				case 8:
				case 9:
					for (int i = 1; i <= 31; i++)
						res.add(part + Integer.toString(i));
					break;
			}
			return res;
		}
		else if (split.length == 2)
		{
			final String part = split[1];
			if (!PATTERN_DATEPART2.matcher(part).matches())
				return res;
			switch (part.length())
			{
				case 0:
				case 1:
					for (int i = 0; i <= 23; i++)
						res.add(part + Integer.toString(i));
					break;
				case 2:
					res.add(part + ":");
					break;
				case 3:
				case 4:
					for (int i = 0; i <= 59; i++)
						res.add(part + Integer.toString(i));
					break;
				case 5:
					res.add(part + ":");
					break;
				case 6:
				case 7:
					for (int i = 0; i <= 59; i++)
						res.add(part + Integer.toString(i));
					break;
			}
			return res;
		}
		else
			return res;
	}
}
