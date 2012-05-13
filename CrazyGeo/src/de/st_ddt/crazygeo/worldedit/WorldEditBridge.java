package de.st_ddt.crazygeo.worldedit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditAPI;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.regions.EllipsoidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;

import de.st_ddt.crazygeo.region.RealRoom;
import de.st_ddt.crazyutil.poly.region.CircleRegion;
import de.st_ddt.crazyutil.poly.region.RectangleRegion;
import de.st_ddt.crazyutil.poly.region.RoundRegion;
import de.st_ddt.crazyutil.poly.room.Elipsoid;
import de.st_ddt.crazyutil.poly.room.FuncRoom;
import de.st_ddt.crazyutil.poly.room.PrismRoom;

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

	public RealRoom<FuncRoom> getSavePlayerSelection(Player player)
	{
		try
		{
			return getPlayerSelection(player);
		}
		catch (WorldEditException e)
		{
			return null;
		}
	}

	public RealRoom<FuncRoom> getPlayerSelection(Player player) throws WorldEditException
	{
		LocalWorld localWorld = we.getSession(player).getSelectionWorld();
		Region weregion = we.getSession(player).getSelection(localWorld);
		RealRoom<FuncRoom> result = null;
		World world = player.getWorld();
		if (weregion instanceof CuboidRegion)
		{
			final CuboidRegion region = (CuboidRegion) weregion;
			Vector vSize = region.getPos1().subtract(region.getPos2());
			Vector vMin = region.getMinimumPoint();
			Location basis = new Location(world, vMin.getX(), vMin.getY(), vMin.getZ());
			final RectangleRegion flat = new RectangleRegion(vSize.getX(), vSize.getZ());
			FuncRoom room = new PrismRoom(flat, vSize.getY(), false);
			result = new RealRoom<FuncRoom>(room, basis);
		}
		else if (weregion instanceof EllipsoidRegion)
		{
			EllipsoidRegion region = (EllipsoidRegion) weregion;
			Vector vSize = region.getRadius();
			Vector vMin = region.getCenter();
			Location basis = new Location(world, vMin.getX(), vMin.getY(), vMin.getZ());
			final CircleRegion flat = new RoundRegion(vSize.getX(), vSize.getZ());
			FuncRoom room = new Elipsoid(flat, vSize.getY());
			result = new RealRoom<FuncRoom>(room, basis);
		}
		else if (weregion instanceof CylinderRegion)
		{
			CylinderRegion region = (CylinderRegion) weregion;
			Vector2D vSize = region.getRadius();
			Vector vMin = region.getCenter();
			Location basis = new Location(world, vMin.getX(), vMin.getY(), vMin.getZ());
			final CircleRegion flat = new RoundRegion(vSize.getX(), vSize.getZ());
			FuncRoom room = new PrismRoom(flat, region.getHeight(), false);
			result = new RealRoom<FuncRoom>(room, basis);
		}
		return result;
	}

	public void setPlayerSelection(Player player, RealRoom<WorldEditRoom> room)
	{
		setPlayerSelection(player, room.getBasis(), room.getRoom());
	}

	public void setPlayerSelection(Player player, Location location, WorldEditRoom region)
	{
		BukkitWorld bukkitWorld = new BukkitWorld(location.getWorld());
		RegionSelector selector = region.getRegionSelector();
		we.getSession(player).setRegionSelector(bukkitWorld, selector);
	}
}
