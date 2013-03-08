package de.st_ddt.crazyspawner.data;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;

import de.st_ddt.crazyutil.ExtendedCreatureType;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.paramitrisable.ExtendedCreatureParamitrisable;

public class CustomCreature_146 implements CustomCreature
{

	private final String name;
	private final EntityType type;
	private final boolean baby;
	private final boolean villager;
	private final boolean wither;
	private final boolean charged;
	private final DyeColor color;
	private final int size;
	private final boolean angry;
	private final boolean tamed;
	private final OfflinePlayer tamer;
	private final int maxHealth;
	private final boolean equiped;
	private final ItemStack boots;
	private final float bootsDropChance;
	private final ItemStack leggings;
	private final float leggingsDropChance;
	private final ItemStack chestplate;
	private final float chestplateDropChance;
	private final ItemStack helmet;
	private final float helmetDropChance;
	private final ItemStack itemInHand;
	private final float itemInHandDropChance;
	private final ExtendedCreatureType passenger;

	public CustomCreature_146(final String name, final EntityType type, final String passenger)
	{
		this(name, type, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger));
	}

	public CustomCreature_146(final String name, final EntityType type, final ExtendedCreatureType passenger)
	{
		this(name, type, null, 0, null, 0, null, 0, null, 0, null, 0, passenger);
	}

	public CustomCreature_146(final String name, final EntityType type, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance)
	{
		this(name, type, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, (ExtendedCreatureType) null);
	}

	public CustomCreature_146(final String name, final EntityType type, final int maxHealth, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, maxHealth, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, (ExtendedCreatureType) null);
	}

	public CustomCreature_146(final String name, final EntityType type, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final String passenger)
	{
		this(name, type, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger));
	}

	public CustomCreature_146(final String name, final EntityType type, final int maxHealth, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final String passenger)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, maxHealth, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger));
	}

	public CustomCreature_146(final String name, final EntityType type, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final ExtendedCreatureType passenger)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, 0, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
	}

	public CustomCreature_146(final String name, final EntityType type, final int maxHealth, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final ExtendedCreatureType passenger)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, maxHealth, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
	}

	public CustomCreature_146(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final int maxHealth, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final String passenger)
	{
		this(name, type, baby, villager, wither, charged, color, 0, angry, tamed, tamer, 0, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger));
	}

	public CustomCreature_146(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final int maxHealth, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final ExtendedCreatureType passenger)
	{
		this(name, type, baby, villager, wither, charged, color, 0, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), 0, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
	}

	public CustomCreature_146(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final int maxHealth, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final String passenger)
	{
		this(name, type, baby, villager, wither, charged, color, 0, angry, tamed, tamer, 0, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger));
	}

	public CustomCreature_146(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final int maxHealth, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final ExtendedCreatureType passenger)
	{
		super();
		if (name == null)
			throw new IllegalArgumentException("Name cannot be null!");
		this.name = name.toUpperCase();
		this.type = type;
		if (type == null)
			throw new IllegalArgumentException("Type cannot be null!");
		final Class<? extends Entity> clazz = type.getEntityClass();
		this.baby = baby && (Ageable.class.isAssignableFrom(clazz) || Zombie.class.isAssignableFrom(clazz));
		this.villager = villager && Zombie.class.isAssignableFrom(clazz);
		this.wither = wither && Skeleton.class.isAssignableFrom(clazz);
		this.charged = charged && Creeper.class.isAssignableFrom(clazz);
		this.color = Colorable.class.isAssignableFrom(clazz) ? color : null;
		this.size = Slime.class.isAssignableFrom(clazz) ? size : 0;
		this.angry = angry && Wolf.class.isAssignableFrom(clazz);
		if (Tameable.class.isAssignableFrom(clazz))
		{
			this.tamer = tamer;
			this.tamed = tamer != null || tamed;
		}
		else
		{
			this.tamer = null;
			this.tamed = false;
		}
		this.maxHealth = Damageable.class.isAssignableFrom(clazz) ? maxHealth : 0;
		if (LivingEntity.class.isAssignableFrom(clazz))
		{
			this.equiped = boots != null || leggings != null || chestplate != null || helmet != null || itemInHand != null;
			this.boots = boots;
			this.bootsDropChance = bootsDropChance;
			this.leggings = leggings;
			this.leggingsDropChance = leggingsDropChance;
			this.chestplate = chestplate;
			this.chestplateDropChance = chestplateDropChance;
			this.helmet = helmet;
			this.helmetDropChance = helmetDropChance;
			this.itemInHand = itemInHand;
			this.itemInHandDropChance = itemInHandDropChance;
		}
		else
		{
			this.equiped = false;
			this.boots = null;
			this.bootsDropChance = 0;
			this.leggings = null;
			this.leggingsDropChance = 0;
			this.chestplate = null;
			this.chestplateDropChance = 0;
			this.helmet = null;
			this.helmetDropChance = 0;
			this.itemInHand = null;
			this.itemInHandDropChance = 0;
		}
		this.passenger = passenger;
	}

	public CustomCreature_146(final ConfigurationSection config)
	{
		super();
		this.name = config.getString("name", "").toUpperCase();
		if (name.length() == 0)
			throw new IllegalArgumentException("Name cannot be null!");
		this.type = EntityType.valueOf(config.getString("type", ""));
		if (type == null)
			throw new IllegalArgumentException("Type cannot be null!");
		final Class<? extends Entity> clazz = type.getEntityClass();
		this.baby = (Ageable.class.isAssignableFrom(clazz) || Zombie.class.isAssignableFrom(clazz)) && config.getBoolean("baby");
		this.villager = Zombie.class.isAssignableFrom(clazz) && config.getBoolean("villager");
		this.wither = Skeleton.class.isAssignableFrom(clazz) && config.getBoolean("wither");
		this.charged = Creeper.class.isAssignableFrom(clazz) && config.getBoolean("charged");
		this.color = Colorable.class.isAssignableFrom(clazz) ? DyeColor.valueOf(config.getString("color", "WHITE")) : null;
		this.size = Slime.class.isAssignableFrom(clazz) ? config.getInt("size") : 0;
		this.angry = Wolf.class.isAssignableFrom(clazz) && config.getBoolean("angry");
		if (Tameable.class.isAssignableFrom(clazz))
		{
			final String tamer = config.getString("tamer");
			if (tamer == null)
				this.tamer = null;
			else
				this.tamer = Bukkit.getOfflinePlayer(tamer);
			this.tamed = tamer != null || config.getBoolean("tamed");
		}
		else
		{
			this.tamer = null;
			this.tamed = false;
		}
		this.maxHealth = Damageable.class.isAssignableFrom(clazz) ? config.getInt("maxHealth") : 0;
		if (LivingEntity.class.isAssignableFrom(clazz))
		{
			this.boots = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("boots"));
			this.bootsDropChance = (float) config.getDouble("bootsDropChance");
			this.leggings = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("leggings"));
			this.leggingsDropChance = (float) config.getDouble("leggingsDropChance");
			this.chestplate = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("chestplate"));
			this.chestplateDropChance = (float) config.getDouble("chestplateDropChance");
			this.helmet = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("helmet"));
			this.helmetDropChance = (float) config.getDouble("helmetDropChance");
			this.itemInHand = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("itemInHand"));
			this.itemInHandDropChance = (float) config.getDouble("itemInHandDropChance");
			this.equiped = boots != null || leggings != null || chestplate != null || helmet != null || itemInHand != null;
		}
		else
		{
			this.equiped = false;
			this.boots = null;
			this.bootsDropChance = 0;
			this.leggings = null;
			this.leggingsDropChance = 0;
			this.chestplate = null;
			this.chestplateDropChance = 0;
			this.helmet = null;
			this.helmetDropChance = 0;
			this.itemInHand = null;
			this.itemInHandDropChance = 0;
		}
		final String passenger = config.getString("passenger");
		if (passenger == null)
			this.passenger = null;
		else
			this.passenger = ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public EntityType getType()
	{
		return type;
	}

	@Override
	public Entity spawn(final Location location)
	{
		final Entity entity = location.getWorld().spawnEntity(location, type);
		try
		{
			if (baby)
				if (entity instanceof Ageable)
					((Ageable) entity).setBaby();
				else if (entity instanceof Zombie)
					((Zombie) entity).setBaby(true);
			if (villager)
				((Zombie) entity).setVillager(true);
			if (wither)
				((Skeleton) entity).setSkeletonType(SkeletonType.WITHER);
			if (charged)
				((Creeper) entity).setPowered(true);
			if (color != null)
				((Colorable) entity).setColor(color);
			if (size > 0 && size < 5)
				((Slime) entity).setSize(size);
			if (angry)
				((Wolf) entity).setAngry(true);
			if (tamed)
			{
				final Tameable tameable = (Tameable) entity;
				if (tamer == null)
					tameable.setTamed(true);
				else
					tameable.setOwner(tamer);
			}
			if (maxHealth > 0)
			{
				final Damageable damageable = (Damageable) entity;
				damageable.setMaxHealth(maxHealth);
				damageable.setHealth(maxHealth);
			}
			if (equiped)
			{
				final EntityEquipment equipment = ((LivingEntity) entity).getEquipment();
				equipment.setBoots(boots.clone());
				equipment.setBootsDropChance(bootsDropChance);
				equipment.setLeggings(leggings.clone());
				equipment.setLeggingsDropChance(leggingsDropChance);
				equipment.setChestplate(chestplate.clone());
				equipment.setChestplateDropChance(chestplateDropChance);
				equipment.setHelmet(helmet.clone());
				equipment.setHelmetDropChance(helmetDropChance);
				equipment.setItemInHand(itemInHand.clone());
				equipment.setItemInHandDropChance(itemInHandDropChance);
			}
		}
		catch (final ClassCastException e)
		{
			System.err.println("Could not properly spawn " + name);
			System.err.println(e.getMessage());
		}
		if (passenger != null)
			entity.setPassenger(passenger.spawn(location));
		return entity;
	}

	@Override
	public Collection<? extends Entity> getEntities(final World world)
	{
		return world.getEntitiesByClass(type.getEntityClass());
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "name", name);
		config.set(path + "type", type.name());
		if (baby)
			config.set(path + "baby", true);
		if (villager)
			config.set(path + "villager", true);
		if (wither)
			config.set(path + "wither", true);
		if (charged)
			config.set(path + "charged", true);
		if (color != null)
			config.set(path + "color", color.name());
		if (size > 0 && size < 5)
			config.set(path + "size", size);
		if (angry)
			config.set(path + "angry", true);
		if (tamed)
			if (tamer == null)
				config.set(path + "tamed", true);
			else
				config.set(path + "tamer", tamer.getName());
		if (maxHealth > 0)
			config.set(path + "maxHealth", maxHealth);
		if (equiped)
		{
			if (boots != null)
				config.set(path + "boots", boots.serialize());
			config.set(path + "bootsDropChance", bootsDropChance);
			if (leggings != null)
				config.set(path + "leggings", leggings.serialize());
			config.set(path + "leggingsDropChance", leggingsDropChance);
			if (chestplate != null)
				config.set(path + "chestplate", chestplate.serialize());
			config.set(path + "chestplateDropChance", chestplateDropChance);
			if (helmet != null)
				config.set(path + "helmet", helmet.serialize());
			config.set(path + "helmetDropChance", helmetDropChance);
			if (itemInHand != null)
				config.set(path + "itemInHand", itemInHand.serialize());
			config.set(path + "itemInHandDropChance", itemInHandDropChance);
		}
		if (passenger != null)
			config.set(path + "passenger", passenger.getName());
	}

	public static void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "name", "String");
		config.set(path + "type", "EntityType");
		config.set(path + "baby", "boolean");
		config.set(path + "villager", "boolean");
		config.set(path + "wither", "boolean");
		config.set(path + "charged", "boolean");
		config.set(path + "color", "DyeColor");
		config.set(path + "size", "int (1-4)");
		config.set(path + "angry", "boolean");
		config.set(path + "tamed", "boolean");
		config.set(path + "tamer", "Player");
		config.set(path + "maxHealth", "int");
		config.set(path + "boots", "Item");
		config.set(path + "bootsDropChance", "float (0-1)");
		config.set(path + "leggings", "Item");
		config.set(path + "leggingsDropChance", "float (0-1)");
		config.set(path + "chestplate", "Item");
		config.set(path + "chestplateDropChance", "float (0-1)");
		config.set(path + "helmet", "Item");
		config.set(path + "helmetDropChance", "float (0-1)");
		config.set(path + "itemInHand", "Item");
		config.set(path + "itemInHandDropChance", "float (0-1)");
		config.set(path + "passenger", "ExtendedCreatureType");
	}
}
