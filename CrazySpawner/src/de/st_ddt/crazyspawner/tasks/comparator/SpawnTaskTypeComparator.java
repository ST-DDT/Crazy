package de.st_ddt.crazyspawner.tasks.comparator;

import java.util.Comparator;

import de.st_ddt.crazyspawner.tasks.TimerSpawnTask;

public class SpawnTaskTypeComparator implements Comparator<TimerSpawnTask>
{

	@Override
	public int compare(final TimerSpawnTask o1, final TimerSpawnTask o2)
	{
		final int res = o1.getType().getName().compareTo(o2.getType().getName());
		if (res == 0)
			return o1.compareTo(o2);
		else
			return res;
	}
}
