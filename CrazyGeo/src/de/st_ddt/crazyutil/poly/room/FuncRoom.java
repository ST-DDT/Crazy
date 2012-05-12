package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.region.FlatRegion;

public class FuncRoom
{

	protected FlatRegion region;
	protected double height;
	protected double exponent;
	protected boolean doubleSided;

	public FuncRoom(FlatRegion region, double height, double exponent, boolean doubleSided)
	{
		super();
		this.region = region;
		this.height = Math.abs(height);
		this.exponent = Math.abs(exponent);
		this.doubleSided = doubleSided;
	}

	public FuncRoom(ConfigurationSection config)
	{
		super();
		this.region = FlatRegion.load(config.getConfigurationSection("region"));
		this.height = Math.abs(config.getDouble("height"));
		exponent = Math.abs(config.getDouble("exponent"));
		this.doubleSided = config.getBoolean("doubleSided");
	}

	public boolean isInsideRel(double x, double y, double z)
	{
		if (0 > y || height < y)
			return false;
		return heightScaledRegion(y).isInsideRel(x, z);
	}

	protected double heightScale(double y)
	{
		if (0 > y || height < y)
			return 0;
		return y / height;
	}

	protected double heightScaledSize(double y)
	{
		return Math.pow(heightScale(y), exponent);
	}

	protected FlatRegion heightScaledRegion(double y)
	{
		FlatRegion scaled = region.clone();
		scaled.scale(y);
		return scaled;
	}

	public void expand(final double x, final double y, final double z)
	{
		region.expand(x, z);
		height += Math.abs(y);
	}

	public void contract(final double x, final double y, final double z)
	{
		region.contract(x, z);
	}

	public void scale(final double scale)
	{
		region.scale(scale);
	}

	public FlatRegion getRegion()
	{
		return region;
	}

	public void setRegion(FlatRegion region)
	{
		this.region = region;
	}

	public double getHeight()
	{
		return height;
	}

	public void setHeight(double height)
	{
		this.height = Math.abs(height);
	}

	public double getExponent()
	{
		return exponent;
	}

	public void setExponent(double exponent)
	{
		this.exponent = Math.abs(exponent);
	}

	public boolean isDoubleSided()
	{
		return doubleSided;
	}

	public void setDoubleSided(boolean doubleSided)
	{
		this.doubleSided = doubleSided;
	}

	public final void save(final ConfigurationSection config, final String path, final boolean includeType)
	{
		if (includeType)
			config.set(path + "type", getClass().getName());
		save(config, path);
	}

	public void save(ConfigurationSection config, String path)
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
}
