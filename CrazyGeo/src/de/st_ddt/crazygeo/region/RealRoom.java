package de.st_ddt.crazygeo.region;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.poly.room.PseudoRoom;
import de.st_ddt.crazyutil.poly.room.Room;

public class RealRoom<S extends Room> implements ConfigurationSaveable
{

	protected S room;
	protected Location basis;

	public static RealRoom<Room> load(final ConfigurationSection config, final World world)
	{
		return new RealRoom<Room>(PseudoRoom.load(config.getConfigurationSection("room")), ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection("basis"), world));
	}

	public RealRoom(final RealRoom<S> realRoom)
	{
		super();
		this.room = realRoom.getRoom();
		this.basis = realRoom.getBasis();
	}

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

	public World getWorld()
	{
		return basis.getWorld();
	}

	public final boolean isInside(final Entity entity)
	{
		return isInside(entity.getLocation());
	}

	public boolean isInside(final Location location)
	{
		if (!location.getWorld().equals(basis.getWorld()))
			return false;
		final Location clone = basis.clone();
		clone.subtract(location);
		return room.isInsideRel(clone.getX(), clone.getY(), clone.getZ());
	}

	public final void save(final ConfigurationSection config, final String path, final boolean includeType)
	{
		if (includeType)
			config.set(path + "type", getClass().getName());
		save(config, path);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		ObjectSaveLoadHelper.saveLocation(config, path + "basis.", basis, true, false);
		room.save(config, path + "room.", true);
	}

	@Override
	protected RealRoom<S> clone()
	{
		return new RealRoom<S>(this);
	}

	public final RealRoom<Room> cloneAsRealRoom()
	{
		return new RealRoom<Room>(room.clone(), basis.clone());
	}

	public void show(final CommandSender target)
	{
		// EDIT add show method
	}

	@Override
	public String toString()
	{
		return "RealRoom {Location: " + basis.getWorld().getName() + ":" + basis.getX() + "," + basis.getY() + "," + basis.getZ() + "; Shape: " + room.toString() + "}";
	}
}
