package de.st_ddt.crazyutil.poly.room.extended;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.room.PseudoRoom;
import de.st_ddt.crazyutil.poly.room.Room;

public abstract class ExtendedRoom implements Room
{

	protected Room room;

	public ExtendedRoom(final Room room)
	{
		super();
		this.room = room;
	}

	public ExtendedRoom(final ConfigurationSection config)
	{
		super();
		this.room = PseudoRoom.load(config.getConfigurationSection("room"));
	}

	public Room getRoom()
	{
		return room;
	}

	public void setRoom(final Room room)
	{
		this.room = room;
	}

	@Override
	public void scale(final double scale)
	{
		room.scale(scale);
	}

	@Override
	public void scale(final double scaleX, final double scaleY, final double scaleZ)
	{
		room.scale(scaleX, scaleY, scaleZ);
	}

	@Override
	public final void save(final ConfigurationSection config, final String path, final boolean includeType)
	{
		if (includeType)
			config.set(path + "type", getClass().getName());
		save(config, path);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		room.save(config, path + "room.");
	}

	@Override
	public abstract ExtendedRoom clone();

	@Override
	public String toString()
	{
		return getClass().getSimpleName();
	}
}
