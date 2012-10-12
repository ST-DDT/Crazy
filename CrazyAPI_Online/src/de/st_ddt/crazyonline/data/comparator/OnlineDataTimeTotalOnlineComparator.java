package de.st_ddt.crazyonline.data.comparator;

import de.st_ddt.crazyonline.data.OnlineData;

public class OnlineDataTimeTotalOnlineComparator implements OnlineDataComparator
{

	@Override
	public int compare(final OnlineData o1, final OnlineData o2)
	{
		return new Long(o2.getTimeTotal()).compareTo(o1.getTimeTotal());
	}
}
