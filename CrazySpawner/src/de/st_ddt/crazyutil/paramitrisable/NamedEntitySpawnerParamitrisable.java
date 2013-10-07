package de.st_ddt.crazyutil.paramitrisable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.NamedEntitySpawner;

public class NamedEntitySpawnerParamitrisable extends TypedParamitrisable<NamedEntitySpawner>
{

	public final static Map<String, NamedEntitySpawner> ENTITY_TYPES = new TreeMap<String, NamedEntitySpawner>(String.CASE_INSENSITIVE_ORDER);

	public static void registerNamedEntitySpawner(final NamedEntitySpawner entitySpawner, final String... aliases)
	{
		if (entitySpawner == null)
			throw new IllegalArgumentException("EntitySpawner cannot be null!");
		if (entitySpawner.getName() == null)
			throw new IllegalArgumentException("EntitySpawner's name cannot be null (" + entitySpawner + ")!");
		ENTITY_TYPES.put(entitySpawner.getName().toUpperCase(), entitySpawner);
		for (final String alias : aliases)
			if (alias != null)
				ENTITY_TYPES.put(alias.toUpperCase(), entitySpawner);
	}

	public static NamedEntitySpawner getNamedEntitySpawner(final String name)
	{
		if (name == null)
			return null;
		else
			return ENTITY_TYPES.get(name.toUpperCase());
	}

	public static List<NamedEntitySpawner> getNamedEntitySpawnerList(final Collection<String> names)
	{
		if (names == null)
			return new ArrayList<NamedEntitySpawner>(0);
		final List<NamedEntitySpawner> res = new ArrayList<NamedEntitySpawner>(names.size());
		for (final String name : names)
			res.add(getNamedEntitySpawner(name));
		return res;
	}

	static
	{
		for (final EntityType type : EntityType.values())
			if (type.isAlive() && type.isSpawnable())
			{
				registerNamedEntitySpawner(new DefaultNamedEntitySpawner(type), type.toString());
				if (Ageable.class.isAssignableFrom(type.getEntityClass()))
					registerNamedEntitySpawner(new BabyNamedEntitySpawner(type));
			}
		for (final Profession profession : Profession.values())
			registerNamedEntitySpawner(new VillagerNamedEntitySpawner(profession));
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.ZOMBIE)
		{

			@Override
			public String getName()
			{
				return "BABY_ZOMBIE";
			}

			@Override
			public Zombie spawn(final Location location)
			{
				final Zombie zombie = (Zombie) super.spawn(location);
				zombie.setBaby(true);
				return zombie;
			}
		});
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.ZOMBIE)
		{

			@Override
			public String getName()
			{
				return "ZOMBIE_VILLAGER";
			}

			@Override
			public Zombie spawn(final Location location)
			{
				final Zombie zombie = (Zombie) super.spawn(location);
				zombie.setVillager(true);
				return zombie;
			}
		});
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.ZOMBIE)
		{

			@Override
			public String getName()
			{
				return "BABY_ZOMBIE_VILLAGER";
			}

			@Override
			public Zombie spawn(final Location location)
			{
				final Zombie zombie = (Zombie) super.spawn(location);
				zombie.setVillager(true);
				zombie.setBaby(true);
				return zombie;
			}
		});
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.SKELETON)
		{

			@Override
			public String getName()
			{
				return "NORMALSKELETON";
			}

			@Override
			public Skeleton spawn(final Location location)
			{
				final Skeleton skeleton = (Skeleton) super.spawn(location);
				skeleton.setSkeletonType(SkeletonType.NORMAL);
				return skeleton;
			}
		});
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.SKELETON)
		{

			@Override
			public String getName()
			{
				return "WITHERSKELETON";
			}

			@Override
			public Skeleton spawn(final Location location)
			{
				final Skeleton skeleton = (Skeleton) super.spawn(location);
				skeleton.setSkeletonType(SkeletonType.WITHER);
				return skeleton;
			}
		});
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.CREEPER)
		{

			@Override
			public String getName()
			{
				return "UNPOWEREDCREEPER";
			}

			@Override
			public Creeper spawn(final Location location)
			{
				final Creeper creeper = (Creeper) super.spawn(location);
				creeper.setPowered(false);
				return creeper;
			}
		}, "UNCHARGEDCREEPER");
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.CREEPER)
		{

			@Override
			public String getName()
			{
				return "POWEREDCREEPER";
			}

			@Override
			public Creeper spawn(final Location location)
			{
				final Creeper creeper = (Creeper) super.spawn(location);
				creeper.setPowered(true);
				return creeper;
			}
		}, "POWEREDCREEPER", "CHARGEDCREEPER");
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.WOLF)
		{

			@Override
			public String getName()
			{
				return "ANGRY_WOLF";
			}

			@Override
			public Wolf spawn(final Location location)
			{
				final Wolf wolf = (Wolf) super.spawn(location);
				wolf.setAngry(true);
				return wolf;
			}
		}, "ANGRYWOLF");
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.PIG_ZOMBIE)
		{

			@Override
			public String getName()
			{
				return "ANGRY_PIG_ZOMBIE";
			}

			@Override
			public PigZombie spawn(final Location location)
			{
				final PigZombie pigzombie = (PigZombie) super.spawn(location);
				pigzombie.setAngry(true);
				return pigzombie;
			}
		}, "ANGRY_PIGMEN", "ANGRYPIG_ZOMBIE", "ANGRYPIGMEN");
		for (int i = 1; i <= SlimeNamedEntitySpawner.SIZECOUNT; i++)
		{
			final String sizeText = SlimeNamedEntitySpawner.getSizeText(i);
			final SlimeNamedEntitySpawner slimeType = new SlimeNamedEntitySpawner(EntityType.SLIME, i);
			registerNamedEntitySpawner(slimeType, sizeText + "SLIME");
			final SlimeNamedEntitySpawner magmaType = new SlimeNamedEntitySpawner(EntityType.MAGMA_CUBE, i);
			registerNamedEntitySpawner(magmaType, sizeText + "LAVASLIME", sizeText + "MAGMACUBE", sizeText + "_MAGMACUBE");
		}
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.OCELOT)
		{

			@Override
			public String getName()
			{
				return "CAT";
			}

			@Override
			public Ocelot spawn(final Location location)
			{
				final Ocelot ocelot = (Ocelot) super.spawn(location);
				ocelot.setTamed(true);
				return ocelot;
			}
		});
		for (final DyeColor color : DyeColor.values())
			registerNamedEntitySpawner(new SheepNamedEntitySpawner(color));
		registerNamedEntitySpawner(new DefaultNamedEntitySpawner(EntityType.SHEEP)
		{

			private final Random random = new Random();
			private final DyeColor[] colors = DyeColor.values();
			private final int max = colors.length;

			@Override
			public String getName()
			{
				return "RANDOM_SHEEP";
			}

			@Override
			public Sheep spawn(final Location location)
			{
				final Sheep sheep = (Sheep) super.spawn(location);
				sheep.setColor(colors[random.nextInt(max)]);
				return sheep;
			}
		});
	}

	public NamedEntitySpawnerParamitrisable()
	{
		super(null);
	}

	public NamedEntitySpawnerParamitrisable(final NamedEntitySpawner defaultValue)
	{
		super(defaultValue);
	}

	public NamedEntitySpawnerParamitrisable(final String defaultValue)
	{
		this(ENTITY_TYPES.get(defaultValue.toUpperCase()));
	}

	@SuppressWarnings("deprecation")
	public NamedEntitySpawnerParamitrisable(final EntityType defaultValue)
	{
		this(defaultValue.getName());
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		value = ENTITY_TYPES.get(parameter.toUpperCase());
		if (value == null)
			throw new CrazyCommandNoSuchException("EntityType", parameter, tabHelp(parameter));
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return tabHelp(parameter);
	}

	public static List<String> tabHelp(String parameter)
	{
		parameter = parameter.toUpperCase();
		final List<String> res = new LinkedList<String>();
		if (parameter.length() == 0)
			res.addAll(ENTITY_TYPES.keySet());
		else
		{
			final List<String> more = new LinkedList<String>();
			for (final Entry<String, NamedEntitySpawner> entry : ENTITY_TYPES.entrySet())
				if (entry.getValue().getName().startsWith(parameter))
					res.add(entry.getKey());
				else if (entry.getValue().getType().name().contains(parameter) || entry.getKey().contains(parameter))
					more.add(entry.getKey());
			res.addAll(more);
		}
		return res.subList(0, Math.min(res.size(), 10));
	}

	private static class DefaultNamedEntitySpawner implements NamedEntitySpawner
	{

		protected final EntityType type;

		public DefaultNamedEntitySpawner(final EntityType type)
		{
			super();
			this.type = type;
		}

		@SuppressWarnings("deprecation")
		@Override
		public String getName()
		{
			if (type.getName() == null)
				return type.name();
			else
				return type.getName().toUpperCase();
		}

		@Override
		public final EntityType getType()
		{
			return type;
		}

		@Override
		public Class<? extends Entity> getEntityClass()
		{
			return type.getEntityClass();
		}

		@Override
		public Entity spawn(final Location location)
		{
			return location.getWorld().spawnEntity(location, type);
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: " + type.name() + "}";
		}

		@Override
		public Collection<? extends Entity> getEntities(final World world)
		{
			return world.getEntitiesByClass(type.getEntityClass());
		}
	}

	private static class BabyNamedEntitySpawner extends DefaultNamedEntitySpawner
	{

		public BabyNamedEntitySpawner(final EntityType type)
		{
			super(type);
		}

		@Override
		public String getName()
		{
			return "BABY_" + super.getName();
		}

		@Override
		public Ageable spawn(final Location location)
		{
			final Ageable ageable = (Ageable) super.spawn(location);
			ageable.setBaby();
			return ageable;
		}

		@Override
		public Collection<? extends Entity> getEntities(final World world)
		{
			final Collection<? extends Entity> entities = super.getEntities(world);
			final Iterator<? extends Entity> it = entities.iterator();
			while (it.hasNext())
			{
				final Ageable ageable = (Ageable) it.next();
				if (ageable.isAdult())
					it.remove();
			}
			return entities;
		}
	}

	private static class VillagerNamedEntitySpawner extends DefaultNamedEntitySpawner
	{

		private final Profession profession;

		public VillagerNamedEntitySpawner(final Profession profession)
		{
			super(EntityType.VILLAGER);
			this.profession = profession;
		}

		@Override
		public String getName()
		{
			return profession.toString();
		}

		@Override
		public Villager spawn(final Location location)
		{
			final Villager villager = (Villager) super.spawn(location);
			villager.setProfession(profession);
			return villager;
		}

		@Override
		public Collection<? extends Entity> getEntities(final World world)
		{
			final Collection<? extends Entity> entities = super.getEntities(world);
			final Iterator<? extends Entity> it = entities.iterator();
			while (it.hasNext())
			{
				final Villager villager = (Villager) it.next();
				if (villager.getProfession() != profession)
					it.remove();
			}
			return entities;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: " + type.name() + "; profession: " + profession.name() + "}";
		}
	}

	private static class SheepNamedEntitySpawner extends DefaultNamedEntitySpawner
	{

		private final DyeColor color;

		public SheepNamedEntitySpawner(final DyeColor color)
		{
			super(EntityType.SHEEP);
			this.color = color;
		}

		@Override
		public String getName()
		{
			return color.name() + "_SHEEP";
		}

		@Override
		public Sheep spawn(final Location location)
		{
			final Sheep sheep = (Sheep) super.spawn(location);
			sheep.setColor(color);
			return sheep;
		}

		@Override
		public Collection<? extends Entity> getEntities(final World world)
		{
			final Collection<? extends Entity> entities = super.getEntities(world);
			final Iterator<? extends Entity> it = entities.iterator();
			while (it.hasNext())
			{
				final Sheep sheep = (Sheep) it.next();
				if (sheep.getColor() != color)
					it.remove();
			}
			return entities;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: " + type.name() + "; color: " + color.name() + "}";
		}
	}

	private static class SlimeNamedEntitySpawner extends DefaultNamedEntitySpawner
	{

		private final static String[] SIZES = new String[] { "TINY", "SMALL", "DEFAULT", "LARGE", "HUGE", "TINYGIANT", "SMALLGIANT", "GIANT", "LARGEGIANT", "HUGEGIANT" };
		private final static int SIZECOUNT = SIZES.length;
		private final int size;

		public static String getSizeText(final int size)
		{
			return SIZES[size - 1];
		}

		public SlimeNamedEntitySpawner(final EntityType slimeType, final int size)
		{
			super(slimeType);
			this.size = size;
		}

		@Override
		public String getName()
		{
			return getSizeText() + "_" + super.getName();
		}

		public final String getSizeText()
		{
			return getSizeText(size);
		}

		@Override
		public Slime spawn(final Location location)
		{
			final Slime slime = (Slime) super.spawn(location);
			slime.setSize(size);
			return slime;
		}

		@Override
		public Collection<? extends Entity> getEntities(final World world)
		{
			final Collection<? extends Entity> entities = super.getEntities(world);
			final Iterator<? extends Entity> it = entities.iterator();
			while (it.hasNext())
			{
				final Slime slime = (Slime) it.next();
				if (slime.getSize() != size)
					it.remove();
			}
			return entities;
		}

		@Override
		public String toString()
		{
			return getClass().getSimpleName() + "{type: " + type.name() + "; size: " + size + "}";
		}
	}
}
