package de.st_ddt.crazygeo.region;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.room.Room;

public class RotatedRealRoom<S extends Room> extends RealRoom<S>
{

	protected double yaw; // xy
	protected double pitch; // xz
	protected double roll; // yz

	public RotatedRealRoom(S room, Location basis)
	{
		super(room, basis);
		this.yaw = 0;
		this.pitch = 0;
		this.roll = 0;
	}

	public RotatedRealRoom(S room, Location basis, double yaw, double pitch, double roll)
	{
		super(room, basis);
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
	}

	public double getYaw()
	{
		return yaw;
	}

	public void setYaw(double yaw)
	{
		this.yaw = yaw;
	}

	public double getPitch()
	{
		return pitch;
	}

	public void setPitch(double pitch)
	{
		this.pitch = pitch;
	}

	public double getRoll()
	{
		return roll;
	}

	public void setRoll(double roll)
	{
		this.roll = roll;
	}

	@Override
	public boolean isInside(Location location)
	{
		final Location clone = basis.clone();
		clone.subtract(location);
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		// Yaw xy
		x = Math.sin(yaw) * x + Math.cos(yaw) * y;
		y = Math.cos(yaw) * x + Math.sin(yaw) * y;
		// Pitch xz
		x = Math.sin(yaw) * x + Math.cos(yaw) * z;
		z = Math.cos(yaw) * x + Math.sin(yaw) * z;
		// Roll yz
		y = Math.sin(yaw) * y + Math.cos(yaw) * z;
		z = Math.cos(yaw) * y + Math.sin(yaw) * z;
		return room.isInsideRel(x, y, z);
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		super.save(config, path);
		config.set(path + "yaw", yaw);
		config.set(path + "pitch", pitch);
		config.set(path + "roll", roll);
	}
}
