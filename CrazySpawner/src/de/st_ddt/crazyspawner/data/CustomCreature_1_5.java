package de.st_ddt.crazyspawner.data;

import java.util.Collection;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import de.st_ddt.crazyspawner.data.drops.Drop;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ExtendedCreatureType;
import de.st_ddt.crazyutil.paramitrisable.ExtendedCreatureParamitrisable;

public class CustomCreature_1_5 extends CustomCreature_1_4_6
{

	protected final String customName;
	protected final boolean showCustomName;

	public CustomCreature_1_5(final String name, final EntityType type)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final String passenger)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final String passenger, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final String passenger, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final String passenger, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final ExtendedCreatureType passenger)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final ExtendedCreatureType passenger, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final ExtendedCreatureType passenger, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final ExtendedCreatureType passenger, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final String passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final String passenger, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final String passenger, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final String passenger, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ExtendedCreatureType passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ExtendedCreatureType passenger, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ExtendedCreatureType passenger, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ExtendedCreatureType passenger, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final String passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final String passenger, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final String passenger, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final String passenger, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ExtendedCreatureType passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ExtendedCreatureType passenger, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ExtendedCreatureType passenger, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ExtendedCreatureType passenger, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final int maxHealth, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, -1, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final String customName, final boolean showCustomName)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, -1, customName, showCustomName);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, maxHealth, null, false);
	}

	public CustomCreature_1_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth, final String customName, final boolean showCustomName)
	{
		super(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, maxHealth);
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
			this.customName = ChatHelper.colorise(config.getString("customName"));
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
			config.set(path + "customName", ChatHelper.decolorise(customName));
			config.set(path + "showCustomName", showCustomName);
		}
	}
}
