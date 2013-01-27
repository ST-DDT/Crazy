package de.st_ddt.crazyutil.poly.room.extended;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.room.Room;

public class NotRoom extends ExtendedRoom
{

	public NotRoom(final ConfigurationSection config)
	{
		super(config);
	}

	public NotRoom(final Room room)
	{
		super(room);
	}

	@Override
	public boolean isInsideRel(final double x, final double y, final double z)
	{
		return !room.isInsideRel(x, y, z);
	}

	@Override
	public boolean equals(final Room room)
	{
		if (room instanceof NotRoom)
			return equals((NotRoom) room);
		return false;
	}

	public boolean equals(final NotRoom room)
	{
		return this.room.equals(room.getRoom());
	}

	@Override
	public ExtendedRoom clone()
	{
		return new NotRoom(room.clone());
	}

	@Override
	public String toString()
	{
		return "Not {Room: " + room.toString() + "}";
	}
}
