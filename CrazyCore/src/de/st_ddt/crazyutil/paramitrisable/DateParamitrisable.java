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
					res.add(part + "01");
					res.add(part + "02");
					res.add(part + "03");
					res.add(part + "04");
					res.add(part + "05");
					res.add(part + "06");
					res.add(part + "07");
					res.add(part + "08");
					res.add(part + "09");
					res.add(part + "10");
					res.add(part + "11");
					res.add(part + "12");
					break;
				case 7:
					res.add(part + ".");
					break;
				case 8:
				case 9:
					res.add(part + "01");
					res.add(part + "02");
					res.add(part + "03");
					res.add(part + "04");
					res.add(part + "05");
					res.add(part + "06");
					res.add(part + "07");
					res.add(part + "08");
					res.add(part + "09");
					res.add(part + "10");
					res.add(part + "11");
					res.add(part + "12");
					res.add(part + "13");
					res.add(part + "14");
					res.add(part + "15");
					res.add(part + "16");
					res.add(part + "17");
					res.add(part + "18");
					res.add(part + "19");
					res.add(part + "20");
					res.add(part + "21");
					res.add(part + "22");
					res.add(part + "23");
					res.add(part + "24");
					res.add(part + "25");
					res.add(part + "26");
					res.add(part + "27");
					res.add(part + "28");
					res.add(part + "29");
					res.add(part + "30");
					res.add(part + "31");
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
