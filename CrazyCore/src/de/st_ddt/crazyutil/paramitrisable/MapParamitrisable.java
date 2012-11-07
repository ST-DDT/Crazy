package de.st_ddt.crazyutil.paramitrisable;

import java.util.Map;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable.TypedParamitrisable;

public class MapParamitrisable<S> extends TypedParamitrisable<S>
{

	private final String type;
	private final Map<String, ? extends S> values;
	private final boolean lowercase;

	public MapParamitrisable(String type, Map<String, ? extends S> values, S defaultValue)
	{
		super(defaultValue);
		this.type = type;
		this.values = values;
		this.lowercase = false;
	}

	public MapParamitrisable(String type, Map<String, ? extends S> values, S defaultValue, boolean lowercase)
	{
		super(defaultValue);
		this.type = type;
		this.values = values;
		this.lowercase = lowercase;
	}

	@Override
	public void setParameter(String parameter) throws CrazyException
	{
		if (lowercase)
			parameter = parameter.toLowerCase();
		if (!values.containsKey(parameter))
			throw new CrazyCommandNoSuchException(type, parameter, values.keySet());
		value = values.get(parameter);
	}
}
