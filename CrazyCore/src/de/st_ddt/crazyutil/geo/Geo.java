package de.st_ddt.crazyutil.geo;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;

public abstract class Geo
{
	
	protected World world;
	
	public static Geo load(FileConfiguration config, String path, World world)
	{
		String newType = config.getString(path + "type", "-1");
		if (newType.equals("-1"))
			return null;
		Class<?> newClass = null;
		try
		{
			newClass = Class.forName(newType);
		}
		catch (ClassNotFoundException e)
		{
			try
			{
				newClass = Class.forName("de.st_ddt.crazyutil.geo." + newType);
			}
			catch (ClassNotFoundException e2)
			{
				e.printStackTrace();
				return null;
			}
		}
		if (Geo.class.isAssignableFrom(newClass))
		{
			return null;
		}
		Geo newGeo = null;
		try
		{
			newGeo = (Geo) newClass.getConstructor(FileConfiguration.class, String.class,World.class).newInstance(config, path, world);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return newGeo;
	}

	public Geo(FileConfiguration config, String path, World world)
	{
		this(world);
	}
	
	public Geo(World world)
	{
		this.world=world;
	}
	
	public World getWorld()
	{
		return getWorld();
	}

	public abstract void save(FileConfiguration config, String path, boolean includeType);

	public final boolean isInside(Entity entity)
	{
		return isInside(entity.getLocation());
	}

	public abstract boolean isInside(Location loc);
}
