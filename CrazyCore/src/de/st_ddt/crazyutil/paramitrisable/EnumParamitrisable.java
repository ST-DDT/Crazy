package de.st_ddt.crazyutil.paramitrisable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class EnumParamitrisable<S extends Enum<S>> extends TypedParamitrisable<S>
{

	private final String type;
	protected final Map<String, S> values;

	public EnumParamitrisable(final String type, final S defaultValue, final Collection<S> values)
	{
		super(defaultValue);
		this.type = type;
		this.values = new TreeMap<String, S>();
		for (final S value : values)
			this.values.put(value.name().toUpperCase(), value);
	}

	public EnumParamitrisable(final String type, final S defaultValue, final S... values)
	{
		super(defaultValue);
		this.type = type;
		this.values = new TreeMap<String, S>();
		for (final S value : values)
			this.values.put(value.name().toUpperCase(), value);
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		if (values.containsKey(parameter.toUpperCase()))
			value = values.get(parameter.toUpperCase());
		else
			throw new CrazyCommandNoSuchException(type, parameter, values.keySet());
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return MapParamitrisable.tabHelp(values, parameter.toUpperCase());
	}

	public static <S extends Enum<S>> List<String> getEnumNames(final S... values)
	{
		final List<String> res = new ArrayList<String>(values.length);
		for (final S value : values)
			res.add(value.name());
		return res;
	}
}
