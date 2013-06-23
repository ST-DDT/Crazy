package de.st_ddt.crazyspawner.tasks.comparator;

import java.util.Comparator;

import org.bukkit.Location;

import de.st_ddt.crazyspawner.tasks.SpawnTask;

public class SpawnTaskRangeComparator implements Comparator<SpawnTask>
{

	private Location location;

	public void setLocation(final Location location)
	{
		this.location = location;
	}

	@Override
	public int compare(final SpawnTask o1, final SpawnTask o2)
	{
		if (location == null)
			return o1.compareTo(o2);
		if (o1.getLocation().getWorld() == location.getWorld())
			if (o2.getLocation().getWorld() == location.getWorld())
			{
				final int res = Double.compare(location.distance(o1.getLocation()), location.distance(o2.getLocation()));
				if (res == 0)
					return o1.compareTo(o2);
				else
					return res;
			}
			else
				return -1;
		else if (o2.getLocation().getWorld() == location.getWorld())
			return 1;
		else
			return o1.compareTo(o2);
	}
}
