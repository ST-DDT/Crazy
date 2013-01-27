package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.region.BasicRegion;
import de.st_ddt.crazyutil.poly.region.Region;

public class FuncRoom extends BasicRoom
{

	protected Region region;
	protected double height;
	protected double exponent;
	protected boolean doubleSided;

	public FuncRoom(final Region region, final double height, final double exponent, final boolean doubleSided)
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
		this.region = BasicRegion.load(config.getConfigurationSection("region"));
		this.height = Math.abs(config.getDouble("height"));
		this.exponent = Math.abs(config.getDouble("exponent"));
		this.doubleSided = config.getBoolean("doubleSided");
	}

	@Override
	public boolean isInsideRel(final double x, double y, final double z)
	{
		if (doubleSided)
			y = Math.abs(y);
		if (height < 0)
			y *= -1;
		if (0 > y || height < y)
			return false;
		return heightScaledRegion(y).isInsideRel(x, z);
	}

	protected double heightScale(final double y)
	{
		if (0 > y || height < y)
			return 0;
		return y / height;
	}

	protected double heightScaledSize(final double y)
	{
		if (exponent == 0)
			return 1;
		return Math.pow(heightScale(y), 1 / exponent);
	}

	protected Region heightScaledRegion(final double y)
	{
		final Region scaled = region.clone();
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
		height -= y;
	}

	@Override
	public void scale(final double scale)
	{
		region.scale(scale);
		height *= scale;
	}

	@Override
	public void scale(final double scaleX, final double scaleY, final double scaleZ)
	{
		region.scale(scaleX, scaleZ);
		height *= scaleY;
	}

	public Region getRegion()
	{
		return region;
	}

	public void setRegion(final BasicRegion region)
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
	public void save(final ConfigurationSection config, final String path)
	{
		region.save(config, path + "region.", true);
		config.set(path + "height", height);
		config.set(path + "exponent", exponent);
		config.set(path + "doubleSided", doubleSided);
	}

	@Override
	public FuncRoom clone()
	{
		return new FuncRoom(region, height, exponent, doubleSided);
	}

	public final FuncRoom cloneAsFuncRoom()
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

	@Override
	public String toString()
	{
		if (doubleSided)
			return "FuncRoom {Region: " + region.toString() + "; Height: " + height + "; Exponent: " + exponent + ";DOUBLESIDED}";
		else
			return "FuncRoom {Region: " + region.toString() + "; Height: " + height + "; Exponent: " + exponent + "}";
	}
}
