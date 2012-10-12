package de.st_ddt.crazyutil;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.st_ddt.crazyplugin.exceptions.CrazyException;

public abstract class Filter<S> implements FilterInterface<S>
{

	protected final String name;
	protected final String[] aliases;

	public static <S> Collection<FilterInstanceInterface<S>> getFilterInstances(FilterInterface<S> filter)
	{
		Set<FilterInstanceInterface<S>> res = new HashSet<FilterInstanceInterface<S>>();
		res.add(filter.getInstance());
		return res;
	}

	public static <S> Collection<FilterInstanceInterface<S>> getFilterInstances(Collection<? extends FilterInterface<S>> filters)
	{
		Set<FilterInstanceInterface<S>> res = new HashSet<FilterInstanceInterface<S>>();
		for (FilterInterface<S> filter : filters)
			res.add(filter.getInstance());
		return res;
	}

	public static <S> Map<String, FilterInstanceInterface<S>> getFilterMap(Collection<FilterInstanceInterface<S>> filters)
	{
		Map<String, FilterInstanceInterface<S>> res = new HashMap<String, FilterInstanceInterface<S>>();
		for (FilterInstanceInterface<S> filter : filters)
		{
			res.put(filter.getName(), filter);
			for (String alias : filter.getAliases())
				res.put(alias, filter);
		}
		return res;
	}

	public Filter(final String name, final String... aliases)
	{
		super();
		this.name = name;
		this.aliases = aliases;
	}

	@Override
	public final String getName()
	{
		return name;
	}

	public abstract FilterInstance getInstance();

	public final String[] getAliases()
	{
		return aliases;
	}

	protected abstract class FilterInstance implements Named, FilterInstanceInterface<S>
	{

		@Override
		public final String getName()
		{
			return name;
		}

		public final String[] getAliases()
		{
			return aliases;
		}

		@Override
		public abstract void setParameter(String parameter) throws CrazyException;

		public void filter(final Collection<? extends S> datas)
		{
			final Iterator<? extends S> it = datas.iterator();
			while (it.hasNext())
				if (!filter(it.next()))
					it.remove();
		}

		public abstract boolean filter(S data);
	}
}
