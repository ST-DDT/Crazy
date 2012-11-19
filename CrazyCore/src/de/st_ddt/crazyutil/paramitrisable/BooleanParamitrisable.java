package de.st_ddt.crazyutil.paramitrisable;

import java.util.LinkedList;
import java.util.List;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class BooleanParamitrisable extends TypedParamitrisable<Boolean>
{

	public BooleanParamitrisable(final Boolean defaultValue)
	{
		super(defaultValue);
	}

	@Override
	public void setParameter(String parameter) throws CrazyException
	{
		parameter = parameter.toLowerCase();
		if (parameter.equals("true"))
			value = true;
		else if (parameter.equals("1"))
			value = true;
		else if (parameter.equals("y"))
			value = true;
		else if (parameter.equals("yes"))
			value = true;
		else if (parameter.equals("false"))
			value = false;
		else if (parameter.equals("0"))
			value = false;
		else if (parameter.equals("n"))
			value = false;
		else if (parameter.equals("no"))
			value = false;
		else
			throw new CrazyCommandParameterException(0, "Boolean (false/true)");
	}

	@Override
	public List<String> tab(String parameter)
	{
		parameter = parameter.toLowerCase();
		final List<String> res = new LinkedList<String>();
		if ("true".startsWith(parameter))
			res.add("true");
		if ("false".startsWith(parameter))
			res.add("false");
		return res;
	}
}
