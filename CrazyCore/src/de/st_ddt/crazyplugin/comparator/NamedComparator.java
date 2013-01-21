package de.st_ddt.crazyplugin.comparator;

import java.util.Comparator;

import de.st_ddt.crazyutil.Named;

public class NamedComparator implements Comparator<Named>
{

	@Override
	public int compare(final Named o1, final Named o2)
	{
		return o1.getName().compareToIgnoreCase(o2.getName());
	}
}
