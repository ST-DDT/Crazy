package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.region.FlatRegion;

public class RoundedRoom extends FuncRoom
{

	public RoundedRoom(FlatRegion region, double height, boolean doubleSided)
	{
		super(region, height, Math.PI, doubleSided);
	}

	public RoundedRoom(ConfigurationSection config)
	{
		super(config);
		this.exponent = Math.PI;
	}

	@Override
	public void setExponent(double exponent)
	{
	}

	@Override
	public RoundedRoom clone()
	{
		return new RoundedRoom(region, height, doubleSided);
	}
}
