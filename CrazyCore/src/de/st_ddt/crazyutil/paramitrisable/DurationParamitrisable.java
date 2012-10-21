package de.st_ddt.crazyutil.paramitrisable;

import java.util.regex.Pattern;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatConverter;

public class DurationParamitrisable extends LongParamitrisable
{

	protected final static Pattern PATTERN_SPACE = Pattern.compile(" ");

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
}
