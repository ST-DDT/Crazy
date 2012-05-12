package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.region.FlatRegion;

public class FuncRoom implements Room
{

	protected FlatRegion region;
	protected double height;
	protected double exponent;
	protected boolean doubleSided;

	public FuncRoom(final FlatRegion region, final double height, final double exponent, final boolean doubleSided)
	{
		super();
		this.region = region;
		this.height = Math.abs(height);
		this.exponent = Math.abs(exponent);
		this.doubleSided = doubleSided;
	}

	public FuncRoom(final ConfigurationSection config)
	{
		super();
		this.region = FlatRegion.load(config.getConfigurationSection("region"));
		this.height = Math.abs(config.getDouble("height"));
		this.exponent = Math.abs(config.getDouble("exponent"));
		this.doubleSided = config.getBoolean("doubleSided");
	}

	@Override
	public boolean isInsideRel(final double x, final double y, final double z)
	{
		if (0 > y || height < y)
			return false;
		return heightScaledRegion(y).isInsideRel(x, z);
	}

	protected double heightScale(double y)
	{
		if (doubleSided)
			y = Math.abs(y);
		if (0 > y || height < y)
			return 0;
		return y / height;
	}

	protected double heightScaledSize(final double y)
	{
		return Math.pow(heightScale(y), exponent);
	}

	protected FlatRegion heightScaledRegion(final double y)
	{
		final FlatRegion scaled = region.clone();
		scaled.scale(y);
		return scaled;
	}

	@Override
	public void expand(final double x, final double y, final double z)
	{
		region.expand(x, z);
		height += Math.abs(y);
	}

	@Override
	public void contract(final double x, final double y, final double z)
	{
		region.contract(x, z);
	}

	@Override
	public void scale(final double scale)
	{
		region.scale(scale);
	}

	public FlatRegion getRegion()
	{
		return region;
	}

	public void setRegion(final FlatRegion region)
	{
		this.region = region;
	}

	public double getHeight()
	{
		return height;
	}

	public double getTotalHeight()
	{
		return height * (doubleSided ? 2 : 1);
	}

	public void setHeight(final double height)
	{
		this.height = Math.abs(height);
	}

	public double getExponent()
	{
		return exponent;
	}

	public void setExponent(final double exponent)
	{
		this.exponent = Math.abs(exponent);
	}

	public boolean isDoubleSided()
	{
		return doubleSided;
	}

	public void setDoubleSided(final boolean doubleSided)
	{
		this.doubleSided = doubleSided;
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
		region.save(config, path + "region");
		config.set(path + "height", height);
		config.set(path + "exponent", exponent);
		config.set(path + "doubleSided", doubleSided);
	}

	@Override
	public FuncRoom clone()
	{
		return new FuncRoom(region, height, exponent, doubleSided);
	}

	@Override
	public boolean equals(final Room room)
	{
		// I know thats dirty!
		if (!(room instanceof FuncRoom))
			return false;
		return equals((FuncRoom) room);
	}

	public boolean equals(final FuncRoom room)
	{
		if (exponent == 0 && room.getExponent() == 0)
			return region.equals(room.getRegion()) && getTotalHeight() == room.getTotalHeight();
		return region.equals(room.getRegion()) && height == room.getHeight() && exponent == room.getExponent() && doubleSided == room.isDoubleSided();
	}
}
