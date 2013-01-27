package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.region.BasicRegion;
import de.st_ddt.crazyutil.poly.region.CircleRegion;

public class Sphere extends FuncRoom
{

	public Sphere(final double radius)
	{
		super(new CircleRegion(radius), radius, Math.PI, true);
	}

	public Sphere(final ConfigurationSection config)
	{
		super(config);
		this.exponent = Math.PI;
		this.doubleSided = true;
	}

	@Override
	public boolean isInsideRel(final double x, final double y, final double z)
	{
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) < getRadius();
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
	public void setHeight(final double height)
	{
		this.region = new CircleRegion(height);
		this.height = height;
	}

	public void setRadius(final double range)
	{
		this.region = new CircleRegion(range);
		this.height = range;
	}

	public double getRadius()
	{
		return height;
	}

	@Override
	public void setExponent(final double exponent)
	{
	}

	@Override
	public Sphere clone()
	{
		return new Sphere(height);
	}

	@Override
	public String toString()
	{
		return "Sphere {Radius: " + height + "}";
	}
}
