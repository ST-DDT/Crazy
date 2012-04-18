package de.st_ddt.crazyutil;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public final class ObjectSaveLoadHelper
{

	private ObjectSaveLoadHelper()
	{
	}

	public static Location loadLocation(final ConfigurationSection config, World world)
	{
		if (config == null)
			return null;
		if (world == null)
			world = Bukkit.getWorld(config.getString("world"));
		return new Location(world, config.getDouble("x"), config.getDouble("y"), config.getDouble("z"), (float) config.getDouble("yaw", 0d), (float) config.getDouble("pitch", 0d));
	}

	public static void saveLocation(final ConfigurationSection config, final String path, final Location location)
	{
		saveLocation(config, path, location, false, true);
	}

	public static void saveLocation(final ConfigurationSection config, final String path, final Location location, final boolean saveRotation)
	{
		saveLocation(config, path, location, false, saveRotation);
	}

	public static void saveLocation(final ConfigurationSection config, final String path, final Location location, final boolean saveWorld, final boolean saveRotation)
	{
		config.set(path + "x", location.getX());
		config.set(path + "y", location.getY());
		config.set(path + "z", location.getZ());
		if (saveRotation)
		{
			config.set(path + "yaw", location.getYaw());
			config.set(path + "pitch", location.getPitch());
		}
		if (saveWorld)
			config.set(path + "world", location.getWorld().getName());
	}

	public static <T> List<T> loadList(final ConfigurationSection config, final Class<T> parentClazz, final Class<?>[] paraClazzes, final Object[] paraObjects, final String alternativePackage)
	{
		return loadList(config, parentClazz, paraClazzes, paraObjects, null);
	}

	public static <T> List<T> loadList(final ConfigurationSection config, final Class<T> parentClazz, final Class<?>[] paraClazzes, final Object[] paraObjects)
	{
		ArrayList<T> list = new ArrayList<T>();
		if (config == null)
			return list;
		for (String key : config.getKeys(false))
			list.add(load(config.getConfigurationSection(key), parentClazz, paraClazzes, paraObjects));
		return list;
	}

	public static <T> T load(final ConfigurationSection config, final Class<T> parentClazz, final Class<?>[] paraClazzes, final Object[] paraObjects)
	{
		return load(config, parentClazz, paraClazzes, paraObjects, null);
	}

	public static <T> T load(final ConfigurationSection config, final Class<T> parentClazz, final Class<?>[] paraClazzes, final Object[] paraObjects, final String alternativePackage)
	{
		String clazzName = config.getString("type", "-1");
		if (clazzName.equals("-1"))
		{
			System.err.println("No class defined!");
			return null;
		}
		return load(clazzName, parentClazz, paraClazzes, paraObjects, alternativePackage);
	}

	public static <T> T load(final String clazzname, final Class<T> parentClazz, final Class<?>[] paraClazzes, final Object[] paraObjects)
	{
		return load(clazzname, parentClazz, paraClazzes, paraObjects, null);
	}

	@SuppressWarnings("unchecked")
	public static <T> T load(final String clazzname, final Class<T> parentClazz, final Class<?>[] paraClazzes, final Object[] paraObjects, final String alternativePackage)
	{
		Class<?> clazz = null;
		try
		{
			clazz = Class.forName(clazzname);
		}
		catch (ClassNotFoundException e)
		{
			if (alternativePackage == null)
			{
				e.printStackTrace();
				return null;
			}
			try
			{
				clazz = Class.forName(alternativePackage + "." + clazzname);
			}
			catch (ClassNotFoundException e2)
			{
				e.printStackTrace();
				return null;
			}
		}
		if (parentClazz != null)
			if (!clazz.getClass().isAssignableFrom(parentClazz))
			{
				new ClassCastException("Cannot cast " + clazz.getName() + " to " + parentClazz.getName()).printStackTrace();
				return null;
			}
		return load((Class<T>) clazz, paraClazzes, paraObjects);
	}

	public static <T> T load(final Class<T> clazz, final Class<?>[] paraClazzes, final Object[] paraObjects)
	{
		T instance = null;
		try
		{
			instance = clazz.getConstructor(paraClazzes).newInstance(paraObjects);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return instance;
	}
}
