package de.st_ddt.crazyutil.geo.location;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;

import de.st_ddt.crazyutil.geo.block.GeoBlock;
import de.st_ddt.crazyutil.geo.block.GeoBlockBag;

public class GeoLocationBag
{

	protected final Set<GeoLocation> locations = new HashSet<GeoLocation>();

	public void merge(final GeoLocationBag bag)
	{
		this.locations.addAll(bag.getLocations());
	}

	public Set<GeoLocation> getLocations()
	{
		return locations;
	}

	public GeoBlockBag getBlocks(final Location basis)
	{
		final GeoBlockBag res = new GeoBlockBag();
		final Set<GeoBlock> blocks = res.getBlocks();
		for (final GeoLocation location : locations)
			blocks.add(new GeoBlock(basis.clone().add(location.x, location.y, location.z).getBlock()));
		return res;
	}
}
