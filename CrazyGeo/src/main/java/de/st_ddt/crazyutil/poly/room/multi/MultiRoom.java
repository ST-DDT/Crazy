package de.st_ddt.crazyutil.poly.room.multi;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.room.PseudoRoom;
import de.st_ddt.crazyutil.poly.room.Room;

public abstract class MultiRoom extends PseudoRoom
{

	protected final ArrayList<Room> rooms = new ArrayList<Room>();

	public MultiRoom()
	{
		super();
	}

	public MultiRoom(final Collection<Room> rooms)
	{
		super();
		this.rooms.addAll(rooms);
	}

	public MultiRoom(final ConfigurationSection config)
	{
		super();
		final ConfigurationSection roomConfig = config.getConfigurationSection("rooms");
		for (final String key : roomConfig.getKeys(false))
			rooms.add(PseudoRoom.load(roomConfig.getConfigurationSection(key)));
	}

	public ArrayList<Room> getRooms()
	{
		return rooms;
	}

	@Override
	public void scale(final double scale)
	{
		for (final Room room : rooms)
			room.scale(scale);
	}

	@Override
	public void scale(final double scaleX, final double scaleY, final double scaleZ)
	{
		for (final Room room : rooms)
			room.scale(scaleX, scaleY, scaleZ);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		int i = 0;
		config.set(path + "rooms", null);
		for (final Room room : rooms)
			room.save(config, path + "rooms.room" + (i++) + ".");
	}

	@Override
	public boolean equals(final Room room)
	{
		if (room instanceof MultiRoom)
			return equals((MultiRoom) room);
		return false;
	}

	public boolean equals(final MultiRoom room)
	{
		Found: for (final Room search : rooms)
		{
			for (final Room match : room.getRooms())
				if (search.equals(match))
					continue Found;
			return false;
		}
		return true;
	}

	@Override
	public abstract MultiRoom clone();
}
