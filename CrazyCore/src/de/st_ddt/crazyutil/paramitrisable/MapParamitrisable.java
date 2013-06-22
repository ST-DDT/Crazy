package de.st_ddt.crazyutil.paramitrisable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class MapParamitrisable<S> extends TypedParamitrisable<S>
{

	protected final String type;
	protected final Map<String, ? extends S> values;
	private final boolean lowercase;

	public MapParamitrisable(final String type, final Map<String, ? extends S> values, final S defaultValue)
	{
		super(defaultValue);
		this.type = type;
		this.values = values;
		this.lowercase = false;
	}

	public MapParamitrisable(final String type, final Map<String, ? extends S> values, final S defaultValue, final boolean lowercase)
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
		if (values.containsKey(parameter))
			value = values.get(parameter);
		else
			throw new CrazyCommandNoSuchException(type, parameter, values.keySet());
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return tabHelp(values, parameter);
	}

	public static List<String> tabHelp(final Map<String, ?> values, final String parameter)
	{
		final List<String> res = new LinkedList<String>();
		for (final String entry : values.keySet())
			if (entry.startsWith(parameter))
				res.add(entry);
		return res;
	}

	public final Map<String, ? extends S> getValues()
	{
		return values;
	}
}
