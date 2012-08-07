package de.st_ddt.crazyonline.data;

public class OnlineDataOnlineComperator implements OnlineDataComparator
{

	@Override
	public int compare(final OnlineData<?> o1, final OnlineData<?> o2)
	{
		return -(new Long(o1.getTimeTotal()).compareTo(o2.getTimeTotal()));
	}
}
