package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class VelocityProperty extends BasicProperty
{

	private final Vector velocity;

	public VelocityProperty()
	{
		super();
		this.velocity = new Vector(0, 0, 0);
	}

	public VelocityProperty(final Vector velocity)
	{
		super();
		this.velocity = velocity;
	}

	public VelocityProperty(final ConfigurationSection config)
	{
		super(config);
		final double x = config.getDouble("velocity.X", 0);
		final double y = config.getDouble("velocity.Y", 0);
		final double z = config.getDouble("velocity.Z", 0);
		this.velocity = new Vector(x, y, z);
	}

	public VelocityProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final DoubleParamitrisable velocityXParam = (DoubleParamitrisable) params.get("velocityx");
		final double x = velocityXParam.getValue();
		final DoubleParamitrisable velocityYParam = (DoubleParamitrisable) params.get("velocityy");
		final double y = velocityYParam.getValue();
		final DoubleParamitrisable velocityZParam = (DoubleParamitrisable) params.get("velocityz");
		final double z = velocityZParam.getValue();
		this.velocity = new Vector(x, y, z);
	}

	@Override
	public void apply(final Entity entity)
	{
		entity.setVelocity(velocity);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final DoubleParamitrisable velocityXParam = new DoubleParamitrisable(velocity.getX());
		params.put("vx", velocityXParam);
		params.put("velx", velocityXParam);
		params.put("velocityx", velocityXParam);
		final DoubleParamitrisable velocityYParam = new DoubleParamitrisable(velocity.getY());
		params.put("vy", velocityYParam);
		params.put("vely", velocityYParam);
		params.put("velocityy", velocityYParam);
		final DoubleParamitrisable velocityZParam = new DoubleParamitrisable(velocity.getZ());
		params.put("vz", velocityZParam);
		params.put("velz", velocityZParam);
		params.put("velocityz", velocityZParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "velocity.X", velocity.getX());
		config.set(path + "velocity.Y", velocity.getY());
		config.set(path + "velocity.Z", velocity.getZ());
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "velocity.X", "double");
		config.set(path + "velocity.Y", "double");
		config.set(path + "velocity.Z", "double");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.SKELETONTYPE $X$ $Y$ $Z$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.VELOCITY", target, velocity.getX(), velocity.getY(), velocity.getZ());
	}
}
