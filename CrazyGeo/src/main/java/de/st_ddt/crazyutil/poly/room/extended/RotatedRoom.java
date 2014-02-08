package de.st_ddt.crazyutil.poly.room.extended;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.room.Room;

public class RotatedRoom extends ExtendedRoom
{

	protected double yaw; // xy
	protected double pitch; // xz
	protected double roll; // yz

	public RotatedRoom(final Room room)
	{
		super(room);
		this.yaw = 0;
		this.pitch = 0;
		this.roll = 0;
	}

	public RotatedRoom(final Room room, final double yaw, final double pitch, final double roll)
	{
		super(room);
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
	}

	public RotatedRoom(final ConfigurationSection config)
	{
		super(config);
		this.yaw = config.getDouble("yaw", 0);
		this.pitch = config.getDouble("pitch", 0);
		this.roll = config.getDouble("roll", 0);
	}

	public double getYaw()
	{
		return yaw;
	}

	public void setYaw(final double yaw)
	{
		this.yaw = yaw;
	}

	public double getPitch()
	{
		return pitch;
	}

	public void setPitch(final double pitch)
	{
		this.pitch = pitch;
	}

	public double getRoll()
	{
		return roll;
	}

	public void setRoll(final double roll)
	{
		this.roll = roll;
	}

	@Override
	public boolean isInsideRel(double x, double y, double z)
	{
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
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "yaw", yaw);
		config.set(path + "pitch", pitch);
		config.set(path + "roll", roll);
	}

	@Override
	public boolean equals(final Room room)
	{
		if (room instanceof OffsetRoom)
			return equals(room);
		return this.room.equals(room) && yaw == 0 && pitch == 0 && roll == 0;
	}

	public boolean equals(final RotatedRoom room)
	{
		return this.room.equals(room) && yaw == room.getYaw() && pitch == room.getPitch() && roll == room.getRoll();
	}

	@Override
	public RotatedRoom clone()
	{
		return new RotatedRoom(room, yaw, pitch, roll);
	}

	public RotatedRoom cloneAsRotatedRoom()
	{
		return new RotatedRoom(room, yaw, pitch, roll);
	}
}
