package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.util.Vector;

import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;

public class FireballVelocityProperty extends VelocityProperty
{

	public FireballVelocityProperty()
	{
		super();
	}

	public FireballVelocityProperty(final Vector vector)
	{
		super(vector);
	}

	public FireballVelocityProperty(final ConfigurationSection config)
	{
		super(config);
	}

	public FireballVelocityProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return Fireball.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		final Fireball fireball = (Fireball) entity;
		final double pitch = getOff(this.pitch, pitchOff) / ANGLECONVERTER;
		final double yaw = getOff(this.yaw, yawOff) / ANGLECONVERTER;
		final double velocity = getRandom(velocityMin, velocityMax);
		fireball.setDirection(getVector(pitch, yaw, velocity));
	}
}
