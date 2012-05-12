package de.st_ddt.crazyutil.poly.room;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public class RealRoom<S extends Room> implements ConfigurationSaveable
{

	protected S room;
	protected Location basis;

	public RealRoom(final S room, final Location basis)
	{
		super();
		this.room = room;
		this.basis = basis;
	}

	public S getRoom()
	{
		return room;
	}

	public void setRoom(final S room)
	{
		this.room = room;
	}

	public Location getBasis()
	{
		return basis;
	}

	public void setBasis(final Location basis)
	{
		this.basis = basis;
	}

	public boolean isInside(final Location location)
	{
		final Location clone = basis.clone();
		clone.subtract(location);
		return room.isInsideRel(clone.getX(), clone.getY(), clone.getZ());
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		ObjectSaveLoadHelper.saveLocation(config, path + "basis.", basis, true, false);
		room.save(config, path + "room.", true);
	}
}
