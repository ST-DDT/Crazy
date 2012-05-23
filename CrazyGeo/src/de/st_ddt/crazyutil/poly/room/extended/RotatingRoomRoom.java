package de.st_ddt.crazyutil.poly.room.extended;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.room.Room;

public class RotatingRoomRoom extends ExtendedRoom
{

	protected double radius;

	public RotatingRoomRoom(final Room room, final double radius)
	{
		super(room);
		this.radius = radius;
	}

	public RotatingRoomRoom(final ConfigurationSection config)
	{
		super(config);
		this.radius = config.getDouble("radius", 0);
	}

	public double getRadius()
	{
		return radius;
	}

	public void setRadius(final double radius)
	{
		this.radius = radius;
	}

	@Override
	public boolean isInsideRel(final double x, final double y, final double z)
	{
		final double angle = Math.abs(Math.atan(z / x));
		final double radiusXPart = Math.cos(angle) * radius;
		final double radiusZPart = Math.sin(angle) * radius;
		return room.isInsideRel(x - radiusXPart, y, z - radiusZPart);
	}

	@Override
	public boolean equals(final Room room)
	{
		if (room instanceof RotatingRoomRoom)
			return equals((RotatingRoomRoom) room);
		return false;
	}

	public boolean equals(final RotatingRoomRoom room)
	{
		return radius == room.getRadius() && this.room.equals(room.getRoom());
	}

	@Override
	public RotatingRoomRoom clone()
	{
		return new RotatingRoomRoom(room.clone(), radius);
	}
}
