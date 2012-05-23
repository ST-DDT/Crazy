package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.region.Region;

public class RoundedRoom extends FuncRoom
{

	public RoundedRoom(final Region region, final double height, final boolean doubleSided)
	{
		super(region, height, Math.PI, doubleSided);
	}

	public RoundedRoom(final ConfigurationSection config)
	{
		super(config);
		this.exponent = Math.PI;
	}

	@Override
	public void setExponent(final double exponent)
	{
	}

	@Override
	public RoundedRoom clone()
	{
		return new RoundedRoom(region, height, doubleSided);
	}
}
