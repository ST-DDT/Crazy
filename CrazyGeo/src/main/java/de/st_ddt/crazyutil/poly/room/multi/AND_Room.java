package de.st_ddt.crazyutil.poly.room.multi;

import java.util.Collection;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.room.Room;

public class AND_Room extends MultiRoom
{

	public AND_Room()
	{
		super();
	}

	public AND_Room(final Collection<Room> rooms)
	{
		super(rooms);
	}

	public AND_Room(final ConfigurationSection config)
	{
		super(config);
	}

	@Override
	public boolean isInsideRel(final double x, final double y, final double z)
	{
		for (final Room room : rooms)
			if (!room.isInsideRel(x, y, z))
				return false;
		return true;
	}

	@Override
	public MultiRoom clone()
	{
		return new AND_Room(rooms);
	}
}
