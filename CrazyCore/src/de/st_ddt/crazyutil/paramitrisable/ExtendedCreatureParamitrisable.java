package de.st_ddt.crazyutil.paramitrisable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
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
				registerExtendedEntityType(new DefaultExtendedEntityType(type), type.toString());
		for (final Profession profession : Profession.values())
			registerExtendedEntityType(new VillagerExtendedEntityType(profession), "VILLAGER_" + profession.toString());
		registerExtendedEntityType(new ExtendedCreatureType()
		{

			@Override
			public String getName()
			{
				return "ZOMBIE_VILLAGER";
			}

			@Override
			public EntityType getType()
			{
				return EntityType.VILLAGER;
			}

			@Override
			public Zombie spawn(final Location location)
			{
				final Zombie zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
				zombie.setVillager(true);
				return zombie;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.ZOMBIE.getEntityClass());
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (!((Zombie) it.next()).isVillager())
						it.remove();
				return entities;
			}

			@Override
			public String toString()
			{
				return getName();
			}
		});
		registerExtendedEntityType(new ExtendedCreatureType()
		{

			@Override
			public String getName()
			{
				return "POWEREDCREEPER";
			}

			@Override
			public EntityType getType()
			{
				return EntityType.CREEPER;
			}

			@Override
			public Creeper spawn(final Location location)
			{
				final Creeper creeper = (Creeper) location.getWorld().spawnEntity(location, EntityType.CREEPER);
				creeper.setPowered(true);
				return creeper;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.CREEPER.getEntityClass());
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (!((Creeper) it.next()).isPowered())
						it.remove();
				return entities;
			}

			@Override
			public String toString()
			{
				return getName();
			}
		}, "POWEREDCREEPER", "CHARGEDCREEPER");
		registerExtendedEntityType(new ExtendedCreatureType()
		{

			@Override
			public String getName()
			{
				return "UNPOWEREDCREEPER";
			}

			@Override
			public EntityType getType()
			{
				return EntityType.CREEPER;
			}

			@Override
			public Creeper spawn(final Location location)
			{
				final Creeper creeper = (Creeper) location.getWorld().spawnEntity(location, EntityType.CREEPER);
				creeper.setPowered(false);
				return creeper;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.CREEPER.getEntityClass());
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Creeper) it.next()).isPowered())
						it.remove();
				return entities;
			}

			@Override
			public String toString()
			{
				return getName();
			}
		}, "UNCHARGEDCREEPER");
		registerExtendedEntityType(new ExtendedCreatureType()
		{

			@Override
			public String getName()
			{
				return "ANGRYWOLF";
			}

			@Override
			public EntityType getType()
			{
				return EntityType.WOLF;
			}

			@Override
			public Wolf spawn(final Location location)
			{
				final Wolf wolf = (Wolf) location.getWorld().spawnEntity(location, EntityType.WOLF);
				wolf.setAngry(true);
				return wolf;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.WOLF.getEntityClass());
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (!((Wolf) it.next()).isAngry())
						it.remove();
				return entities;
			}

			@Override
			public String toString()
			{
				return getName();
			}
		});
		registerExtendedEntityType(new ExtendedCreatureType()
		{

			@Override
			public String getName()
			{
				return "TINYSLIME";
			}

			@Override
			public EntityType getType()
			{
				return EntityType.SLIME;
			}

			@Override
			public Slime spawn(final Location location)
			{
				final Slime slime = (Slime) location.getWorld().spawnEntity(location, EntityType.SLIME);
				slime.setSize(1);
				return slime;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.SLIME.getEntityClass());
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Slime) it.next()).getSize() != 1)
						it.remove();
				return entities;
			}

			@Override
			public String toString()
			{
				return getName();
			}
		});
		registerExtendedEntityType(new ExtendedCreatureType()
		{

			@Override
			public String getName()
			{
				return "TINYMAGMASLIME";
			}

			@Override
			public EntityType getType()
			{
				return EntityType.MAGMA_CUBE;
			}

			@Override
			public Slime spawn(final Location location)
			{
				final Slime slime = (Slime) location.getWorld().spawnEntity(location, EntityType.MAGMA_CUBE);
				slime.setSize(1);
				return slime;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.MAGMA_CUBE.getEntityClass());
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Slime) it.next()).getSize() != 1)
						it.remove();
				return entities;
			}

			@Override
			public String toString()
			{
				return getName();
			}
		});
		registerExtendedEntityType(new ExtendedCreatureType()
		{

			@Override
			public String getName()
			{
				return "SMALLSLIME";
			}

			@Override
			public EntityType getType()
			{
				return EntityType.SLIME;
			}

			@Override
			public Slime spawn(final Location location)
			{
				final Slime slime = (Slime) location.getWorld().spawnEntity(location, EntityType.SLIME);
				slime.setSize(2);
				return slime;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.SLIME.getEntityClass());
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Slime) it.next()).getSize() != 2)
						it.remove();
				return entities;
			}

			@Override
			public String toString()
			{
				return getName();
			}
		});
		registerExtendedEntityType(new ExtendedCreatureType()
		{

			@Override
			public String getName()
			{
				return "SMALLMAGMASLIME";
			}

			@Override
			public EntityType getType()
			{
				return EntityType.MAGMA_CUBE;
			}

			@Override
			public Slime spawn(final Location location)
			{
				final Slime slime = (Slime) location.getWorld().spawnEntity(location, EntityType.MAGMA_CUBE);
				slime.setSize(2);
				return slime;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.MAGMA_CUBE.getEntityClass());
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Slime) it.next()).getSize() != 2)
						it.remove();
				return entities;
			}

			@Override
			public String toString()
			{
				return getName();
			}
		});
		registerExtendedEntityType(new ExtendedCreatureType()
		{

			@Override
			public String getName()
			{
				return "BIGSLIME";
			}

			@Override
			public EntityType getType()
			{
				return EntityType.SLIME;
			}

			@Override
			public Slime spawn(final Location location)
			{
				final Slime slime = (Slime) location.getWorld().spawnEntity(location, EntityType.SLIME);
				slime.setSize(3);
				return slime;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.SLIME.getEntityClass());
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Slime) it.next()).getSize() != 3)
						it.remove();
				return entities;
			}

			@Override
			public String toString()
			{
				return getName();
			}
		});
		registerExtendedEntityType(new ExtendedCreatureType()
		{

			@Override
			public String getName()
			{
				return "BIGMAGMASLIME";
			}

			@Override
			public EntityType getType()
			{
				return EntityType.MAGMA_CUBE;
			}

			@Override
			public Slime spawn(final Location location)
			{
				final Slime slime = (Slime) location.getWorld().spawnEntity(location, EntityType.MAGMA_CUBE);
				slime.setSize(3);
				return slime;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.MAGMA_CUBE.getEntityClass());
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Slime) it.next()).getSize() != 3)
						it.remove();
				return entities;
			}

			@Override
			public String toString()
			{
				return getName();
			}
		});
		registerExtendedEntityType(new ExtendedCreatureType()
		{

			@Override
			public String getName()
			{
				return "HUGESLIME";
			}

			@Override
			public EntityType getType()
			{
				return EntityType.SLIME;
			}

			@Override
			public Slime spawn(final Location location)
			{
				final Slime slime = (Slime) location.getWorld().spawnEntity(location, EntityType.SLIME);
				slime.setSize(4);
				return slime;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.SLIME.getEntityClass());
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Slime) it.next()).getSize() != 4)
						it.remove();
				return entities;
			}

			@Override
			public String toString()
			{
				return getName();
			}
		});
		registerExtendedEntityType(new ExtendedCreatureType()
		{

			@Override
			public String getName()
			{
				return "HUGEMAGMASLIME";
			}

			@Override
			public EntityType getType()
			{
				return EntityType.MAGMA_CUBE;
			}

			@Override
			public Slime spawn(final Location location)
			{
				final Slime slime = (Slime) location.getWorld().spawnEntity(location, EntityType.MAGMA_CUBE);
				slime.setSize(4);
				return slime;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.MAGMA_CUBE.getEntityClass());
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Slime) it.next()).getSize() != 4)
						it.remove();
				return entities;
			}

			@Override
			public String toString()
			{
				return getName();
			}
		});
		registerExtendedEntityType(new ExtendedCreatureType()
		{

			@Override
			public String getName()
			{
				return "CAT";
			}

			@Override
			public EntityType getType()
			{
				return EntityType.OCELOT;
			}

			@Override
			public Ocelot spawn(final Location location)
			{
				final Ocelot ocelot = (Ocelot) location.getWorld().spawnEntity(location, EntityType.OCELOT);
				ocelot.setTamed(true);
				return ocelot;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.OCELOT.getEntityClass());
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Ocelot) it.next()).isTamed())
						it.remove();
				return entities;
			}

			@Override
			public String toString()
			{
				return getName();
			}
		});
		registerExtendedEntityType(new ExtendedCreatureType()
		{

			@Override
			public String getName()
			{
				return "NORMALSKELETON";
			}

			@Override
			public EntityType getType()
			{
				return EntityType.SKELETON;
			}

			@Override
			public Skeleton spawn(final Location location)
			{
				final Skeleton skeleton = (Skeleton) location.getWorld().spawnEntity(location, EntityType.SKELETON);
				skeleton.setSkeletonType(SkeletonType.NORMAL);
				return skeleton;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.SKELETON.getEntityClass());
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Skeleton) it.next()).getSkeletonType() != SkeletonType.NORMAL)
						it.remove();
				return entities;
			}

			@Override
			public String toString()
			{
				return getName();
			}
		});
		registerExtendedEntityType(new ExtendedCreatureType()
		{

			@Override
			public String getName()
			{
				return "WITHERSKELETON";
			}

			@Override
			public EntityType getType()
			{
				return EntityType.SKELETON;
			}

			@Override
			public Skeleton spawn(final Location location)
			{
				final Skeleton skeleton = (Skeleton) location.getWorld().spawnEntity(location, EntityType.SKELETON);
				skeleton.setSkeletonType(SkeletonType.WITHER);
				return skeleton;
			}

			@Override
			public Collection<? extends Entity> getEntities(final World world)
			{
				final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.SKELETON.getEntityClass());
				final Iterator<? extends Entity> it = entities.iterator();
				while (it.hasNext())
					if (((Skeleton) it.next()).getSkeletonType() != SkeletonType.WITHER)
						it.remove();
				return entities;
			}

			@Override
			public String toString()
			{
				return getName();
			}
		});
		for (final DyeColor color : DyeColor.values())
			registerExtendedEntityType(new ExtendedCreatureType()
			{

				@Override
				public String getName()
				{
					return color.name() + "SHEEP";
				}

				@Override
				public EntityType getType()
				{
					return EntityType.SHEEP;
				}

				@Override
				public Sheep spawn(final Location location)
				{
					final Sheep sheep = (Sheep) location.getWorld().spawnEntity(location, EntityType.SHEEP);
					sheep.setColor(color);
					return sheep;
				}

				@Override
				public Collection<? extends Entity> getEntities(final World world)
				{
					final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.SKELETON.getEntityClass());
					final Iterator<? extends Entity> it = entities.iterator();
					while (it.hasNext())
						if (((Sheep) it.next()).getColor() != color)
							it.remove();
					return entities;
				}

				@Override
				public String toString()
				{
					return getName();
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
		try
		{
			value = CREATURE_TYPES.get(parameter.toUpperCase());
			if (value == null)
				throw new CrazyCommandNoSuchException("CreatureType", parameter, tabHelp(parameter));
		}
		catch (final Exception e)
		{
			throw new CrazyCommandNoSuchException("CreatureType", parameter, CREATURE_TYPES.keySet());
		}
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
		for (final String name : CREATURE_TYPES.keySet())
			if (name.startsWith(parameter))
				res.add(name);
		return res;
	}

	private static class DefaultExtendedEntityType implements ExtendedCreatureType
	{

		private final EntityType type;

		public DefaultExtendedEntityType(final EntityType type)
		{
			super();
			this.type = type;
		}

		@Override
		public String getName()
		{
			return type.getName();
		}

		@Override
		public EntityType getType()
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
		public String toString()
		{
			return getName();
		}
	}

	private static class VillagerExtendedEntityType implements ExtendedCreatureType
	{

		private final Profession profession;

		public VillagerExtendedEntityType(final Profession profession)
		{
			super();
			this.profession = profession;
		}

		@Override
		public String getName()
		{
			return profession.toString();
		}

		@Override
		public EntityType getType()
		{
			return EntityType.VILLAGER;
		}

		@Override
		public Villager spawn(final Location location)
		{
			final Villager villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
			villager.setProfession(profession);
			return villager;
		}

		@Override
		public Collection<? extends Entity> getEntities(final World world)
		{
			final Collection<? extends Entity> entities = world.getEntitiesByClass(EntityType.VILLAGER.getEntityClass());
			final Iterator<? extends Entity> it = entities.iterator();
			while (it.hasNext())
				if (((Villager) it.next()).getProfession() != profession)
					it.remove();
			return entities;
		}

		@Override
		public String toString()
		{
			return getName();
		}
	}
}
