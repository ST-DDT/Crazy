package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.region.FlatRegion;

public class PrismRoom extends FuncRoom
{

	public PrismRoom(FlatRegion region, double height, boolean doubleSided)
	{
		super(region, height, 0, doubleSided);
	}

	public PrismRoom(ConfigurationSection config)
	{
		super(config);
		this.exponent = 0;
	}

	public boolean isInsideRel(final double x, final double y, final double z)
	{
		return region.isInsideRel(x, z) && y < height;
	}

	@Override
	public void setExponent(double exponent)
	{
	}

	@Override
	public PrismRoom clone()
	{
		return new PrismRoom(region, height, doubleSided);
	}
}
