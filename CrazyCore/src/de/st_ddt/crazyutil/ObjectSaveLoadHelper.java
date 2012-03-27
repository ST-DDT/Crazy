package de.st_ddt.crazyutil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class ObjectSaveLoadHelper
{

	public static Location loadLocation(FileConfiguration config, String path, World world)
	{
		if (world == null)
			world = Bukkit.getWorld(config.getString(path + "world"));
		return new Location(world, config.getDouble(path + "x"), config.getDouble(path + "y"), config.getDouble(path + "z"), (float) config.getDouble(path + "yaw", 0d), (float) config.getDouble(path + "pitch", 0d));
	}

	public static void saveLocation(FileConfiguration config, String path, Location location)
	{
		saveLocation(config, path, location, false);
	}

	public static void saveLocation(FileConfiguration config, String path, Location location, boolean saveWorld)
	{
		config.set(path + "x", location.getX());
		config.set(path + "y", location.getY());
		config.set(path + "z", location.getZ());
		config.set(path + "yaw", location.getYaw());
		config.set(path + "pitch", location.getPitch());
		if (saveWorld)
			config.set(path + "world", location.getWorld().getName());
	}
}
