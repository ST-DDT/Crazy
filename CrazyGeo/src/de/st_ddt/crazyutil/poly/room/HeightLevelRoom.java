package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

public class HeightLevelRoom extends BasicRoom
{

	private double yLevel;
	private final boolean above;

	public HeightLevelRoom(final double y, final boolean above)
	{
		super();
		this.yLevel = y;
		this.above = above;
	}

	public HeightLevelRoom(final ConfigurationSection config)
	{
		super();
		yLevel = config.getDouble("yLevel", 0);
		above = config.getBoolean("above", true);
	}

	@Override
	public boolean isInsideRel(final double x, final double y, final double z)
	{
		return y > yLevel == above || y == yLevel;
	}

	@Override
	public void scale(final double scale)
	{
	}

	@Override
	public void scale(final double scaleX, final double scaleY, final double scaleZ)
	{
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "yLevel", yLevel);
		config.set(path + "above", above);
	}

	@Override
	public boolean equals(final Room room)
	{
		if (room instanceof HeightLevelRoom)
			return equals((HeightLevelRoom) room);
		else
			return false;
	}

	public boolean equals(final HeightLevelRoom room)
	{
		return yLevel == room.yLevel && above == room.above;
	}

	@Override
	public void expand(final double x, final double y, final double z)
	{
		yLevel += y;
	}

	@Override
	public void contract(final double x, final double y, final double z)
	{
		yLevel -= y;
	}

	@Override
	public HeightLevelRoom clone()
	{
		return new HeightLevelRoom(yLevel, above);
	}
}
