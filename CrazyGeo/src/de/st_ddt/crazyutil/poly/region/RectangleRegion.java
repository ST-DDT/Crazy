package de.st_ddt.crazyutil.poly.region;

import org.bukkit.configuration.ConfigurationSection;

public class RectangleRegion extends SquareRegion
{

	protected double sizeZ;

	public RectangleRegion(final double sizeX, final double sizeZ)
	{
		super(sizeX);
		this.sizeZ = sizeZ;
	}

	public RectangleRegion(final ConfigurationSection config)
	{
		super(config);
		this.sizeZ = config.getDouble("sizeZ");
	}

	@Override
	public boolean isInsideRel(final double x, final double z)
	{
		return Math.abs(x) <= sizeX / 2 && Math.abs(z) <= sizeZ / 2;
	}

	@Override
	public void expand(final double x, final double z)
	{
		sizeX += Math.abs(x);
		sizeZ += Math.abs(z);
	}

	@Override
	public void contract(final double x, final double z)
	{
		sizeX = Math.abs(sizeX - x);
		sizeZ = Math.abs(sizeZ - z);
	}

	@Override
	public void scale(final double scale)
	{
		sizeX *= Math.abs(scale);
		sizeZ *= Math.abs(scale);
	}

	@Override
	public void scale(final double scaleX, final double scaleZ)
	{
		this.sizeX *= Math.abs(scaleX);
		this.sizeZ *= Math.abs(scaleZ);
	}

	@Override
	public double getSizeZ()
	{
		return sizeZ;
	}

	@Override
	public void setSizeZ(final double sizeZ)
	{
		this.sizeZ = Math.abs(sizeZ);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		config.set(path + "sizeZ", sizeZ);
	}

	@Override
	public RectangleRegion clone()
	{
		return new RectangleRegion(sizeX, sizeZ);
	}

	@Override
	public String toString()
	{
		return "Rectangle {SizeX: " + sizeX + "; SizeZ: " + sizeZ + "}";
	}
}
