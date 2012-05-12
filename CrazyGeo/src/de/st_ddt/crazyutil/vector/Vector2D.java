package de.st_ddt.crazyutil.vector;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ConfigurationSaveable;

public class Vector2D implements ConfigurationSaveable
{

	protected double x;
	protected double z;

	public Vector2D(final double x, final double z)
	{
		super();
		this.x = x;
		this.z = z;
	}

	public Vector2D(final Location location)
	{
		this(location.getX(), location.getZ());
	}

	public Vector2D(final ConfigurationSection config)
	{
		config.getDouble("x");
		config.getDouble("z");
	}

	public double getX()
	{
		return x;
	}

	public void setX(final double x)
	{
		this.x = x;
	}

	public double getZ()
	{
		return z;
	}

	public void setZ(final double z)
	{
		this.z = z;
	}

	public void add(final double x, final double z)
	{
		this.x += x;
		this.z += z;
	}

	public void add(final Vector2D vector)
	{
		this.x += vector.getX();
		this.z += vector.getZ();
	}

	public void substract(final double x, final double z)
	{
		this.x -= x;
		this.z -= z;
	}

	public void substract(final Vector2D vector)
	{
		this.x -= vector.getX();
		this.z -= vector.getZ();
	}

	public void scale(final double scale)
	{
		this.x *= scale;
		this.z *= scale;
	}

	public void scale(final double scaleX, final double scaleZ)
	{
		this.x *= scaleX;
		this.z *= scaleZ;
	}

	@Override
	public Vector2D clone()
	{
		return new Vector2D(x, z);
	}

	public double length()
	{
		return Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
	}

	public Vector3D toVector3D(final double y)
	{
		return new Vector3D(x, y, z);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "x", x);
		config.set(path + "z", z);
	}
}
