package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.region.FlatRegion;

public class PyramidRoom extends FuncRoom
{

	public PyramidRoom(final FlatRegion region, final double height, final boolean doubleSided)
	{
		super(region, height, 1, doubleSided);
	}

	public PyramidRoom(final ConfigurationSection config)
	{
		super(config);
		this.exponent = 1;
	}

	@Override
	public void setExponent(final double exponent)
	{
	}

	@Override
	public PyramidRoom clone()
	{
		return new PyramidRoom(region, height, doubleSided);
	}
}
