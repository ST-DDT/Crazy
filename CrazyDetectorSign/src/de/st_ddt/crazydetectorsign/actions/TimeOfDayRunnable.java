package de.st_ddt.crazydetectorsign.actions;

import org.bukkit.Location;
import org.bukkit.World;

public class TimeOfDayRunnable extends ActionRunnable
{

	protected final boolean[] active = { false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };
	protected final World world;

	public TimeOfDayRunnable(Location location, World world, String time)
	{
		super(location);
		this.world = world;
		if (time.equalsIgnoreCase("day"))
			for (int i = 6; i < 20; i++)
				active[i] = true;
		else if (time.equalsIgnoreCase("night"))
		{
			for (int i = 0; i < 6; i++)
				active[i] = true;
			for (int i = 20; i < 24; i++)
				active[i] = true;
		}
	}

	@Override
	public void run()
	{
		int time = (int) (world.getTime() / 1000 + 6) % 24;
		if (0 < time && time < 24)
			activate(active[time]);
	}
}
