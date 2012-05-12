package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.region.CircleRegion;
import de.st_ddt.crazyutil.poly.region.FlatRegion;

public class Sphere extends FuncRoom
{

	public Sphere(double radius)
	{
		super(new CircleRegion(radius), radius, Math.PI, true);
	}

	public Sphere(ConfigurationSection config)
	{
		super(config);
		this.exponent = Math.PI;
		this.doubleSided = true;
	}

	@Override
	public boolean isInsideRel(double x, double y, double z)
	{
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) < getRadius();
	}

	private double getRadius()
	{
		return height;
	}

	@Override
	public void setExponent(double exponent)
	{
	}

	@Override
	public CircleRegion getRegion()
	{
		return (CircleRegion) super.getRegion();
	}

	@Override
	public void setRegion(FlatRegion region)
	{
		if (region instanceof CircleRegion)
			super.setRegion(region);
	}

	@Override
	public Sphere clone()
	{
		return new Sphere(height);
	}
}
