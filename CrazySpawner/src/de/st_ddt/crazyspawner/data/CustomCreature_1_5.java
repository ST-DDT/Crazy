package de.st_ddt.crazyspawner.data;

import java.util.Map;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import de.st_ddt.crazyutil.ExtendedCreatureType;

public class CustomCreature_1_5 extends CustomCreature_1_4_6
{

	protected final String customName;
	protected final boolean showCustomName;

	public CustomCreature_1_5(final String name, final String customName, final boolean showCustomName, final EntityType type, final int maxHealth, final String passenger)
	{
		super(name, type, maxHealth, passenger);
		if (LivingEntity.class.isAssignableFrom(type.getEntityClass()))
		{
			this.customName = customName;
			this.showCustomName = showCustomName;
		}
		else
		{
			this.customName = null;
			this.showCustomName = false;
		}
	}

	public CustomCreature_1_5(final String name, final String customName, final boolean showCustomName, final EntityType type, final int maxHealth, final ExtendedCreatureType passenger)
	{
		super(name, type, maxHealth, passenger);
		if (LivingEntity.class.isAssignableFrom(type.getEntityClass()))
		{
			this.customName = customName;
			this.showCustomName = showCustomName;
		}
		else
		{
			this.customName = null;
			this.showCustomName = false;
		}
	}

	public CustomCreature_1_5(final String name, final String customName, final boolean showCustomName, final EntityType type, final int maxHealth, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance)
	{
		super(name, type, maxHealth, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance);
		if (LivingEntity.class.isAssignableFrom(type.getEntityClass()))
		{
			this.customName = customName;
			this.showCustomName = showCustomName;
		}
		else
		{
			this.customName = null;
			this.showCustomName = false;
		}
	}

	public CustomCreature_1_5(final String name, final String customName, final boolean showCustomName, final EntityType type, final int maxHealth, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final String passenger)
	{
		super(name, type, maxHealth, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
		if (LivingEntity.class.isAssignableFrom(type.getEntityClass()))
		{
			this.customName = customName;
			this.showCustomName = showCustomName;
		}
		else
		{
			this.customName = null;
			this.showCustomName = false;
		}
	}

	public CustomCreature_1_5(final String name, final String customName, final boolean showCustomName, final EntityType type, final int maxHealth, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final ExtendedCreatureType passenger)
	{
		super(name, type, maxHealth, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
		if (LivingEntity.class.isAssignableFrom(type.getEntityClass()))
		{
			this.customName = customName;
			this.showCustomName = showCustomName;
		}
		else
		{
			this.customName = null;
			this.showCustomName = false;
		}
	}

	public CustomCreature_1_5(final String name, final String customName, final boolean showCustomName, final EntityType type, final int maxHealth, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final String passenger)
	{
		super(name, type, maxHealth, baby, villager, wither, charged, color, size, angry, tamed, tamer, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
		if (LivingEntity.class.isAssignableFrom(type.getEntityClass()))
		{
			this.customName = customName;
			this.showCustomName = showCustomName;
		}
		else
		{
			this.customName = null;
			this.showCustomName = false;
		}
	}

	public CustomCreature_1_5(final String name, final String customName, final boolean showCustomName, final EntityType type, final int maxHealth, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final ExtendedCreatureType passenger)
	{
		super(name, type, maxHealth, baby, villager, wither, charged, color, size, angry, tamed, tamer, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
		if (LivingEntity.class.isAssignableFrom(type.getEntityClass()))
		{
			this.customName = customName;
			this.showCustomName = showCustomName;
		}
		else
		{
			this.customName = null;
			this.showCustomName = false;
		}
	}

	public CustomCreature_1_5(final String name, final String customName, final boolean showCustomName, final EntityType type, final int maxHealth, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final ExtendedCreatureType passenger)
	{
		super(name, type, maxHealth, baby, villager, wither, charged, color, size, angry, tamed, tamer, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
		if (LivingEntity.class.isAssignableFrom(type.getEntityClass()))
		{
			this.customName = customName;
			this.showCustomName = showCustomName;
		}
		else
		{
			this.customName = null;
			this.showCustomName = false;
		}
	}

	public CustomCreature_1_5(final String name, final String customName, final boolean showCustomName, final EntityType type, final int maxHealth, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final String passenger)
	{
		super(name, type, maxHealth, baby, villager, wither, charged, color, size, angry, tamed, tamer, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
		if (LivingEntity.class.isAssignableFrom(type.getEntityClass()))
		{
			this.customName = customName;
			this.showCustomName = showCustomName;
		}
		else
		{
			this.customName = null;
			this.showCustomName = false;
		}
	}

	public CustomCreature_1_5(final String name, final String customName, final boolean showCustomName, final EntityType type, final int maxHealth, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		super(name, type, maxHealth, baby, villager, wither, charged, color, size, angry, tamed, tamer, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger, potionEffects);
		if (LivingEntity.class.isAssignableFrom(type.getEntityClass()))
		{
			this.customName = customName;
			this.showCustomName = showCustomName;
		}
		else
		{
			this.customName = null;
			this.showCustomName = false;
		}
	}

	public CustomCreature_1_5(final ConfigurationSection config)
	{
		super(config);
		if (LivingEntity.class.isAssignableFrom(type.getEntityClass()))
		{
			this.customName = config.getString("customName");
			this.showCustomName = config.getBoolean("showCustomName");
		}
		else
		{
			this.customName = null;
			this.showCustomName = false;
		}
	}

	@Override
	public Entity spawn(final Location location)
	{
		final Entity entity = super.spawn(location);
		if (customName != null)
		{
			final LivingEntity living = (LivingEntity) entity;
			living.setCustomName(customName);
			living.setCustomNameVisible(showCustomName);
		}
		return entity;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		if (customName != null)
		{
			config.set(path + "customName", customName);
			config.set(path + "showCustomName", showCustomName);
		}
	}
}
