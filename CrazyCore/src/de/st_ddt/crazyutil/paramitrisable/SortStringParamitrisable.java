package de.st_ddt.crazyutil.paramitrisable;

import java.util.Comparator;
import java.util.Map;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class SortStringParamitrisable<S> extends StringParamitrisable
{

	protected final Map<String, ? extends Comparator<S>> sorters;
	protected Comparator<S> sorter;

	public SortStringParamitrisable(final Map<String, ? extends Comparator<S>> sorters, final Comparator<S> defaultSorter)
	{
		super(null);
		this.sorters = sorters;
		this.sorter = defaultSorter;
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		super.setParameter(parameter);
		sorter = sorters.get(parameter.toLowerCase());
		if (sorter == null)
			throw new CrazyCommandNoSuchException("Sorter", parameter, sorters.keySet());
	}

	public Comparator<S> getSorter()
	{
		return sorter;
	}
}
