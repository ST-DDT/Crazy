package de.st_ddt.crazyonline;

import java.util.Comparator;

public class OnlinePlayerDataLoginComperator implements Comparator<OnlinePlayerData>
{

	@Override
	public int compare(final OnlinePlayerData data1, final OnlinePlayerData data2)
	{
		return data1.getLastLogin().compareTo(data2.getLastLogin());
	}
}
