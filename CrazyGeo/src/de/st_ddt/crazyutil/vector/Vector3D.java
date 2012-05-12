package de.st_ddt.crazyutil.vector;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class Vector3D extends Vector2D
{

	protected double y;

	public Vector3D(final double x, final double y, final double z)
	{
		super(x, z);
		this.y = y;
	}

	public Vector3D(final Location location)
	{
		super(location);
		this.y = location.getY();
	}

	public Vector3D(final ConfigurationSection config)
	{
		super(config);
		this.y = config.getDouble("y");
	}

	public double getY()
	{
		return y;
	}

	public void setY(final double y)
	{
		this.y = y;
	}

	public void add(final Vector3D vector)
	{
		super.add(vector);
		this.y += vector.getY();
	}

	public void add(final double x, final double y, final double z)
	{
		super.add(x, z);
		this.y += y;
	}

	public void substract(final Vector3D vector)
	{
		super.substract(vector);
		this.y -= vector.getY();
	}

	public void substract(final double x, final double y, final double z)
	{
		super.substract(x, z);
		this.y -= y;
	}

	@Override
	public void scale(final double scale)
	{
		super.scale(scale);
		this.y *= scale;
	}

	public void scale(final double scaleX, final double scaleY, final double scaleZ)
	{
		super.scale(scaleX, scaleZ);
		this.y *= scaleY;
	}

	@Override
	public double length()
	{
		return Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2) + Math.pow(y, 2));
	}

	@Override
	public Vector3D clone()
	{
		return new Vector3D(x, y, z);
	}

	public Location getLocation(final World world)
	{
		return new Location(world, x, y, y);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "y", y);
	}
}
