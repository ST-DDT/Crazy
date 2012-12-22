package de.st_ddt.crazyutil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;

public final class ObjectSaveLoadHelper
{

	private static DateFormat OLDDATETIMEFORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	private ObjectSaveLoadHelper()
	{
	}

	// Location
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
	public static HashMap<Integer, ItemStack> loadInventory(final ConfigurationSection config)
	{
		final Set<String> entries = config.getKeys(false);
		final HashMap<Integer, ItemStack> res = new HashMap<Integer, ItemStack>(entries.size());
		for (final String key : entries)
			res.put(Integer.parseInt(key), loadItemStack(config.getConfigurationSection(key)));
		return res;
	}

	public static void saveInventory(final ConfigurationSection config, final String path, final HashMap<Integer, ItemStack> items)
	{
		for (final Entry<Integer, ItemStack> entry : items.entrySet())
			saveItemStack(config, path + entry.getKey().toString() + ".", entry.getValue());
	}

	public static ArrayList<ItemStack> loadItemStacks(final ConfigurationSection config)
	{
		final Set<String> entries = config.getKeys(false);
		final ArrayList<ItemStack> res = new ArrayList<ItemStack>(entries.size());
		for (final String key : entries)
			res.add(loadItemStack(config.getConfigurationSection(key)));
		return res;
	}

	public static void saveItemStacks(final ConfigurationSection config, final String path, final Collection<ItemStack> items)
	{
		final int i = 0;
		final Iterator<ItemStack> it = items.iterator();
		while (it.hasNext())
			saveItemStack(config, path + i + ".", it.next());
	}

	public static ItemStack loadItemStack(final ConfigurationSection config)
	{
		final ItemStack item = new MaterialData(config.getInt("id"), (byte) config.getInt("data")).toItemStack(config.getInt("amount", 1));
		if (config.contains("durability"))
			item.setDurability((short) config.getInt("durability"));
		if (config.contains("enchantments"))
			item.addEnchantments(loadEnchantments(config.getConfigurationSection("enchantments")));
		return item;
	}

	public static void saveItemStack(final ConfigurationSection config, final String path, final ItemStack item)
	{
		saveItemStack(config, path, item, true, true);
	}

	public static void saveItemStack(final ConfigurationSection config, final String path, final ItemStack item, final boolean includeDurability)
	{
		saveItemStack(config, path, item, includeDurability, true);
	}

	public static void saveItemStack(final ConfigurationSection config, final String path, final ItemStack item, final boolean includeDurability, final boolean includeEnchantments)
	{
		if (item == null)
			return;
		config.set(path + "id", item.getTypeId());
		config.set(path + "data", item.getData().getData());
		config.set(path + "amount", item.getAmount());
		if (includeDurability)
			config.set(path + "durability", item.getDurability());
		else
			config.set(path + "durability", null);
		config.set(path + "enchantments", null);
		if (includeEnchantments)
			saveEnchantments(config, path + "enchantments.", item);
	}

	// Enchantments
	public static Map<Enchantment, Integer> loadEnchantments(final ConfigurationSection config)
	{
		final Map<Enchantment, Integer> map = new LinkedHashMap<Enchantment, Integer>();
		for (final String name : config.getKeys(false))
			map.put(Enchantment.getByName(name), config.getInt(name));
		return map;
	}

	public static void saveEnchantments(final ConfigurationSection config, final String path, final ItemStack item)
	{
		saveEnchantments(config, path, item.getEnchantments());
	}

	public static void saveEnchantments(final ConfigurationSection config, final String path, final Map<Enchantment, Integer> enchantments)
	{
		for (final Entry<Enchantment, Integer> entry : enchantments.entrySet())
			config.set(path + entry.getKey().getName(), entry.getValue());
	}

	/* Load Objects */
	/*
	 * Load a list of Objects
	 */
	public static <T> List<T> loadList(final ConfigurationSection config, final Class<T> parentClazz, final Class<?>[] paraClazzes, final Object[] paraObjects, final String alternativePackage)
	{
		return loadList(config, parentClazz, paraClazzes, paraObjects, null);
	}

	public static <T> List<T> loadList(final ConfigurationSection config, final Class<T> parentClazz, final Class<?>[] paraClazzes, final Object[] paraObjects)
	{
		final ArrayList<T> list = new ArrayList<T>();
		if (config == null)
			return list;
		for (final String key : config.getKeys(false))
			list.add(load(config.getConfigurationSection(key), parentClazz, paraClazzes, paraObjects));
		return list;
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
		T instance = null;
		try
		{
			instance = clazz.getConstructor(paraClazzes).newInstance(paraObjects);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return instance;
	}
}
