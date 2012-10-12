package de.st_ddt.crazyplugin.comparator;

import de.st_ddt.crazyplugin.data.PlayerDataInterface;

public class PlayerDataNameComparator<S extends PlayerDataInterface> implements PlayerDataComparator<S>
{

	@Override
	public int compare(final S o1, final S o2)
	{
		return o1.getName().compareTo(o2.getName());
	}
}
