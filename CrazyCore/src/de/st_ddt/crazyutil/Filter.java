package de.st_ddt.crazyutil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.st_ddt.crazyplugin.exceptions.CrazyException;

public abstract class Filter<S> implements FilterInterface<S>
{

	protected final String name;
	protected final String[] aliases;

	public static <S> Collection<FilterInstanceInterface<S>> getFilterInstances(final FilterInterface<S> filter)
	{
		final Set<FilterInstanceInterface<S>> res = new HashSet<FilterInstanceInterface<S>>();
		res.add(filter.getInstance());
		return res;
	}

	public static <S> Collection<FilterInstanceInterface<S>> getFilterInstances(final Collection<? extends FilterInterface<S>> filters)
	{
		final Set<FilterInstanceInterface<S>> res = new HashSet<FilterInstanceInterface<S>>();
		for (final FilterInterface<S> filter : filters)
			res.add(filter.getInstance());
		return res;
	}

	public static <S> Map<String, FilterInstanceInterface<S>> getFilterMap(final Collection<FilterInstanceInterface<S>> filters)
	{
		final Map<String, FilterInstanceInterface<S>> res = new HashMap<String, FilterInstanceInterface<S>>();
		for (final FilterInstanceInterface<S> filter : filters)
		{
			res.put(filter.getName(), filter);
			for (final String alias : filter.getAliases())
				res.put(alias, filter);
		}
		return res;
	}

	public static <S> Map<String, Tabbed> getFilterTabMap(final Collection<? extends FilterInterface<S>> filters)
	{
		final Map<String, Tabbed> res = new HashMap<String, Tabbed>();
		for (final FilterInterface<S> filter : filters)
		{
			res.put(filter.getName(), filter);
			for (final String alias : filter.getAliases())
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

	@Override
	public abstract FilterInstance getInstance();

	@Override
	public final String[] getAliases()
	{
		return aliases;
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return new ArrayList<String>();
	}

	protected abstract class FilterInstance extends AbstractFilterInstance<S>
	{

		@Override
		public final String getName()
		{
			return name;
		}

		@Override
		public final String[] getAliases()
		{
			return aliases;
		}

		@Override
		public abstract void setParameter(String parameter) throws CrazyException;

		@Override
		public abstract boolean filter(S data);
	}

	public abstract static class AbstractFilterInstance<S> implements FilterInstanceInterface<S>
	{

		@Override
		public boolean isActive()
		{
			return true;
		}

		@Override
		public void filter(final Collection<? extends S> datas)
		{
			final Iterator<? extends S> it = datas.iterator();
			while (it.hasNext())
				if (!filter(it.next()))
					it.remove();
		}
	}

	public abstract static class DeafFilterInstance<S> extends AbstractFilterInstance<S>
	{

		private final static String[] DEAFALIASES = new String[0];

		@Override
		public String getName()
		{
			return null;
		}

		@Override
		public String[] getAliases()
		{
			return DEAFALIASES;
		}

		@Override
		public void setParameter(final String parameter) throws CrazyException
		{
		}
	}
}
