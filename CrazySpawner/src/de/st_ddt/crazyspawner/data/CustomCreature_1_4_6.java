package de.st_ddt.crazyspawner.data;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyutil.ExtendedCreatureType;

public class CustomCreature_1_4_6 extends CustomCreature_1_4_5
{

	protected final int maxHealth;

	public CustomCreature_1_4_6(final String name, final EntityType type, final int maxHealth)
	{
		super(name, type);
		if (Damageable.class.isAssignableFrom(type.getEntityClass()))
			this.maxHealth = maxHealth;
		else
			this.maxHealth = 0;
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final int maxHealth, final String passenger)
	{
		super(name, type, passenger);
		if (Damageable.class.isAssignableFrom(type.getEntityClass()))
			this.maxHealth = maxHealth;
		else
			this.maxHealth = 0;
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final int maxHealth, final ExtendedCreatureType passenger)
	{
		super(name, type, passenger);
		if (Damageable.class.isAssignableFrom(type.getEntityClass()))
			this.maxHealth = maxHealth;
		else
			this.maxHealth = 0;
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final int maxHealth, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance)
	{
		super(name, type, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance);
		if (Damageable.class.isAssignableFrom(type.getEntityClass()))
			this.maxHealth = maxHealth;
		else
			this.maxHealth = 0;
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final int maxHealth, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final String passenger)
	{
		super(name, type, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
		if (Damageable.class.isAssignableFrom(type.getEntityClass()))
			this.maxHealth = maxHealth;
		else
			this.maxHealth = 0;
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final int maxHealth, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final ExtendedCreatureType passenger)
	{
		super(name, type, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
		if (Damageable.class.isAssignableFrom(type.getEntityClass()))
			this.maxHealth = maxHealth;
		else
			this.maxHealth = 0;
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final int maxHealth, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final String passenger)
	{
		super(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
		if (Damageable.class.isAssignableFrom(type.getEntityClass()))
			this.maxHealth = maxHealth;
		else
			this.maxHealth = 0;
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final int maxHealth, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final ExtendedCreatureType passenger)
	{
		super(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
		if (Damageable.class.isAssignableFrom(type.getEntityClass()))
			this.maxHealth = maxHealth;
		else
			this.maxHealth = 0;
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final int maxHealth, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final String passenger)
	{
		super(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
		if (Damageable.class.isAssignableFrom(type.getEntityClass()))
			this.maxHealth = maxHealth;
		else
			this.maxHealth = 0;
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final int maxHealth, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final ExtendedCreatureType passenger)
	{
		super(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
		if (Damageable.class.isAssignableFrom(type.getEntityClass()))
			this.maxHealth = maxHealth;
		else
			this.maxHealth = 0;
	}

	public CustomCreature_1_4_6(final ConfigurationSection config)
	{
		super(config);
		if (Damageable.class.isAssignableFrom(type.getEntityClass()))
			this.maxHealth = config.getInt("maxHealth", 0);
		else
			this.maxHealth = 0;
	}

	@Override
	public Entity spawn(final Location location)
	{
		final Entity entity = super.spawn(location);
		if (maxHealth > 0)
		{
			final Damageable damageable = (Damageable) entity;
			damageable.setMaxHealth(maxHealth);
			damageable.setHealth(maxHealth);
		}
		return entity;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		if (maxHealth > 0)
			config.set(path + "maxHealth", maxHealth);
	}
}
