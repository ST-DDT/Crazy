package de.st_ddt.crazyonline.data.comparator;

import de.st_ddt.crazyonline.data.OnlineData;

public class OnlineDataNameComparator implements OnlineDataComparator
{

	@Override
	public int compare(final OnlineData o1, final OnlineData o2)
	{
		return o1.getName().compareTo(o2.getName());
	}
}
