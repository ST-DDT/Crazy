package de.st_ddt.crazyutil.geo;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public class Sphere extends Geo
{

	protected Location center;
	protected double radius;

	public Sphere(Location center, double radius)
	{
		super(center.getWorld());
		this.center = center;
		this.radius = radius;
	}

	public Sphere(ConfigurationSection config, World world)
	{
		super(config, world);
		center = ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection("center"), world);
		radius = config.getDouble("radius.", 1);
	}

	public Location getCenter()
	{
		return center;
	}

	public void setCenter(Location center)
	{
		if (center == null)
			return;
		this.center = center;
		this.world = center.getWorld();
	}

	public double getRadius()
	{
		return radius;
	}

	public void setRadius(double radius)
	{
		this.radius = radius;
	}

	@Override
	public boolean isInside(Location loc)
	{
		return world == loc.getWorld() && center.distance(loc) < radius;
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		ObjectSaveLoadHelper.saveLocation(config, path + "center.", center, false);
		config.set(path + "range", radius);
	}
}
