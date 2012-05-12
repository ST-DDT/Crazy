package de.st_ddt.crazyutil.poly.region;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.vector.Vector2D;

public class QuadradRegion extends FlatRegion
{

	protected double sizeX;

	public QuadradRegion(double size)
	{
		super();
		this.sizeX = size;
	}

	public QuadradRegion(ConfigurationSection config)
	{
		super(config);
		this.sizeX = config.getDouble("sizeX");
	}

	@Override
	public boolean isInsideRel(double x, double z)
	{
		return Math.min(x, z) < sizeX;
	}

	@Override
	public void expand(double x, double z)
	{
		sizeX += new Vector2D(x, z).length();
	}

	@Override
	public void contract(double x, double z)
	{
		sizeX = Math.abs(sizeX - new Vector2D(x, z).length());
	}

	@Override
	public void scale(double scale)
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

	public void setSizeX(double sizeX)
	{
		this.sizeX = Math.abs(sizeX);
	}

	public double getSizeZ()
	{
		return sizeX;
	}

	public void setSizeZ(double sizeZ)
	{
		this.sizeX = Math.abs(sizeZ);
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		config.set(path + "sizeX", sizeX);
	}

	@Override
	public FlatRegion clone()
	{
		return new QuadradRegion(sizeX);
	}

	@Override
	public boolean equals(FlatRegion region)
	{
		if (region instanceof QuadradRegion)
			return ((QuadradRegion) region).getSizeX() < getSizeX() && ((QuadradRegion) region).getSizeZ() < getSizeZ();
		return false;
	}
}
