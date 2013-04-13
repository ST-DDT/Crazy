package de.st_ddt.crazyspawner.data;

import org.bukkit.Location;

public enum Thunder
{
	ENABLED,
	EFFECT,
	DISABLED;

	public void trigger(Location location)
	{
		switch (this)
		{
			case ENABLED:
				location.getWorld().strikeLightning(location);
				return;
			case EFFECT:
				location.getWorld().strikeLightningEffect(location);
				return;
			default:
				return;
		}
	}
}
