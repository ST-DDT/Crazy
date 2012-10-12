package de.st_ddt.crazyutil.paramitrisable;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;

public class ColoredStringParamitrisable extends StringParamitrisable
{

	public ColoredStringParamitrisable(final String defaultValue)
	{
		super(defaultValue);
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		value = ChatHelper.colorise(parameter);
	}
}
