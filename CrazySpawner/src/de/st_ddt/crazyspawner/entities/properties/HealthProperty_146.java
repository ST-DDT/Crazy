package de.st_ddt.crazyspawner.entities.properties;

import java.lang.reflect.Method;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;

public final class HealthProperty_146 extends HealthProperty
{

	protected final Method setMaxHealth;
	protected final Method setHealth;

	public HealthProperty_146() throws SecurityException, NoSuchMethodException
	{
		super();
		setMaxHealth = Damageable.class.getDeclaredMethod("setMaxHealth", int.class);
		setHealth = Damageable.class.getDeclaredMethod("setHealth", int.class);
	}

	public HealthProperty_146(final ConfigurationSection config) throws SecurityException, NoSuchMethodException
	{
		super(config);
		setMaxHealth = Damageable.class.getDeclaredMethod("setMaxHealth", int.class);
		setHealth = Damageable.class.getDeclaredMethod("setHealth", int.class);
	}

	public HealthProperty_146(final Map<String, ? extends Paramitrisable> params) throws SecurityException, NoSuchMethodException
	{
		super(params);
		setMaxHealth = Damageable.class.getDeclaredMethod("setMaxHealth", int.class);
		setHealth = Damageable.class.getDeclaredMethod("setHealth", int.class);
	}

	@Override
	public void apply(final Entity entity)
	{
		if (maxHealth < 0)
			return;
		try
		{
			setMaxHealth.invoke(entity, (int) maxHealth);
			setHealth.invoke(entity, (int) maxHealth);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}
}
