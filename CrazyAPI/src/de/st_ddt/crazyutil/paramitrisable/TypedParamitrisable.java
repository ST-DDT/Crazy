package de.st_ddt.crazyutil.paramitrisable;

import java.util.LinkedList;
import java.util.List;

import de.st_ddt.crazyplugin.exceptions.CrazyException;

public abstract class TypedParamitrisable<S> implements TabbedParamitrisable
{

	protected S value;

	public TypedParamitrisable(final S defaultValue)
	{
		super();
		this.value = defaultValue;
	}

	@Override
	public abstract void setParameter(String parameter) throws CrazyException;

	public final S getValue()
	{
		return value;
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return new LinkedList<String>();
	}

	@Override
	public String toString()
	{
		if (getValue() == null)
			return "null";
		else
			return getValue().toString();
	}
}
