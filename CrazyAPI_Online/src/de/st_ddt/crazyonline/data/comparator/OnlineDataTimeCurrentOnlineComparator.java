package de.st_ddt.crazyonline.data.comparator;

import de.st_ddt.crazyonline.data.OnlineData;

public class OnlineDataTimeCurrentOnlineComparator implements OnlineDataComparator
{

	@Override
	public int compare(final OnlineData o1, final OnlineData o2)
	{
		return new Long(o2.getTimeLast()).compareTo(o1.getTimeLast());
	}
}
