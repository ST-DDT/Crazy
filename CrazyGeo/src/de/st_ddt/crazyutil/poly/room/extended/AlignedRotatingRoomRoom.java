package de.st_ddt.crazyutil.poly.room.extended;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.room.Room;

public class AlignedRotatingRoomRoom extends RotatingRoomRoom
{

	protected double radius;

	public AlignedRotatingRoomRoom(final Room room, final double radius)
	{
		super(room, radius);
	}

	public AlignedRotatingRoomRoom(final ConfigurationSection config)
	{
		super(config);
	}

	@Override
	public boolean isInsideRel(final double x, final double y, final double z)
	{
		final double angle = Math.abs(Math.atan(z / x));
		final double radiusXPart = Math.cos(angle) * radius;
		final double radiusZPart = Math.sin(angle) * radius;
		return new RotatedRoom(room, 0, angle, 0).isInsideRel(x - radiusXPart, y, z - radiusZPart);
	}

	@Override
	public boolean equals(final RotatingRoomRoom room)
	{
		if (room instanceof AlignedRotatingRoomRoom)
			return equals((AlignedRotatingRoomRoom) room);
		return false;
	}

	public boolean equals(final AlignedRotatingRoomRoom room)
	{
		return radius == room.getRadius() && this.room.equals(room.getRoom());
	}

	@Override
	public AlignedRotatingRoomRoom clone()
	{
		return new AlignedRotatingRoomRoom(room.clone(), radius);
	}
}
