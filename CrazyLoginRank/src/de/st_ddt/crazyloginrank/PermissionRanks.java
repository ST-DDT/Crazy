package de.st_ddt.crazyloginrank;

import de.st_ddt.crazyutil.Named;

public class PermissionRanks implements Comparable<PermissionRanks>, Named
{

	protected final String name;
	protected final int rank;

	public PermissionRanks(final String name, final int rank)
	{
		super();
		this.name = name;
		this.rank = rank;
	}

	@Override
	public final String getName()
	{
		return name;
	}

	public final Integer getRank()
	{
		return rank;
	}

	@Override
	public int compareTo(final PermissionRanks o)
	{
		return -getRank().compareTo(o.getRank());
	}

	@Override
	public String toString()
	{
		return name + " Rank: " + rank;
	}
}
