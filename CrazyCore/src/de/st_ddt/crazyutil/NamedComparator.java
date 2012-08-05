package de.st_ddt.crazyutil;

import java.util.Comparator;

public class NamedComparator implements Comparator<Named>
{

	@Override
	public int compare(final Named o1, final Named o2)
	{
		return o1.getName().compareTo(o2.getName());
	}
}
