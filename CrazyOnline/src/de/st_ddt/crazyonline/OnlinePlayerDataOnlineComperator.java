package de.st_ddt.crazyonline;

import java.util.Comparator;

public class OnlinePlayerDataOnlineComperator implements Comparator<OnlinePlayerData>
{

	@Override
	public int compare(OnlinePlayerData data1, OnlinePlayerData data2)
	{
		return -(new Long(data1.getTimeTotal()).compareTo(data2.getTimeTotal()));
	}
}
