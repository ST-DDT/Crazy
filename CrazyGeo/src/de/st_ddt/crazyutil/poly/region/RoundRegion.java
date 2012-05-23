package de.st_ddt.crazyutil.poly.region;

import org.bukkit.configuration.ConfigurationSection;

public class RoundRegion extends CircleRegion
{

	protected double radiusZ;

	public RoundRegion(final double radiusX, final double radiusZ)
	{
		super(radiusX);
		this.radiusZ = radiusZ;
	}

	public RoundRegion(final ConfigurationSection config)
	{
		super(config);
		radiusZ = config.getDouble("radiusZ");
	}

	@Override
	public boolean isInsideRel(double x, double z)
	{
		x = Math.abs(x);
		z = Math.abs(z);
		final double angle = Math.abs(Math.atan(z / x));
		final double radiusXPart = Math.cos(angle) * radiusX;
		if (x > radiusXPart)
			return false;
		final double radiusZPart = Math.sin(angle) * radiusZ;
		if (z > radiusZPart)
			return false;
		return true;
	}

	@Override
	public void expand(final double x, final double z)
	{
		this.radiusX += Math.abs(x);
		this.radiusZ += Math.abs(z);
	}

	@Override
	public void contract(final double x, final double z)
	{
		this.radiusX = Math.abs(radiusX - Math.abs(x));
		this.radiusZ = Math.abs(radiusZ - Math.abs(z));
	}

	@Override
	public void scale(final double scale)
	{
		this.radiusX *= Math.abs(scale);
		this.radiusZ *= Math.abs(scale);
	}

	@Override
	public void scale(final double scaleX, final double scaleZ)
	{
		this.radiusX *= Math.abs(scaleX);
		this.radiusZ *= Math.abs(scaleZ);
	}

	@Override
	public double getArea()
	{
		return radiusX * radiusZ * Math.PI;
	}

	@Override
	public double getRadiusZ()
	{
		return radiusZ;
	}

	@Override
	public void setRadiusZ(final double radiusZ)
	{
		this.radiusZ = Math.abs(radiusZ);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "radiusZ", radiusZ);
	}

	@Override
	public RoundRegion clone()
	{
		return new RoundRegion(radiusZ, radiusZ);
	}
}
