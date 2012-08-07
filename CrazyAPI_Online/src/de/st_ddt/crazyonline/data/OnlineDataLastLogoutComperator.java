package de.st_ddt.crazyonline.data;

public class OnlineDataLastLogoutComperator implements OnlineDataComparator
{

	@Override
	public int compare(final OnlineData<?> o1, final OnlineData<?> o2)
	{
		return o1.getLastLogout().compareTo(o2.getLastLogout());
	}
}
