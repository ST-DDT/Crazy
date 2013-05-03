package de.st_ddt.crazyspawner.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.st_ddt.crazyspawner.data.drops.Drop;
import de.st_ddt.crazyspawner.data.meta.CustomCreatureMeta;
import de.st_ddt.crazyspawner.data.meta.DropsMeta;
import de.st_ddt.crazyutil.ExtendedCreatureType;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.paramitrisable.ExtendedCreatureParamitrisable;

public class CustomCreature_1_4_5 implements CustomCreature
{

	private final static int POTIONDURATION = 20 * 60 * 60 * 24;
	private final static Comparator<PotionEffectType> POTIONEFFECTTYPE_COMCOMPARATOR = new Comparator<PotionEffectType>()
	{

		@Override
		public int compare(final PotionEffectType o1, final PotionEffectType o2)
		{
			return o1.getName().compareTo(o2.getName());
		}
	};
	protected final CustomCreatureMeta meta = new CustomCreatureMeta(this);
	protected final String name;
	protected final EntityType type;
	protected final boolean baby;
	protected final boolean villager;
	protected final boolean wither;
	protected final boolean charged;
	protected final DyeColor color;
	protected final int size;
	protected final boolean angry;
	protected final boolean tamed;
	protected final OfflinePlayer tamer;
	protected final DropsMeta dropsMeta = new DropsMeta(this);
	protected final List<Drop> drops = new ArrayList<Drop>();
	protected final boolean equiped;
	protected final ItemStack boots;
	protected final float bootsDropChance;
	protected final ItemStack leggings;
	protected final float leggingsDropChance;
	protected final ItemStack chestplate;
	protected final float chestplateDropChance;
	protected final ItemStack helmet;
	protected final float helmetDropChance;
	protected final ItemStack itemInHand;
	protected final float itemInHandDropChance;
	protected final ExtendedCreatureType passenger;
	protected final Map<PotionEffectType, Integer> potionEffects = new TreeMap<PotionEffectType, Integer>(POTIONEFFECTTYPE_COMCOMPARATOR);

	public CustomCreature_1_4_5(final String name, final EntityType type)
	{
		this(name, type, (ExtendedCreatureType) null);
	}

	public CustomCreature_1_4_5(final String name, final EntityType type, final String passenger)
	{
		this(name, type, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger));
	}

	public CustomCreature_1_4_5(final String name, final EntityType type, final ExtendedCreatureType passenger)
	{
		this(name, type, null, 0, null, 0, null, 0, null, 0, null, 0, passenger);
	}

	public CustomCreature_1_4_5(final String name, final EntityType type, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance)
	{
		this(name, type, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, (ExtendedCreatureType) null);
	}

	public CustomCreature_1_4_5(final String name, final EntityType type, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final String passenger)
	{
		this(name, type, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger));
	}

	public CustomCreature_1_4_5(final String name, final EntityType type, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final ExtendedCreatureType passenger)
	{
		this(name, type, false, false, false, false, null, 0, false, false, (OfflinePlayer) null, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
	}

	public CustomCreature_1_4_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final String passenger)
	{
		this(name, type, baby, villager, wither, charged, color, 0, angry, tamed, tamer, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger));
	}

	public CustomCreature_1_4_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final String tamer, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final ExtendedCreatureType passenger)
	{
		this(name, type, baby, villager, wither, charged, color, 0, angry, tamed, tamer == null ? null : Bukkit.getOfflinePlayer(tamer), boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger);
	}

	public CustomCreature_1_4_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final String passenger)
	{
		this(name, type, baby, villager, wither, charged, color, 0, angry, tamed, tamer, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger));
	}

	public CustomCreature_1_4_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final ExtendedCreatureType passenger)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger, null);
	}

	public CustomCreature_1_4_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
	{
		this(name, type, baby, villager, wither, charged, color, size, angry, tamed, tamer, null, boots, bootsDropChance, leggings, leggingsDropChance, chestplate, chestplateDropChance, helmet, helmetDropChance, itemInHand, itemInHandDropChance, passenger, potionEffects);
	}

	public CustomCreature_1_4_5(final String name, final EntityType type, final boolean baby, final boolean villager, final boolean wither, final boolean charged, final DyeColor color, final int size, final boolean angry, final boolean tamed, final OfflinePlayer tamer, final Collection<Drop> drops, final ItemStack boots, final float bootsDropChance, final ItemStack leggings, final float leggingsDropChance, final ItemStack chestplate, final float chestplateDropChance, final ItemStack helmet, final float helmetDropChance, final ItemStack itemInHand, final float itemInHandDropChance, final ExtendedCreatureType passenger, final Map<? extends PotionEffectType, Integer> potionEffects)
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
		this.angry = (Wolf.class.isAssignableFrom(clazz) || PigZombie.class.isAssignableFrom(clazz)) && angry;
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
		if (LivingEntity.class.isAssignableFrom(clazz))
		{
			if (drops == null || drops.contains(null))
				this.drops.add(null);
			else
				this.drops.addAll(drops);
			this.equiped = boots != null || leggings != null || chestplate != null || helmet != null || itemInHand != null;
			this.boots = boots;
			this.bootsDropChance = this.drops.contains(null) ? bootsDropChance : 0;
			this.leggings = leggings;
			this.leggingsDropChance = this.drops.contains(null) ? leggingsDropChance : 0;
			this.chestplate = chestplate;
			this.chestplateDropChance = this.drops.contains(null) ? chestplateDropChance : 0;
			this.helmet = helmet;
			this.helmetDropChance = this.drops.contains(null) ? helmetDropChance : 0;
			this.itemInHand = itemInHand;
			this.itemInHandDropChance = this.drops.contains(null) ? itemInHandDropChance : 0;
			if (potionEffects != null)
				this.potionEffects.putAll(potionEffects);
		}
		else
		{
			this.drops.add(null);
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

	public CustomCreature_1_4_5(final ConfigurationSection config)
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
		this.angry = (Wolf.class.isAssignableFrom(clazz) || PigZombie.class.isAssignableFrom(clazz)) && config.getBoolean("angry");
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
		if (LivingEntity.class.isAssignableFrom(clazz))
		{
			final ConfigurationSection dropsConfig = config.getConfigurationSection("drops");
			if (dropsConfig == null)
				this.drops.add(null);
			else
				for (final String key : dropsConfig.getKeys(false))
					try
					{
						this.drops.add(new Drop(dropsConfig.getConfigurationSection(key)));
					}
					catch (final Exception e)
					{
						System.err.println(name + "'s costum drop " + key + " was corrupted and has been removed!");
					}
			this.boots = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("boots"));
			this.bootsDropChance = this.drops.contains(null) ? ((float) config.getDouble("bootsDropChance")) : 0;
			this.leggings = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("leggings"));
			this.leggingsDropChance = this.drops.contains(null) ? ((float) config.getDouble("leggingsDropChance")) : 0;
			this.chestplate = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("chestplate"));
			this.chestplateDropChance = this.drops.contains(null) ? ((float) config.getDouble("chestplateDropChance")) : 0;
			this.helmet = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("helmet"));
			this.helmetDropChance = this.drops.contains(null) ? ((float) config.getDouble("helmetDropChance")) : 0;
			this.itemInHand = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("itemInHand"));
			this.itemInHandDropChance = this.drops.contains(null) ? ((float) config.getDouble("itemInHandDropChance")) : 0;
			this.equiped = boots != null || leggings != null || chestplate != null || helmet != null || itemInHand != null;
			final ConfigurationSection potionConfig = config.getConfigurationSection("potionEffects");
			if (potionConfig != null)
				for (final String key : potionConfig.getKeys(false))
				{
					final PotionEffectType type = PotionEffectType.getByName(key);
					if (type == null)
						continue;
					potionEffects.put(type, potionConfig.getInt(key, 1));
				}
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
			this.drops.add(null);
		}
		final String passenger = config.getString("passenger");
		if (passenger == null)
			this.passenger = null;
		else
			this.passenger = ExtendedCreatureParamitrisable.getExtendedCreatureType(passenger);
	}

	@Override
	public final String getName()
	{
		return name;
	}

	@Override
	public final EntityType getType()
	{
		return type;
	}

	@Override
	public Entity spawn(final Location location)
	{
		final Entity entity = location.getWorld().spawnEntity(location, type);
		entity.setMetadata(CustomCreatureMeta.METAHEADER, meta);
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
			if (size > 0)
				((Slime) entity).setSize(size);
			if (angry)
				if (entity instanceof Wolf)
					((Wolf) entity).setAngry(true);
				else
					((PigZombie) entity).setAngry(true);
			if (tamed)
			{
				final Tameable tameable = (Tameable) entity;
				if (tamer == null)
					tameable.setTamed(true);
				else
					tameable.setOwner(tamer);
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
			if (!drops.contains(null))
				entity.setMetadata(CustomCreatureMeta.METAHEADER, meta);
			if (potionEffects.size() > 0)
			{
				final LivingEntity living = (LivingEntity) entity;
				for (final Entry<PotionEffectType, Integer> entry : potionEffects.entrySet())
					living.addPotionEffect(new PotionEffect(entry.getKey(), POTIONDURATION, entry.getValue()));
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
		if (size > 0)
			config.set(path + "size", size);
		if (angry)
			config.set(path + "angry", true);
		if (tamed)
			if (tamer == null)
				config.set(path + "tamed", true);
			else
				config.set(path + "tamer", tamer.getName());
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
		if (drops.isEmpty())
			config.set(path + "drops", new HashMap<String, Drop>());
		else if (!drops.contains(null))
		{
			int i = 1;
			for (final Drop drop : drops)
				drop.save(config, path + "drops." + drop.getItem().getType().name() + i++ + ".");
		}
		if (passenger != null)
			config.set(path + "passenger", passenger.getName());
		if (potionEffects.size() > 0)
			for (final Entry<PotionEffectType, Integer> entry : potionEffects.entrySet())
				config.set(path + "potionEffects." + entry.getKey().getName(), entry.getValue());
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
		config.set(path + "size", "int (1-x)");
		config.set(path + "angry", "boolean");
		config.set(path + "tamed", "boolean");
		config.set(path + "tamer", "Player");
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
		config.set(path + "drops.DROP1.item", "Item");
		config.set(path + "drops.DROP1.chance", "float (0-1)");
		config.set(path + "drops.DROP2.item", "Item");
		config.set(path + "drops.DROP2.chance", "float (0-1)");
		config.set(path + "drops.DROPx.item", "Item");
		config.set(path + "drops.DROPx.chance", "float (0-1)");
		config.set(path + "passenger", "ExtendedCreatureType");
		config.set(path + "maxHealth", "int");
		config.set(path + "customName", "String");
		config.set(path + "showCustomName", "boolean");
		config.set(path + "potionEffects.POTIONEFFECT1", "int (1-x)");
		config.set(path + "potionEffects.POTIONEFFECT2", "int (1-x)");
		config.set(path + "potionEffects.POTIONEFFECTx", "int (1-x)");
	}

	@Override
	public List<ItemStack> getDrops()
	{
		return updateDrops(new ArrayList<ItemStack>());
	}

	@Override
	public <S extends Collection<ItemStack>> S updateDrops(final S collection)
	{
		if (drops.contains(null))
			return collection;
		try
		{
			collection.clear();
			for (final Drop drop : drops)
				if (drop.checkChance())
					collection.add(drop.getItemClone());
		}
		catch (final UnsupportedOperationException e)
		{}
		return collection;
	}

	@Override
	public final int hashCode()
	{
		return name.hashCode();
	}

	@Override
	public final String toString()
	{
		return "CustomCreature{Name: " + name + ", Type: " + type.getName() + "}";
	}
}
