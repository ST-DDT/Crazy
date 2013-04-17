package de.st_ddt.crazyspawner.data.comparator;

import java.util.Comparator;

import de.st_ddt.crazyspawner.tasks.SpawnTask;

public class SpawnTaskTypeComparator implements Comparator<SpawnTask>
{

	@Override
	public int compare(final SpawnTask o1, final SpawnTask o2)
	{
		final int res = o1.getType().getName().compareTo(o2.getType().getName());
		if (res == 0)
			return o1.compareTo(o2);
		else
			return res;
	}
}
