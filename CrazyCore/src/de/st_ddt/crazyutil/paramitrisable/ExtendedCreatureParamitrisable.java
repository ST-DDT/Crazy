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
import org.bukkit.entity.Wolf;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ExtendedCreatureType;

public class ExtendedCreatureParamitrisable extends TypedParamitrisable<ExtendedCreatureType>
{

	public final static Map<String, ExtendedCreatureType> CREATURE_TYPES = getDefaultCreatureTypes();

	private static Map<String, ExtendedCreatureType> getDefaultCreatureTypes()
	{
		final Map<String, ExtendedCreatureType> res = new TreeMap<String, ExtendedCreatureType>(String.CASE_INSENSITIVE_ORDER);
		for (final EntityType type : EntityType.values())
			if (type.isAlive() && type.isSpawnable())
				res.put(type.toString(), new DefaultExtendedEntityType(type));
		return res;
	}

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
			public Entity spawn(final Location location)
			{
				final Entity entity = location.getWorld().spawnEntity(location, EntityType.CREEPER);
				((Creeper) entity).setPowered(true);
				return entity;
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
			public Entity spawn(final Location location)
			{
				final Entity entity = location.getWorld().spawnEntity(location, EntityType.CREEPER);
				((Creeper) entity).setPowered(true);
				return entity;
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
			public Entity spawn(final Location location)
			{
				final Entity entity = location.getWorld().spawnEntity(location, EntityType.WOLF);
				((Wolf) entity).setAngry(true);
				return entity;
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
			public Entity spawn(final Location location)
			{
				final Entity entity = location.getWorld().spawnEntity(location, EntityType.SLIME);
				((Slime) entity).setSize(1);
				return entity;
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
			public Entity spawn(final Location location)
			{
				final Entity entity = location.getWorld().spawnEntity(location, EntityType.MAGMA_CUBE);
				((Slime) entity).setSize(1);
				return entity;
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
			public Entity spawn(final Location location)
			{
				final Entity entity = location.getWorld().spawnEntity(location, EntityType.SLIME);
				((Slime) entity).setSize(2);
				return entity;
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
			public Entity spawn(final Location location)
			{
				final Entity entity = location.getWorld().spawnEntity(location, EntityType.MAGMA_CUBE);
				((Slime) entity).setSize(2);
				return entity;
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
			public Entity spawn(final Location location)
			{
				final Entity entity = location.getWorld().spawnEntity(location, EntityType.SLIME);
				((Slime) entity).setSize(3);
				return entity;
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
			public Entity spawn(final Location location)
			{
				final Entity entity = location.getWorld().spawnEntity(location, EntityType.MAGMA_CUBE);
				((Slime) entity).setSize(3);
				return entity;
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
			public Entity spawn(final Location location)
			{
				final Entity entity = location.getWorld().spawnEntity(location, EntityType.SLIME);
				((Slime) entity).setSize(4);
				return entity;
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
			public Entity spawn(final Location location)
			{
				final Entity entity = location.getWorld().spawnEntity(location, EntityType.MAGMA_CUBE);
				((Slime) entity).setSize(4);
				return entity;
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
			public Entity spawn(final Location location)
			{
				final Entity entity = location.getWorld().spawnEntity(location, EntityType.OCELOT);
				((Ocelot) entity).setTamed(true);
				return entity;
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
			public Entity spawn(final Location location)
			{
				final Entity entity = location.getWorld().spawnEntity(location, EntityType.SKELETON);
				((Skeleton) entity).setSkeletonType(SkeletonType.NORMAL);
				return entity;
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
			public Entity spawn(final Location location)
			{
				final Entity entity = location.getWorld().spawnEntity(location, EntityType.SKELETON);
				((Skeleton) entity).setSkeletonType(SkeletonType.WITHER);
				return entity;
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
				public Entity spawn(final Location location)
				{
					final Entity entity = location.getWorld().spawnEntity(location, EntityType.SHEEP);
					((Sheep) entity).setColor(color);
					return entity;
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
		}
		catch (final Exception e)
		{
			throw new CrazyCommandNoSuchException("CreatureType", parameter, CREATURE_TYPES.keySet());
		}
	}

	@Override
	public List<String> tab(String parameter)
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
}
