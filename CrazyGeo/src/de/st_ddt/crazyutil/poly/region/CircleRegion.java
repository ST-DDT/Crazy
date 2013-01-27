package de.st_ddt.crazyutil.poly.region;

import org.bukkit.configuration.ConfigurationSection;

public class CircleRegion extends BasicRegion
{

	protected double radiusX;

	public CircleRegion(final double radius)
	{
		super();
		this.radiusX = radius;
	}

	public CircleRegion(final ConfigurationSection config)
	{
		super(config);
		radiusX = config.getDouble("radiusX");
	}

	@Override
	public boolean isInsideRel(final double x, final double z)
	{
		return Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2)) < radiusX;
	}

	@Override
	public void expand(final double x, final double z)
	{
		radiusX += Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
	}

	@Override
	public void contract(final double x, final double z)
	{
		radiusX = Math.abs(radiusX - Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2)));
	}

	@Override
	public void scale(final double scale)
	{
		radiusX *= Math.abs(scale);
	}

	@Override
	public void scale(final double scaleX, final double scaleZ)
	{
		radiusX *= Math.abs(Math.sqrt(Math.pow(scaleX, 2) + Math.pow(scaleZ, 2)));
	}

	@Override
	public double getArea()
	{
		return Math.pow(radiusX, 2) * Math.PI;
	}

	public double getRadiusX()
	{
		return radiusX;
	}

	public void setRadiusX(final double radiusX)
	{
		this.radiusX = Math.abs(radiusX);
	}

	public double getRadiusZ()
	{
		return radiusX;
	}

	public void setRadiusZ(final double radiusZ)
	{
		this.radiusX = Math.abs(radiusZ);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "radiusX", radiusX);
	}

	@Override
	public CircleRegion clone()
	{
		return new CircleRegion(radiusX);
	}

	@Override
	public boolean equals(final Region region)
	{
		if (region instanceof CircleRegion)
			return ((CircleRegion) region).getRadiusX() == getRadiusX() && ((CircleRegion) region).getRadiusZ() == getRadiusZ();
		return false;
	}

	@Override
	public String toString()
	{
		return "Circle {Radius: " + radiusX + "}";
	}
}
