package de.st_ddt.crazyutil.geo.location;

public class GeoLocation implements Comparable<GeoLocation>
{

	protected final int x, y, z;

	public GeoLocation(final int x, final int y, final int z)
	{
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public int compareTo(final GeoLocation o)
	{
		if (x == o.x)
			if (y == o.y)
				if (z == o.z)
					return 0;
				else
					return (z < o.z ? -1 : 1);
			else
				return (y < o.y ? -1 : 1);
		else
			return (x < o.x ? -1 : 1);
	}

	@Override
	public int hashCode()
	{
		return y * 16777216 + z * 4096 + x;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj instanceof GeoLocation)
			return super.equals(obj);
		return false;
	}

	public boolean equals(final GeoLocation o)
	{
		return x == o.x && y == o.y && z == o.z;
	}

	@Override
	public String toString()
	{
		return "x=" + x + ";y=" + y + ";z=" + z;
	}
}
