package de.st_ddt.crazyspawner.data;

import java.util.Collection;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import de.st_ddt.crazyspawner.data.drops.Drop;
import de.st_ddt.crazyutil.ExtendedCreatureType;
import de.st_ddt.crazyutil.paramitrisable.ExtendedCreatureParamitrisable;

public class CustomCreature_1_4_6 extends CustomCreature_1_4_5
{

	protected final int maxHealth;

	public CustomCreature_1_4_6(final String name, final EntityType type)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final String passenger)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final String passenger, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final ExtendedCreatureType passenger)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final ExtendedCreatureType passenger, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final String passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final String passenger, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ExtendedCreatureType passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ExtendedCreatureType passenger, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, (ExtendedCreatureType) null, potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final String passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final String passenger, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ExtendedCreatureType passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ExtendedCreatureType passenger, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, -1, -1, null, 0, null, 0, null, 0, null, 0, null, 0, -1, -1, passenger, potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, (ExtendedCreatureType) null, potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final String passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger), potionEffects, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final int maxHealth)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, (Map<? extends PotionEffectType, Integer>) null, maxHealth);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects, -1);
	}

	public CustomCreature_1_4_6(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final int minXP, final int maxXP, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final int minDamage, final int maxDamage, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects, final int maxHealth)
	{
		super(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, drops, minXP, maxXP, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, minDamage, maxDamage, passenger, potionEffects);
		if (Damageable.class.isAssignableFrom(type.getEntityClass()))
			this.maxHealth = maxHealth;
		else
			this.maxHealth = 0;
	}

	public CustomCreature_1_4_6(final ConfigurationSection config)
	{
		super(config);
		if (Damageable.class.isAssignableFrom(type.getEntityClass()))
			this.maxHealth = config.getInt("maxHealth", -1);
		else
			this.maxHealth = -1;
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
