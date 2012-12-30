package de.st_ddt.crazyutil.paramitrisable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class MultiParamitrisable<S> extends TypedParamitrisable<List<S>> implements InfiniteParamitrisableInterface
{

	private final TypedParamitrisable<S> param;

	public MultiParamitrisable(final TypedParamitrisable<S> param)
	{
		super(new LinkedList<S>());
		this.param = param;
	}

	public MultiParamitrisable(final TypedParamitrisable<S> param, final Collection<S> startEntries)
	{
		super(new LinkedList<S>(startEntries));
		this.param = param;
	}

	public MultiParamitrisable(final TypedParamitrisable<S> param, final S... startEntries)
	{
		super(new LinkedList<S>());
		for (final S entry : startEntries)
			value.add(entry);
		this.param = param;
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		param.setParameter(parameter);
		value.add(param.getValue());
	}
}
