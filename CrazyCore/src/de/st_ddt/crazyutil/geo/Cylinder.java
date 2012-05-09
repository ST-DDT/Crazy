package de.st_ddt.crazyutil.geo;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class Cylinder extends Sphere
{

	protected double height;

	public Cylinder(ConfigurationSection config, World world)
	{
		super(config, world);
		height = config.getDouble("height", 1);
	}

	public Cylinder(Location center, double radius, double height)
	{
		super(center, radius);
		this.height = height;
	}

	public double getHeight()
	{
		return height;
	}

	public void setHeight(double height)
	{
		this.height = height;
	}

	@Override
	public boolean isInside(Location loc)
	{
		if (loc.getWorld() != world)
			return false;
		double dX = loc.getX() - center.getX();
		double dZ = loc.getZ() - center.getZ();
		if (radius < Math.sqrt(Math.pow(dX, 2) + Math.pow(dZ, 2)))
			return false;
		if (loc.getY() < center.getY())
			return false;
		if (loc.getY() > center.getY() + height)
			return false;
		return true;
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		super.save(config, path);
		config.set(path + "height", height);
	}
}
