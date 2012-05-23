package de.st_ddt.crazyutil.poly.room.extended;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.room.Room;

public class OffsetRoom extends ExtendedRoom
{

	protected double offsetX, offsetY, offsetZ;

	public OffsetRoom(final Room room)
	{
		super(room);
		this.offsetX = 0;
		this.offsetY = 0;
		this.offsetZ = 0;
		this.room = room;
	}

	public OffsetRoom(final Room room, final double offsetX, final double offsetY, final double offsetZ)
	{
		super(room);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
	}

	public OffsetRoom(final ConfigurationSection config)
	{
		super(config);
		this.offsetX = config.getDouble("offsetX", 0);
		this.offsetY = config.getDouble("offsetY", 0);
		this.offsetZ = config.getDouble("offsetZ", 0);
	}

	public double getOffsetX()
	{
		return offsetX;
	}

	public void setOffsetX(final double offsetX)
	{
		this.offsetX = offsetX;
	}

	public double getOffsetY()
	{
		return offsetY;
	}

	public void setOffsetY(final double offsetY)
	{
		this.offsetY = offsetY;
	}

	public double getOffsetZ()
	{
		return offsetZ;
	}

	public void setOffsetZ(final double offsetZ)
	{
		this.offsetZ = offsetZ;
	}

	@Override
	public boolean isInsideRel(final double x, final double y, final double z)
	{
		return room.isInsideRel(x - offsetX, y - offsetY, z - offsetZ);
	}

	@Override
	public void scale(final double scale)
	{
		super.scale(scale);
		offsetX *= scale;
		offsetY *= scale;
		offsetZ *= scale;
	}

	@Override
	public void scale(final double scaleX, final double scaleY, final double scaleZ)
	{
		super.scale(scaleX, scaleY, scaleZ);
		offsetX *= scaleX;
		offsetY *= scaleY;
		offsetZ *= scaleZ;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "offsetX", offsetX);
		config.set(path + "offsetY", offsetX);
		config.set(path + "offsetZ", offsetX);
	}

	@Override
	public boolean equals(final Room room)
	{
		if (room instanceof OffsetRoom)
			return equals((OffsetRoom) room);
		return this.room.equals(room) && offsetX == 0 && offsetY == 0 && offsetZ == 0;
	}

	public boolean equals(final OffsetRoom room)
	{
		return this.room.equals(room) && offsetX == room.getOffsetX() && offsetY == room.getOffsetY() && offsetZ == room.getOffsetZ();
	}

	@Override
	public OffsetRoom clone()
	{
		return new OffsetRoom(room.clone(), offsetX, offsetY, offsetZ);
	}

	public final OffsetRoom cloneAsOffsetRoom()
	{
		return new OffsetRoom(room.clone(), offsetX, offsetY, offsetZ);
	}
}
