package de.st_ddt.crazyutil.paramitrisable;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class DoubleParamitrisable extends NumberParamitrisable<Double>
{

	public DoubleParamitrisable(final double defaultValue)
	{
		super(defaultValue);
	}

	public DoubleParamitrisable(final Double defaultValue)
	{
		super(defaultValue);
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		try
		{
			value = Double.parseDouble(parameter);
		}
		catch (final NumberFormatException e)
		{
			throw new CrazyCommandParameterException(0, "Number (Double)");
		}
	}
}
