package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.region.BasicRegion;
import de.st_ddt.crazyutil.poly.region.CircleRegion;

public class Elipsoid extends FuncRoom
{

	public Elipsoid(final CircleRegion region, final double height)
	{
		super(region, height, Math.PI, true);
	}

	public Elipsoid(final ConfigurationSection config)
	{
		super(config);
		this.exponent = Math.PI;
		this.doubleSided = true;
	}

	@Override
	public CircleRegion getRegion()
	{
		return (CircleRegion) super.getRegion();
	}

	@Override
	public void setRegion(final BasicRegion region)
	{
		if (region instanceof CircleRegion)
			super.setRegion(region);
	}

	@Override
	public void setExponent(final double exponent)
	{
	}

	@Override
	public Elipsoid clone()
	{
		return new Elipsoid(((CircleRegion) region).clone(), height);
	}
}
