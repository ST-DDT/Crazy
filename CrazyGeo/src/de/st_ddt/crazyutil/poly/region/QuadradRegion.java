package de.st_ddt.crazyutil.poly.region;

import org.bukkit.configuration.ConfigurationSection;

public class QuadradRegion extends FlatRegion
{

	protected double sizeX;

	public QuadradRegion(final double size)
	{
		super();
		this.sizeX = size;
	}

	public QuadradRegion(final ConfigurationSection config)
	{
		super(config);
		this.sizeX = config.getDouble("sizeX");
	}

	@Override
	public boolean isInsideRel(final double x, final double z)
	{
		return Math.min(x, z) < sizeX;
	}

	@Override
	public void expand(final double x, final double z)
	{
		sizeX += Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
	}

	@Override
	public void contract(final double x, final double z)
	{
		sizeX = Math.abs(sizeX - Math.pow(x, 2) + Math.pow(z, 2));
	}

	@Override
	public void scale(final double scale)
	{
		sizeX *= Math.abs(scale);
	}

	@Override
	public double getArea()
	{
		return Math.pow(sizeX, 2);
	}

	public double getSizeX()
	{
		return sizeX;
	}

	public void setSizeX(final double sizeX)
	{
		this.sizeX = Math.abs(sizeX);
	}

	public double getSizeZ()
	{
		return sizeX;
	}

	public void setSizeZ(final double sizeZ)
	{
		this.sizeX = Math.abs(sizeZ);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "sizeX", sizeX);
	}

	@Override
	public FlatRegion clone()
	{
		return new QuadradRegion(sizeX);
	}

	@Override
	public boolean equals(final FlatRegion region)
	{
		if (region instanceof QuadradRegion)
			return ((QuadradRegion) region).getSizeX() < getSizeX() && ((QuadradRegion) region).getSizeZ() < getSizeZ();
		return false;
	}
}
