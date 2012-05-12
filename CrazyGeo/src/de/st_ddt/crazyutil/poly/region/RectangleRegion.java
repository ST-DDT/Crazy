package de.st_ddt.crazyutil.poly.region;

import org.bukkit.configuration.ConfigurationSection;

public class RectangleRegion extends QuadradRegion
{

	protected double sizeZ;

	public RectangleRegion(double sizeX, double sizeZ)
	{
		super(sizeX);
		this.sizeZ = sizeZ;
	}

	public RectangleRegion(ConfigurationSection config)
	{
		super(config);
		config.getDouble("sizeZ");
	}

	@Override
	public boolean isInsideRel(double x, double z)
	{
		return x <= sizeX && z <= sizeZ;
	}

	@Override
	public void expand(double x, double z)
	{
		sizeX += Math.abs(x);
		sizeZ += Math.abs(z);
	}

	@Override
	public void contract(double x, double z)
	{
		sizeX = Math.abs(sizeX - x);
		sizeZ = Math.abs(sizeZ - z);
	}

	@Override
	public void scale(double scale)
	{
		sizeX *= Math.abs(scale);
		sizeZ *= Math.abs(scale);
	}

	@Override
	public double getSizeZ()
	{
		return sizeZ;
	}

	@Override
	public void setSizeZ(double sizeZ)
	{
		this.sizeZ = Math.abs(sizeZ);
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		super.save(config, path);
		config.set(path + "sizeZ", sizeZ);
	}

	@Override
	public FlatRegion clone()
	{
		return new RectangleRegion(sizeX, sizeZ);
	}
}
