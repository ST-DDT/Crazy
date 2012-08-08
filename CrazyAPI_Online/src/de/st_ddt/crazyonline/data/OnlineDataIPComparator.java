package de.st_ddt.crazyonline.data;

public class OnlineDataIPComparator implements OnlineDataComparator
{

	@Override
	public int compare(final OnlineData o1, final OnlineData o2)
	{
		return o1.getLatestIP().compareTo(o2.getLatestIP());
	}
}
