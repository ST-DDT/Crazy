package de.st_ddt.crazydetectorsign.actions;

import org.bukkit.Location;
import org.bukkit.Material;

public abstract class ActionRunnable implements Runnable
{

	protected final Location location;

	public ActionRunnable(final Location location)
	{
		super();
		this.location = location;
	}

	public Location getLocation()
	{
		return location;
	}

	public void activate(final boolean active)
	{
		if (location.getBlock().getType() == Material.LEVER)
			location.getBlock().setData((byte) (active ? location.getBlock().getData() | 8 : location.getBlock().getData() & 7));
	}

	@Override
	public abstract void run();
}
