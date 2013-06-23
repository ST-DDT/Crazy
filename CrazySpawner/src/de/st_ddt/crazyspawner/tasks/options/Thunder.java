package de.st_ddt.crazyspawner.tasks.options;

import org.bukkit.Location;

public enum Thunder
{
	ENABLED,
	EFFECT,
	DISABLED;

	public void trigger(final Location location)
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
