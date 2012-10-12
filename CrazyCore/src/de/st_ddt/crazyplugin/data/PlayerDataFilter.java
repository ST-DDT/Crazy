package de.st_ddt.crazyplugin.data;

import de.st_ddt.crazyutil.Filter;

public abstract class PlayerDataFilter<S extends PlayerDataInterface> extends Filter<S> implements PlayerDataFilterInterface<S>
{

	public PlayerDataFilter(final String name, final String... aliases)
	{
		super(name, aliases);
	}
}
