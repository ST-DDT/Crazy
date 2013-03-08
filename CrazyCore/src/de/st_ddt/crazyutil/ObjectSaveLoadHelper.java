package de.st_ddt.crazyutil;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;

public class ObjectSaveLoadHelper
{

	private static DateFormat OLDDATETIMEFORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	protected ObjectSaveLoadHelper()
	{
		super();
	}

	// Location
	public static Location loadLocation(final ConfigurationSection config, World world)
	{
		if (config == null)
			return null;
		if (world == null)
		{
			world = Bukkit.getWorld(config.getString("world", ""));
			if (world == null)
				return null;
		}
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
			if (location.getWorld() != null)
				config.set(path + "world", location.getWorld().getName());
	}

	// Date
	public static Date StringToDate(final String date, final Date defaultDate)
	{
		if (date == null)
			return defaultDate;
		try
		{
			return CrazyLightPluginInterface.DATETIMEFORMAT.parse(date);
		}
		catch (final ParseException e)
		{
			try
			{
				return OLDDATETIMEFORMAT.parse(date);
			}
			catch (final ParseException e1)
			{
				return defaultDate;
			}
		}
	}

	public static String DateToString(final Date date)
	{
		return CrazyLightPluginInterface.DATETIMEFORMAT.format(date);
	}

	// ItemStack
	public static Map<Integer, ItemStack> loadInventory(final ConfigurationSection config, final Inventory inventory)
	{
		final Set<String> entries = config.getKeys(false);
		final Map<Integer, ItemStack> res = new HashMap<Integer, ItemStack>(entries.size());
		for (final String key : entries)
			try
			{
				inventory.setItem(Integer.parseInt(key), loadItemStack(config.getConfigurationSection(key)));
			}
			catch (final NumberFormatException e)
			{
				System.err.println("Failed to read Inventory Index Key. Fix your config! " + config.getCurrentPath() + "." + key);
				e.printStackTrace();
			}
		return res;
	}

	public static Map<Integer, ItemStack> loadInventory(final ConfigurationSection config)
	{
		final Set<String> entries = config.getKeys(false);
		final Map<Integer, ItemStack> res = new HashMap<Integer, ItemStack>(entries.size());
		for (final String key : entries)
			try
			{
				res.put(Integer.parseInt(key), loadItemStack(config.getConfigurationSection(key)));
			}
			catch (final NumberFormatException e)
			{
				System.err.println("Failed to read Inventory Index Key. Fix your config! " + config.getCurrentPath() + "." + key);
				e.printStackTrace();
			}
		return res;
	}

	public static void saveInventory(final ConfigurationSection config, final String path, final Inventory inventory)
	{
		for (int i = 0; i < inventory.getSize(); i++)
			saveItemStack(config, path + i + ".", inventory.getItem(i));
	}

	public static void saveInventory(final ConfigurationSection config, final String path, final Map<Integer, ItemStack> items)
	{
		for (final Entry<Integer, ItemStack> entry : items.entrySet())
			saveItemStack(config, path + entry.getKey().toString() + ".", entry.getValue());
	}

	public static ArrayList<ItemStack> loadItemStacks(final ConfigurationSection config)
	{
		if (config == null)
			return new ArrayList<ItemStack>(0);
		final Set<String> entries = config.getKeys(false);
		final ArrayList<ItemStack> res = new ArrayList<ItemStack>(entries.size());
		for (final String key : entries)
			res.add(loadItemStack(config.getConfigurationSection(key)));
		return res;
	}

	public static void saveItemStacks(final ConfigurationSection config, final String path, final Collection<ItemStack> items)
	{
		int i = 0;
		for (final ItemStack item : items)
			saveItemStack(config, path + i++ + ".", item);
	}

	public static ItemStack loadItemStack(final ConfigurationSection config)
	{
		if (config == null)
			return null;
		else
			try
			{
				return ItemStack.deserialize(config.getValues(true));
			}
			catch (final Exception e)
			{
				System.err.println("The config is messed up! Have a look at " + config.getCurrentPath());
				System.err.println("This error log may help you to find the plugin and more details what is wrong.");
				e.printStackTrace();
				return null;
			}
	}

	public static void saveItemStack(final ConfigurationSection config, final String path, final ItemStack item)
	{
		if (item == null)
			return;
		config.set(path, item.serialize());
	}

	/* Load Objects */
	/*
	 * Load a list of Objects
	 */
	public static <T> List<T> loadList(final ConfigurationSection config, final Class<T> parentClazz, final Class<?>[] paraClazzes, final Object[] paraObjects, final String alternativePackage)
	{
		final ArrayList<T> list = new ArrayList<T>();
		if (config == null)
			return list;
		for (final String key : config.getKeys(false))
			list.add(load(config.getConfigurationSection(key), parentClazz, paraClazzes, paraObjects, alternativePackage));
		return list;
	}

	public static <T> List<T> loadList(final ConfigurationSection config, final Class<T> parentClazz, final Class<?>[] paraClazzes, final Object[] paraObjects)
	{
		return loadList(config, parentClazz, paraClazzes, paraObjects, null);
	}

	public static <T> T load(final ConfigurationSection config, final Class<T> parentClazz, final Class<?>[] paraClazzes, final Object[] paraObjects)
	{
		return load(config, parentClazz, paraClazzes, paraObjects, null);
	}

	public static <T> T load(final ConfigurationSection config, final Class<T> parentClazz, final Class<?>[] paraClazzes, final Object[] paraObjects, final String alternativePackage)
	{
		final String clazzName = config.getString("type", "-1");
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

	public static <T> T load(final String clazzname, final Class<T> parentClazz, final Class<?>[] paraClazzes, final Object[] paraObjects, final String alternativePackage)
	{
		Class<? extends T> clazz = null;
		try
		{
			clazz = Class.forName(clazzname).asSubclass(parentClazz);
		}
		catch (final ClassNotFoundException e)
		{
			if (alternativePackage == null)
			{
				e.printStackTrace();
				return null;
			}
			try
			{
				clazz = Class.forName(alternativePackage + "." + clazzname).asSubclass(parentClazz);
			}
			catch (final ClassNotFoundException e2)
			{
				e.printStackTrace();
				return null;
			}
		}
		return load(clazz, paraClazzes, paraObjects);
	}

	public static <T> T load(final Class<? extends T> clazz, final Class<?>[] paraClazzes, final Object[] paraObjects)
	{
		try
		{
			return clazz.getConstructor(paraClazzes).newInstance(paraObjects);
		}
		catch (final InvocationTargetException e)
		{
			ChatHelper.shortPrintStackTrace(e, e.getCause(), "ObjectSaveLoadHelper (" + clazz.getName() + ")");
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
