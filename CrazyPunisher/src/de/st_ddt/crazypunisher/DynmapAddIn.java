package de.st_ddt.crazypunisher;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;

import org.dynmap.DynmapAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import de.st_ddt.crazygeo.region.RealRoom;
import de.st_ddt.crazyutil.poly.room.Room;

public class DynmapAddIn
{

	protected final DynmapAPI dynmapApi;
	protected final CrazyPunisher plugin;
	protected MarkerIcon icon = null;
	protected MarkerSet markerSet = null;

	public DynmapAddIn(DynmapAPI dynmapApi, CrazyPunisher plugin)
	{
		super();
		this.dynmapApi = dynmapApi;
		if (dynmapApi==null)
			throw new NullPointerException();
		this.plugin = plugin;
		// Initialize
		MarkerAPI markerAPI = dynmapApi.getMarkerAPI();
		MarkerIcon icon = markerAPI.getMarkerIcon("crazypunisher_jail");
		HashSet<MarkerIcon> icons = new HashSet<MarkerIcon>();
		if (icon == null)
		{
			InputStream stream = null;
			try
			{
				stream = new URL(plugin.getMainDownloadLocation() + "/images/jail.png").openStream();
				icon = markerAPI.createMarkerIcon("crazypunisher_jail", "Jail", stream);
				icons.add(icon);
				markerSet = markerAPI.createMarkerSet("CrazyPunisher", "CrazyPunisher", icons, false);
			}
			catch (IOException e)
			{
				//
			}
		}
	}

	public DynmapAPI getDynmapApi()
	{
		return dynmapApi;
	}

	public void updateMarkers()
	{
		if (icon == null || markerSet == null)
			return;
		for (Marker marker : markerSet.getMarkers())
			marker.deleteMarker();
		int anz = 0;
		for (RealRoom<? extends Room> jail : plugin.getJails())
		{
			markerSet.createMarker("crazypunisher_jailIco" + (anz++), "Jail", jail.getWorld().getName(), jail.getBasis().getX(), jail.getBasis().getY(), jail.getBasis().getZ(), icon, false);
			// draw JailRegion?
		}
	}
}
