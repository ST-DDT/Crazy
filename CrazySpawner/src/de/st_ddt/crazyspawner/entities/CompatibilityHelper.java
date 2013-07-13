package de.st_ddt.crazyspawner.entities;

import java.lang.reflect.Method;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.VersionComparator;

public class CompatibilityHelper
{

	private final static Healther healther = getHealther();
	private final static Damager damager = getDamager();

	private static Healther getHealther()
	{
		if (VersionComparator.compareVersions(ChatHelper.getMinecraftVersion(), "1.6.1") >= 0)
			return new Healther_161();
		else if (VersionComparator.compareVersions(ChatHelper.getMinecraftVersion(), "1.4.6") >= 0)
			try
			{
				return new Healther_146();
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				return new Healther_Null();
			}
		else
			return new Healther_Null();
	}

	private static Damager getDamager()
	{
		if (VersionComparator.compareVersions(ChatHelper.getMinecraftVersion(), "1.6.1") >= 0)
			return new Damager_161();
		else
			try
			{
				return new Damager_Pre161();
			}
			catch (final Exception e)
			{
				e.printStackTrace();
				return new Damager_Null();
			}
	}

	public static double getHealth(final LivingEntity entity)
	{
		return healther.getHealth(entity);
	}

	public static void setHealth(final LivingEntity entity, final double health)
	{
		healther.setHealth(entity, health);
	}

	public static double getMaxHealth(final LivingEntity entity)
	{
		return healther.getMaxHealth(entity);
	}

	public static void setMaxHealth(final LivingEntity entity, final double health)
	{
		healther.setMaxHealth(entity, health);
	}

	public static double getDamage(final EntityDamageEvent event)
	{
		return damager.getDamage(event);
	}

	public static void setDamage(final EntityDamageEvent event, final double damage)
	{
		damager.setDamage(event, damage);
	}

	private CompatibilityHelper()
	{
	}

	private interface Healther
	{

		public double getHealth(LivingEntity entity);

		public void setHealth(LivingEntity entity, double health);

		public double getMaxHealth(LivingEntity entity);

		public void setMaxHealth(LivingEntity entity, double health);
	}

	private final static class Healther_Null implements Healther
	{

		@Override
		public double getHealth(final LivingEntity entity)
		{
			return -1;
		}

		@Override
		public void setHealth(final LivingEntity entity, final double health)
		{
		}

		@Override
		public double getMaxHealth(final LivingEntity entity)
		{
			return -1;
		}

		@Override
		public void setMaxHealth(final LivingEntity entity, final double health)
		{
		}
	}

	private final static class Healther_146 implements Healther
	{

		private final Method getHealth;
		private final Method setHealth;
		private final Method getMaxHealth;
		private final Method setMaxHealth;

		private Healther_146() throws SecurityException, NoSuchMethodException
		{
			getHealth = Damageable.class.getDeclaredMethod("getHealth");
			setHealth = Damageable.class.getDeclaredMethod("setHealth", int.class);
			getMaxHealth = Damageable.class.getDeclaredMethod("getMaxHealth");
			setMaxHealth = Damageable.class.getDeclaredMethod("setMaxHealth", int.class);
		}

		@Override
		public double getHealth(final LivingEntity entity)
		{
			try
			{
				return ((Number) getHealth.invoke(entity)).doubleValue();
			}
			catch (final Exception e)
			{
				return -1;
			}
		}

		@Override
		public void setHealth(final LivingEntity entity, final double health)
		{
			try
			{
				setHealth.invoke(entity, (int) health);
			}
			catch (final Exception e)
			{}
		}

		@Override
		public double getMaxHealth(final LivingEntity entity)
		{
			try
			{
				return ((Number) getMaxHealth.invoke(entity)).doubleValue();
			}
			catch (final Exception e)
			{
				return -1;
			}
		}

		@Override
		public void setMaxHealth(final LivingEntity entity, final double health)
		{
			try
			{
				setMaxHealth.invoke(entity, (int) health);
			}
			catch (final Exception e)
			{}
		}
	}

	private final static class Healther_161 implements Healther
	{

		@Override
		public double getHealth(final LivingEntity entity)
		{
			return entity.getHealth();
		}

		@Override
		public void setHealth(final LivingEntity entity, final double health)
		{
			entity.setHealth(health);
		}

		@Override
		public double getMaxHealth(final LivingEntity entity)
		{
			return entity.getMaxHealth();
		}

		@Override
		public void setMaxHealth(final LivingEntity entity, final double health)
		{
			entity.setMaxHealth(health);
		}
	}

	private interface Damager
	{

		public double getDamage(EntityDamageEvent event);

		public void setDamage(EntityDamageEvent event, double damage);
	}

	private final static class Damager_Null implements Damager
	{

		@Override
		public double getDamage(final EntityDamageEvent event)
		{
			return -1;
		}

		@Override
		public void setDamage(final EntityDamageEvent event, final double damage)
		{
		}
	}

	private final static class Damager_Pre161 implements Damager
	{

		private final Method getDamage;
		private final Method setDamage;

		private Damager_Pre161() throws SecurityException, NoSuchMethodException
		{
			getDamage = EntityDamageEvent.class.getDeclaredMethod("getDamage");
			setDamage = EntityDamageEvent.class.getDeclaredMethod("setDamage", int.class);
		}

		@Override
		public double getDamage(final EntityDamageEvent event)
		{
			try
			{
				return ((Number) getDamage.invoke(event)).doubleValue();
			}
			catch (final Exception e)
			{
				return -1;
			}
		}

		@Override
		public void setDamage(final EntityDamageEvent event, final double damage)
		{
			try
			{
				setDamage.invoke(event, damage);
			}
			catch (final Exception e)
			{}
		}
	}

	private final static class Damager_161 implements Damager
	{

		@Override
		public double getDamage(final EntityDamageEvent event)
		{
			return event.getDamage();
		}

		@Override
		public void setDamage(final EntityDamageEvent event, final double damage)
		{
			event.setDamage(damage);
		}
	}
}
