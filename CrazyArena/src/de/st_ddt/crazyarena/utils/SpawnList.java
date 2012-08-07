package de.st_ddt.crazyarena.utils;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public class SpawnList extends ArrayList<Location> implements ConfigurationSaveable
{

	protected final World world;
	private static final long serialVersionUID = -4995034097544178441L;

	public SpawnList(World world)
	{
		super();
		this.world = world;
	}

	public SpawnList(World world, ConfigurationSection config)
	{
		this(world);
		if (config != null)
		{
			for (String key : config.getKeys(false))
				this.add(ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection(key), world));
		}
	}

	public void teleport(LivingEntity entity)
	{
		entity.teleport(randomSpawn(), TeleportCause.PLUGIN);
	}

	public void spawn(EntityType type, int amount)
	{
		for (int i = 0; i < amount; i++)
			world.spawnEntity(randomSpawn(), type);
	}

	public Location randomSpawn()
	{
		return get((int) (Math.random() * size()));
	}

	public Location findNearest(Location target)
	{
		if (target.getWorld() != world)
			return null;
		double dist = Double.MAX_VALUE;
		Location nearest = null;
		for (Location location : this)
		{
			double temp = location.distance(target);
			if (temp < dist)
			{
				dist = temp;
				nearest = location;
			}
		}
		return nearest;
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		int anz = 0;
		for (Location location : this)
			ObjectSaveLoadHelper.saveLocation(config, path + "Location_" + (anz++), location);
	}
}
