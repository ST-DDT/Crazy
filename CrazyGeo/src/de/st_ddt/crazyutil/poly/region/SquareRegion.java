package de.st_ddt.crazyutil.poly.region;

import org.bukkit.configuration.ConfigurationSection;

public class SquareRegion extends BasicRegion
{

	protected double sizeX;

	public SquareRegion(final double size)
	{
		super();
		this.sizeX = size;
	}

	public SquareRegion(final ConfigurationSection config)
	{
		super(config);
		this.sizeX = config.getDouble("sizeX");
	}

	@Override
	public boolean isInsideRel(final double x, final double z)
	{
		return Math.min(Math.abs(x), Math.abs(z)) <= sizeX / 2;
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
	public void scale(final double scaleX, final double scaleZ)
	{
		sizeX *= Math.abs(Math.sqrt(Math.pow(scaleX, 2) + Math.pow(scaleZ, 2)));
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
	public SquareRegion clone()
	{
		return new SquareRegion(sizeX);
	}

	@Override
	public boolean equals(final Region region)
	{
		if (region instanceof SquareRegion)
			return ((SquareRegion) region).getSizeX() < getSizeX() && ((SquareRegion) region).getSizeZ() < getSizeZ();
		return false;
	}

	@Override
	public String toString()
	{
		return "Square {Size: " + sizeX + "}";
	}
}
