package de.st_ddt.crazyutil.poly.room.multi;

import java.util.Collection;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.room.Room;

public class OR_Room extends MultiRoom
{

	public OR_Room()
	{
		super();
	}

	public OR_Room(final Collection<Room> rooms)
	{
		super(rooms);
	}

	public OR_Room(final ConfigurationSection config)
	{
		super(config);
	}

	@Override
	public boolean isInsideRel(final double x, final double y, final double z)
	{
		for (final Room room : rooms)
			if (room.isInsideRel(x, y, z))
				return true;
		return false;
	}

	@Override
	public MultiRoom clone()
	{
		return new OR_Room(rooms);
	}
}
