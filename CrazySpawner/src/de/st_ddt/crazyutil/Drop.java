package de.st_ddt.crazyutil;

import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class Drop implements ConfigurationSaveable
{

	private static final Random random = new Random();
	private final ItemStack item;
	private final float chance;

	public Drop(final ItemStack item, final float chance)
	{
		super();
		this.item = item;
		Validate.notNull(item, "Item cannot be null!");
		this.chance = chance;
	}

	public Drop(final ConfigurationSection config)
	{
		super();
		this.item = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("item"));
		Validate.notNull(item, "Item cannot be null!");
		this.chance = (float) config.getDouble("chance", 0);
	}

	public boolean checkChance()
	{
		return random.nextFloat() <= chance;
	}

	public ItemStack getItem()
	{
		return item;
	}

	public ItemStack getItemClone()
	{
		return item.clone();
	}

	public float getChance()
	{
		return chance;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		ObjectSaveLoadHelper.saveItemStack(config, path + "item", item);
		config.set(path + "chance", chance);
	}

	@Override
	public String toString()
	{
		return "Drop {Item: " + item.toString() + ", Chance: " + chance + "}";
	}
}
