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
import de.st_ddt.crazyutil.ExtendedCreatureType;

public class ExtendedCreatureParamitrisable extends TypedParamitrisable<ExtendedCreatureType>
{

	public final static Map<String, ExtendedCreatureType> CREATURE_TYPES = new TreeMap<String, ExtendedCreatureType>(String.CASE_INSENSITIVE_ORDER);

	public static void registerExtendedEntityType(final ExtendedCreatureType type, final String... aliases)
	{
		CREATURE_TYPES.put(type.getName().toUpperCase(), type);
		for (final String alias : aliases)
			CREATURE_TYPES.put(alias.toUpperCase(), type);
	}

	public static ExtendedCreatureType getExtendedCreatureType(final String name)
	{
		if (name == null)
			return null;
		else
			return CREATURE_TYPES.get(name.toUpperCase());
	}

	public static List<ExtendedCreatureType> getExtendedCreatureTypeList(final Collection<String> names)
	{
		if (names == null)
			return new ArrayList<ExtendedCreatureType>(0);
		final List<ExtendedCreatureType> res = new ArrayList<ExtendedCreatureType>(names.size());
		for (final String name : names)
			res.add(getExtendedCreatureType(name));
		return res;
	}

	static
	{
		for (final EntityType type : EntityType.values())
			if (type.isAlive() && type.isSpawnable())
			{
				registerExtendedEntityType(new DefaultExtendedEntityType(type), type.toString());
				if (Ageable.class.isAssignableFrom(type.getEntityClass()))
					registerExtendedEntityType(new BabyExtendedEntityType(type));
			}
		for (final Profession profession : Profession.values())
			registerExtendedEntityType(new VillagerExtendedEntityType(profession));
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.ZOMBIE)
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

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (!((Zombie) it.next()).isBaby())
						it.remove();
				return entities;
			}
		});
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.ZOMBIE)
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

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (!((Zombie) it.next()).isVillager())
						it.remove();
				return entities;
			}
		});
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.ZOMBIE)
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

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
				{
					final Zombie zombie = (Zombie) it.next();
					if (!zombie.isVillager() || !zombie.isBaby())
						it.remove();
				}
				return entities;
			}
		});
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.SKELETON)
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

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Skeleton) it.next()).getSkeletonType() != SkeletonType.NORMAL)
						it.remove();
				return entities;
			}
		});
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.SKELETON)
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

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Skeleton) it.next()).getSkeletonType() != SkeletonType.WITHER)
						it.remove();
				return entities;
			}
		});
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.CREEPER)
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

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Creeper) it.next()).isPowered())
						it.remove();
				return entities;
			}
		}, "UNCHARGEDCREEPER");
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.CREEPER)
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

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (!((Creeper) it.next()).isPowered())
						it.remove();
				return entities;
			}
		}, "POWEREDCREEPER", "CHARGEDCREEPER");
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.WOLF)
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

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (!((Wolf) it.next()).isAngry())
						it.remove();
				return entities;
			}
		}, "ANGRYWOLF");
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.PIG_ZOMBIE)
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

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (!((PigZombie) it.next()).isAngry())
						it.remove();
				return entities;
			}
		}, "ANGRY_PIGMEN", "ANGRYPIG_ZOMBIE", "ANGRYPIGMEN");
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.SLIME)
		{

			@Override
			public String getName()
			{
				return "TINYSLIME";
			}

			@Override
			public Slime spawn(final Location location)
			{
				final Slime slime = (Slime) super.spawn(location);
				slime.setSize(1);
				return slime;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Slime) it.next()).getSize() != 1)
						it.remove();
				return entities;
			}
		});
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.MAGMA_CUBE)
		{

			@Override
			public String getName()
			{
				return "TINYMAGMASLIME";
			}

			@Override
			public Slime spawn(final Location location)
			{
				final Slime slime = (Slime) super.spawn(location);
				slime.setSize(1);
				return slime;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Slime) it.next()).getSize() != 1)
						it.remove();
				return entities;
			}
		});
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.SLIME)
		{

			@Override
			public String getName()
			{
				return "SMALLSLIME";
			}

			@Override
			public Slime spawn(final Location location)
			{
				final Slime slime = (Slime) super.spawn(location);
				slime.setSize(2);
				return slime;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Slime) it.next()).getSize() != 2)
						it.remove();
				return entities;
			}
		});
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.MAGMA_CUBE)
		{

			@Override
			public String getName()
			{
				return "SMALLMAGMASLIME";
			}

			@Override
			public Slime spawn(final Location location)
			{
				final Slime slime = (Slime) super.spawn(location);
				slime.setSize(2);
				return slime;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Slime) it.next()).getSize() != 2)
						it.remove();
				return entities;
			}
		});
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.SLIME)
		{

			@Override
			public String getName()
			{
				return "BIGSLIME";
			}

			@Override
			public Slime spawn(final Location location)
			{
				final Slime slime = (Slime) super.spawn(location);
				slime.setSize(3);
				return slime;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Slime) it.next()).getSize() != 3)
						it.remove();
				return entities;
			}
		});
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.MAGMA_CUBE)
		{

			@Override
			public String getName()
			{
				return "BIGMAGMASLIME";
			}

			@Override
			public Slime spawn(final Location location)
			{
				final Slime slime = (Slime) super.spawn(location);
				slime.setSize(3);
				return slime;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Slime) it.next()).getSize() != 3)
						it.remove();
				return entities;
			}
		});
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.SLIME)
		{

			@Override
			public String getName()
			{
				return "HUGESLIME";
			}

			@Override
			public Slime spawn(final Location location)
			{
				final Slime slime = (Slime) super.spawn(location);
				slime.setSize(4);
				return slime;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Slime) it.next()).getSize() != 4)
						it.remove();
				return entities;
			}
		});
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.MAGMA_CUBE)
		{

			@Override
			public String getName()
			{
				return "HUGEMAGMASLIME";
			}

			@Override
			public Slime spawn(final Location location)
			{
				final Slime slime = (Slime) super.spawn(location);
				slime.setSize(4);
				return slime;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Slime) it.next()).getSize() != 4)
						it.remove();
				return entities;
			}
		});
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.OCELOT)
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

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = super.getEntities(world);
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Ocelot) it.next()).isTamed())
						it.remove();
				return entities;
			}
		});
		for (final DyeColor color : DyeColor.values())
			registerExtendedEntityType(new SheepExtendedEntityType(color));
		registerExtendedEntityType(new DefaultExtendedEntityType(EntityType.SHEEP)
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

	public ExtendedCreatureParamitrisable()
	{
		super(null);
	}

	public ExtendedCreatureParamitrisable(final ExtendedCreatureType defaultValue)
	{
		super(defaultValue);
	}

	public ExtendedCreatureParamitrisable(final String defaultValue)
	{
		this(CREATURE_TYPES.get(defaultValue.toUpperCase()));
	}

	public ExtendedCreatureParamitrisable(final EntityType defaultValue)
	{
		this(defaultValue.getName());
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		value = CREATURE_TYPES.get(parameter.toUpperCase());
		if (value == null)
			throw new CrazyCommandNoSuchException("CreatureType", parameter, tabHelp(parameter));
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
		final List<String> more = new LinkedList<String>();
		if (parameter.length() == 0)
			res.addAll(CREATURE_TYPES.keySet());
		else
			for (final Entry<String, ExtendedCreatureType> entry : CREATURE_TYPES.entrySet())
				if (entry.getValue().getName().startsWith(parameter))
					res.add(entry.getKey());
				else if (entry.getValue().getType().name().contains(parameter) || entry.getKey().contains(parameter))
					more.add(entry.getKey());
		res.addAll(more);
		return res.subList(0, Math.min(res.size(), 10));
	}

	private static class DefaultExtendedEntityType implements ExtendedCreatureType
	{

		protected final EntityType type;

		public DefaultExtendedEntityType(final EntityType type)
		{
			super();
			this.type = type;
		}

		@Override
		public String getName()
		{
			return type.getName().toUpperCase();
		}

		@Override
		public final EntityType getType()
		{
			return type;
		}

		@Override
		public Entity spawn(final Location location)
		{
			return location.getWorld().spawnEntity(location, type);
		}

		@Override
		public Collection<? extends Entity> getEntities(final World world)
		{
			return world.getEntitiesByClass(type.getEntityClass());
		}

		@Override
		public final String toString()
		{
			return getName();
		}
	}

	private static class BabyExtendedEntityType extends DefaultExtendedEntityType
	{

		public BabyExtendedEntityType(final EntityType type)
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
				if (((Ageable) it.next()).isAdult())
					it.remove();
			return entities;
		}
	}

	private static class VillagerExtendedEntityType extends DefaultExtendedEntityType
	{

		private final Profession profession;

		public VillagerExtendedEntityType(final Profession profession)
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
				if (((Villager) it.next()).getProfession() != profession)
					it.remove();
			return entities;
		}
	}

	private static class SheepExtendedEntityType extends DefaultExtendedEntityType
	{

		private final DyeColor color;

		public SheepExtendedEntityType(final DyeColor color)
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
				if (((Sheep) it.next()).getColor() != color)
					it.remove();
			return entities;
		}
	}
}
