package de.st_ddt.crazygeo.worldedit;

import org.bukkit.Bukkit;
//import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

//import com.sk89q.worldedit.LocalWorld;
//import com.sk89q.worldedit.Vector;
//import com.sk89q.worldedit.Vector2D;
//import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditAPI;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
//import com.sk89q.worldedit.regions.CuboidRegion;
//import com.sk89q.worldedit.regions.CuboidRegionSelector;
//import com.sk89q.worldedit.regions.CylinderRegion;
//import com.sk89q.worldedit.regions.CylinderRegionSelector;
//import com.sk89q.worldedit.regions.EllipsoidRegion;
//import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
//import com.sk89q.worldedit.regions.SphereRegionSelector;

public class WorldEditBridge
{

	protected static WorldEditPlugin plugin;
	protected final WorldEditAPI we;

	public static WorldEditPlugin getWorldEditPlugin()
	{
		if (plugin == null)
			plugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		return plugin;
	}

	public static WorldEditBridge getWorldEditBridge()
	{
		WorldEditPlugin plugin = getWorldEditPlugin();
		if (plugin == null)
			return null;
		return new WorldEditBridge(plugin);
	}

	private WorldEditBridge(WorldEditPlugin plugin)
	{
		we = new WorldEditAPI(plugin);
	}

	// public Room getSavePlayerSelection(Player player)
	// {
	// try
	// {
	// return getPlayerSelection(player);
	// }
	// catch (WorldEditException e)
	// {
	// return null;
	// }
	// }
	// public Geo getPlayerSelection(Player player) throws WorldEditException
	// {
	// LocalWorld localWorld = we.getSession(player).getSelectionWorld();
	// Region region = we.getSession(player).getSelection(localWorld);
	// Geo geo = null;
	// World world = player.getWorld();
	// if (region instanceof CuboidRegion)
	// {
	// CuboidRegion region2 = (CuboidRegion) region;
	// Vector v1 = region2.getPos1();
	// Vector v2 = region2.getPos2();
	// Location location1 = new Location(world, v1.getX(), v1.getY(), v1.getZ());
	// Location location2 = new Location(world, v2.getX(), v2.getY(), v2.getZ());
	// geo = new Cuboid(world, location1, location2);
	// }
	// else if (region instanceof EllipsoidRegion)
	// {
	// EllipsoidRegion region2 = (EllipsoidRegion) region;
	// Vector v1 = region2.getCenter();
	// Vector v2 = region2.getRadius();
	// Location location1 = new Location(world, v1.getX(), v1.getY(), v1.getZ());
	// geo = new Sphere(location1, v2.length());
	// }
	// else if (region instanceof CylinderRegion)
	// {
	// CylinderRegion region2 = (CylinderRegion) region;
	// Vector v1 = region2.getCenter();
	// Vector2D v2 = region2.getRadius();
	// int height = region2.getMaximumY() - region2.getMinimumY() + 1;
	// Location location1 = new Location(world, v1.getX(), region2.getMinimumY(), v1.getZ());
	// geo = new Cylinder(location1, v2.length(), height);
	// }
	// return geo;
	// }
	
	public void setPlayerSelection(Player player, World world, WorldEditRegionable region)
	{
		BukkitWorld bukkitWorld = new BukkitWorld(world);
		RegionSelector selector = region.getRegionSelector();
		we.getSession(player).setRegionSelector(bukkitWorld, selector);
	}
}
