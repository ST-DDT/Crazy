package de.st_ddt.crazyonline.data;

public class OnlineDataLastLoginComperator implements OnlineDataComparator
{

	@Override
	public int compare(final OnlineData<?> o1, final OnlineData<?> o2)
	{
		return o1.getLastLogin().compareTo(o2.getLastLogin());
	}
}
