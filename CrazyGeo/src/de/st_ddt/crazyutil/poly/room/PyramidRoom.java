package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.region.FlatRegion;

public class PyramidRoom extends FuncRoom
{

	public PyramidRoom(FlatRegion region, double height, boolean doubleSided)
	{
		super(region, height, 1, doubleSided);
	}

	public PyramidRoom(ConfigurationSection config)
	{
		super(config);
		this.exponent = 1;
	}

	@Override
	public void setExponent(double exponent)
	{
	}

	@Override
	public PyramidRoom clone()
	{
		return new PyramidRoom(region, height, doubleSided);
	}
}
