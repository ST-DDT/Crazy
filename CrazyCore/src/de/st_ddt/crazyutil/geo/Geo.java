package de.st_ddt.crazyutil.geo;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public abstract class Geo
{

	protected World world;

	public static Geo load(ConfigurationSection config, World world)
	{
		return ObjectSaveLoadHelper.load(config, Geo.class, new Class<?>[] { ConfigurationSection.class, World.class }, new Object[] { config, world }, "de.st_ddt.crazyutil.geo");
	}

	public Geo(ConfigurationSection config, World world)
	{
		this(world);
	}

	public Geo(World world)
	{
		this.world = world;
	}

	public World getWorld()
	{
		return getWorld();
	}

	public final void save(ConfigurationSection config, String path, boolean includeType)
	{
		if (includeType)
			config.set(path + "type", getClass().getName());
		save(config, path);
	}

	public abstract void save(ConfigurationSection config, String path);

	public final boolean isInside(Entity entity)
	{
		return isInside(entity.getLocation());
	}

	public abstract boolean isInside(Location loc);
}
