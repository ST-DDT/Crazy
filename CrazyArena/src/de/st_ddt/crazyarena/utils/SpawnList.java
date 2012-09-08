package de.st_ddt.crazyarena.utils;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public class SpawnList extends ArrayList<Location> implements ConfigurationSaveable
{

	private static final long serialVersionUID = -4995034097544178441L;

	public SpawnList()
	{
		super();
	}

	public SpawnList(final ConfigurationSection config)
	{
		super();
		if (config != null)
		{
			for (final String key : config.getKeys(false))
				this.add(ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection(key), null));
		}
	}

	public void teleport(final LivingEntity entity)
	{
		entity.teleport(randomSpawn(), TeleportCause.PLUGIN);
	}

	public void spawn(final EntityType type, final int amount)
	{
		for (int i = 0; i < amount; i++)
		{
			final Location location = randomSpawn();
			location.getWorld().spawnEntity(location, type);
		}
	}

	public Location randomSpawn()
	{
		return get((int) (Math.random() * size()));
	}

	public Location findNearest(final Location target)
	{
		double dist = Double.MAX_VALUE;
		Location nearest = null;
		for (final Location location : this)
		{
			if (target.getWorld() != location.getWorld())
				continue;
			final double temp = location.distance(target);
			if (temp < dist)
			{
				dist = temp;
				nearest = location;
			}
		}
		return nearest;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		int anz = 0;
		for (final Location location : this)
			ObjectSaveLoadHelper.saveLocation(config, path + "Location_" + (anz++), location, true, true);
	}
}
