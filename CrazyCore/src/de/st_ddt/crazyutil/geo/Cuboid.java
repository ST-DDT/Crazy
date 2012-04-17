package de.st_ddt.crazyutil.geo;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public class Cuboid extends Geo
{

	Location location1, location2;
	Location locationMin, locationMax;

	public Cuboid(ConfigurationSection config, World world)
	{
		super(config, world);
		this.location1 = ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection("loc1"), world);
		this.location2 = ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection("loc2"), world);
		this.location1.setPitch(0);
		this.location1.setYaw(0);
		this.location2.setPitch(0);
		this.location2.setYaw(0);
		updateCornerLocations();
	}

	public Cuboid(World world, Location location1, Location location2)
	{
		super(world);
		this.location1 = location1.clone();
		this.location2 = location2.clone();
		this.location1.setPitch(0);
		this.location1.setYaw(0);
		this.location2.setPitch(0);
		this.location2.setYaw(0);
		updateCornerLocations();
	}

	public Cuboid(World world, Location location1, double x, double y, double z)
	{
		super(world);
		this.location1 = location1.clone();
		this.location2 = location1.clone().add(x, y, z);
		this.location1.setYaw(0);
		this.location2.setPitch(0);
		this.location2.setYaw(0);
		updateCornerLocations();
	}

	protected void updateCornerLocations()
	{
		locationMin = new Location(world, Math.min(location1.getX(), location2.getX()), Math.min(location1.getY(), location2.getY()), Math.min(location1.getZ(), location2.getZ()));
		locationMax = new Location(world, Math.max(location1.getX(), location2.getX()), Math.max(location1.getY(), location2.getY()), Math.max(location1.getZ(), location2.getZ()));
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		ObjectSaveLoadHelper.saveLocation(config, path + "loc1.", location1);
		ObjectSaveLoadHelper.saveLocation(config, path + "loc2.", location2);
	}

	@Override
	public boolean isInside(Location location)
	{
		if (world != location.getWorld())
			return false;
		if (locationMin.getX() < location.getX() || location.getX() > locationMax.getX())
			return false;
		if (locationMin.getY() < location.getY() || location.getY() > locationMax.getY())
			return false;
		if (locationMin.getZ() < location.getZ() || location.getZ() > locationMax.getZ())
			return false;
		return true;
	}
}
