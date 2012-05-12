package de.st_ddt.crazygeo.worldedit;

import com.sk89q.worldedit.regions.RegionSelector;

import de.st_ddt.crazyutil.poly.room.Room;

public interface WorldEditRoom extends Room
{

	public RegionSelector getRegionSelector();
}
