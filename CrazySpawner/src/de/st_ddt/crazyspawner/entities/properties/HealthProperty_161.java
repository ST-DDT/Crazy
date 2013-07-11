package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;

public final class HealthProperty_161 extends HealthProperty
{

	public HealthProperty_161()
	{
		super();
	}

	public HealthProperty_161(final ConfigurationSection config)
	{
		super(config);
	}

	public HealthProperty_161(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
	}

	@Override
	public void apply(final Entity entity)
	{
		if (maxHealth < 0)
			return;
		final Damageable damageable = (Damageable) entity;
		damageable.setMaxHealth(maxHealth);
		damageable.setHealth(maxHealth);
	}
}
