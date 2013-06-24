package de.st_ddt.crazyutil.paramitrisable;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class LongParamitrisable extends TypedParamitrisable<Long>
{

	public LongParamitrisable(final long defaultValue)
	{
		super(defaultValue);
	}

	public LongParamitrisable(final Long defaultValue)
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
			throw new CrazyCommandParameterException(0, "Number (Long)");
		}
	}
}
