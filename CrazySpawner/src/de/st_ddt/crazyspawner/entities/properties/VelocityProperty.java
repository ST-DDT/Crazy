package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.util.Vector;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class VelocityProperty extends BasicProperty
{

	public final static double ANGLECONVERTER = 180 / Math.PI;
	protected final int pitch;
	protected final int pitchOff;
	protected final int yaw;
	protected final int yawOff;
	protected final double velocityMin;
	protected final double velocityMax;

	public VelocityProperty()
	{
		super();
		this.pitch = 0;
		this.pitchOff = 0;
		this.yaw = 0;
		this.yawOff = 0;
		this.velocityMin = 0;
		this.velocityMax = 0;
	}

	public VelocityProperty(final Vector vector)
	{
		super();
		final double[] velocity = fromVector(vector);
		this.pitch = (int) velocity[0];
		this.pitchOff = 0;
		this.yaw = (int) velocity[1];
		this.yawOff = 0;
		this.velocityMin = velocity[2];
		this.velocityMax = velocity[2];
	}

	public VelocityProperty(final ConfigurationSection config)
	{
		super(config);
		if (config.contains("velocity.pitch"))
		{
			this.pitch = config.getInt("velocity.pitch", 0);
			this.pitchOff = config.getInt("velocity.pitchOff", 0);
			this.yaw = config.getInt("velocity.yaw", 0);
			this.yawOff = config.getInt("velocity.yawOff", 0);
			this.velocityMin = config.getDouble("velocity.speedMin", 0);
			this.velocityMax = config.getDouble("velocity.speedMax", 0);
		}
		else
		{
			final double x = config.getDouble("velocity.X", 0);
			final double y = config.getDouble("velocity.Y", 0);
			final double z = config.getDouble("velocity.Z", 0);
			final double[] velocity = fromXYZ(x, y, z);
			this.pitch = (int) velocity[0];
			this.pitchOff = 0;
			this.yaw = (int) velocity[1];
			this.yawOff = 0;
			this.velocityMin = velocity[2];
			this.velocityMax = velocity[2];
		}
	}

	public VelocityProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final IntegerParamitrisable pitchMinParam = (IntegerParamitrisable) params.get("velocitypitch");
		this.pitch = pitchMinParam.getValue();
		final IntegerParamitrisable pitchMaxParam = (IntegerParamitrisable) params.get("velocitypitchoff");
		this.pitchOff = pitchMaxParam.getValue();
		final IntegerParamitrisable yawMinParam = (IntegerParamitrisable) params.get("velocityyaw");
		this.yaw = yawMinParam.getValue();
		final IntegerParamitrisable yawMaxParam = (IntegerParamitrisable) params.get("velocityyawoff");
		this.yawOff = yawMaxParam.getValue();
		final DoubleParamitrisable velocityMinParam = (DoubleParamitrisable) params.get("velocitymin");
		this.velocityMin = velocityMinParam.getValue();
		final DoubleParamitrisable velocityMaxParam = (DoubleParamitrisable) params.get("velocitymax");
		this.velocityMax = velocityMaxParam.getValue();
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return !Fireball.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		final double pitch = getOff(this.pitch, pitchOff) / ANGLECONVERTER;
		final double yaw = getOff(this.yaw, yawOff) / ANGLECONVERTER;
		final double velocity = getRandom(velocityMin, velocityMax);
		entity.setVelocity(getVector(pitch, yaw, velocity));
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final IntegerParamitrisable pitchParam = new IntegerParamitrisable(pitch);
		params.put("vp", pitchParam);
		params.put("vpitch", pitchParam);
		params.put("velopitch", pitchParam);
		params.put("velocitypitch", pitchParam);
		final IntegerParamitrisable pitchOffParam = new IntegerParamitrisable(pitchOff);
		params.put("vpo", pitchOffParam);
		params.put("vpitchoff", pitchOffParam);
		params.put("velopitchoff", pitchOffParam);
		params.put("velocitypitchoff", pitchOffParam);
		final IntegerParamitrisable yawParam = new IntegerParamitrisable(yaw);
		params.put("vyaw", yawParam);
		params.put("veloyaw", yawParam);
		params.put("velocityyaw", yawParam);
		final IntegerParamitrisable yawOffParam = new IntegerParamitrisable(yawOff);
		params.put("vyo", yawOffParam);
		params.put("vyawoff", yawOffParam);
		params.put("veloyawoff", yawOffParam);
		params.put("velocityyawoff", yawOffParam);
		final DoubleParamitrisable velocityMinParam = new DoubleParamitrisable(velocityMin);
		params.put("vmin", velocityMinParam);
		params.put("velomin", velocityMinParam);
		params.put("velocitymin", velocityMinParam);
		params.put("speedmin", velocityMinParam);
		final DoubleParamitrisable velocityMaxParam = new DoubleParamitrisable(velocityMax);
		params.put("vmax", velocityMaxParam);
		params.put("velomax", velocityMaxParam);
		params.put("velocitymax", velocityMaxParam);
		params.put("speedmax", velocityMaxParam);
		final DoubleParamitrisable velocityXParam = new DoubleParamitrisable(null)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				final double vx = getValue();
				final Vector vector = getVector(pitch, yaw, (velocityMin + velocityMax) / 2);
				vector.setX(vx);
				final double[] res = fromVector(vector);
				pitchParam.setValue((int) res[0]);
				pitchOffParam.setValue(0);
				yawParam.setValue((int) res[1]);
				yawOffParam.setValue(0);
				velocityMinParam.setValue(res[2]);
				velocityMaxParam.setValue(res[2]);
			}
		};
		params.put("vx", velocityXParam);
		params.put("velx", velocityXParam);
		params.put("velocityx", velocityXParam);
		final DoubleParamitrisable velocityYParam = new DoubleParamitrisable(null)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				final double vy = getValue();
				final Vector vector = getVector(pitch, yaw, (velocityMin + velocityMax) / 2);
				vector.setY(vy);
				final double[] res = fromVector(vector);
				pitchParam.setValue((int) res[0]);
				pitchOffParam.setValue(0);
				yawParam.setValue((int) res[1]);
				yawOffParam.setValue(0);
				velocityMinParam.setValue(res[2]);
				velocityMaxParam.setValue(res[2]);
			}
		};
		params.put("vy", velocityYParam);
		params.put("vely", velocityYParam);
		params.put("velocityy", velocityYParam);
		final DoubleParamitrisable velocityZParam = new DoubleParamitrisable(null)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				final double vz = getValue();
				final Vector vector = getVector(pitch, yaw, (velocityMin + velocityMax) / 2);
				vector.setZ(vz);
				final double[] res = fromVector(vector);
				pitchParam.setValue((int) res[0]);
				pitchOffParam.setValue(0);
				yawParam.setValue((int) res[1]);
				yawOffParam.setValue(0);
				velocityMinParam.setValue(res[2]);
				velocityMaxParam.setValue(res[2]);
			}
		};
		params.put("vz", velocityZParam);
		params.put("velz", velocityZParam);
		params.put("velocityz", velocityZParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "velocity.pitch", pitch);
		config.set(path + "velocity.pitchOff", pitchOff);
		config.set(path + "velocity.yaw", yaw);
		config.set(path + "velocity.yawOff", yawOff);
		config.set(path + "velocity.speedMin", velocityMin);
		config.set(path + "velocity.speedMax", velocityMax);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "velocity.pitch", "int (0-360)");
		config.set(path + "velocity.pitchOff", "int (0-360)");
		config.set(path + "velocity.yaw", "int (0-360)");
		config.set(path + "velocity.yawOff", "int (0-360)");
		config.set(path + "velocity.speedMin", "double");
		config.set(path + "velocity.speedMax", "double");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.VELOCITY $Pitch$ $PitchOff$ $Yaw$ $YawOff$ $SpeedMin$ $SpeedMax$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.VELOCITY", target, pitch, pitchOff, yaw, yawOff, velocityMin, velocityMax);
	}

	@Override
	public boolean equalsDefault()
	{
		return pitch == 0 && pitchOff == 0 && yaw == 0 && yawOff == 0 && velocityMin == 0 && velocityMax == 0;
	}

	protected double getOff(final double center, final double off)
	{
		if (off <= 0)
			return center;
		if (RANDOM.nextBoolean())
			return center + RANDOM.nextDouble() * off;
		else
			return center - RANDOM.nextDouble() * off;
	}

	public static Vector getVector(final double pitch, final double yaw, final double velocity)
	{
		final double x = Math.sin(pitch) * velocity;
		final double z = Math.cos(pitch) * velocity;
		final double y = Math.sin(yaw) * velocity;
		return new Vector(x, y, z);
	}

	/**
	 * Converts the the given vector to two angles and a length.
	 * 
	 * @param x
	 *            The vector size in the x-dimension.
	 * @param y
	 *            The vector size in the y-dimension.
	 * @param z
	 *            The vector size in the z-dimension.
	 * @return An array consisting of pitch, yaw, length.
	 */
	public static double[] fromXYZ(final double x, final double y, final double z)
	{
		final double[] res = new double[3];
		res[0] = Math.tan(x / z) * ANGLECONVERTER;
		res[2] = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
		res[1] = Math.tan(y / res[2]) * ANGLECONVERTER;
		return res;
	}

	/**
	 * Converts the the given vector to two angles and a length.
	 * 
	 * @return An array consisting of pitch, yaw, length.
	 */
	public static double[] fromVector(final Vector vector)
	{
		return fromXYZ(vector.getX(), vector.getY(), vector.getZ());
	}
}
