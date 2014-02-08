package de.st_ddt.crazyutil.geo.block;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;

public class GeoBlockBag
{

	protected final Set<GeoBlock> blocks = new HashSet<GeoBlock>();

	public GeoBlockBag()
	{
		super();
	}

	public Set<GeoBlock> getBlocks()
	{
		return blocks;
	}

	public void apply(final Location target, final boolean physics)
	{
		for (final GeoBlock block : blocks)
			block.apply(target, physics);
	}
}
