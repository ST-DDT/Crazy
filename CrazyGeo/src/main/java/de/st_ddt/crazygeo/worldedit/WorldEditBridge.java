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
import de.st_ddt.crazyutil.poly.room.Room;

public final class WorldEditBridge
{

	protected static WorldEditPlugin plugin;
	protected final WorldEditAPI we;

	public static WorldEditPlugin getWorldEditPlugin()
	{
		try
		{
			if (plugin == null)
				plugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
			return plugin;
		}
		catch (final Exception e)
		{
			return null;
		}
	}

	public static WorldEditBridge getWorldEditBridge()
	{
		final WorldEditPlugin plugin = getWorldEditPlugin();
		if (plugin == null)
			return null;
		return new WorldEditBridge(plugin);
	}

	private WorldEditBridge(final WorldEditPlugin plugin)
	{
		we = new WorldEditAPI(plugin);
	}

	public RealRoom<FuncRoom> getSavePlayerSelection(final Player player)
	{
		try
		{
			return getSemiSavePlayerSelection(player);
		}
		catch (final CrazyWEUnsupportedGeoRegionTypeException e)
		{
			return null;
		}
	}

	public RealRoom<FuncRoom> getSemiSavePlayerSelection(final Player player) throws CrazyWEUnsupportedGeoRegionTypeException
	{
		try
		{
			return getPlayerSelection(player);
		}
		catch (final WorldEditException e)
		{
			return null;
		}
	}

	public RealRoom<FuncRoom> getPlayerSelection(final Player player) throws WorldEditException, CrazyWEUnsupportedGeoRegionTypeException
	{
		final LocalWorld localWorld = we.getSession(player).getSelectionWorld();
		final Region weregion = we.getSession(player).getSelection(localWorld);
		RealRoom<FuncRoom> result = null;
		final World world = player.getWorld();
		if (weregion instanceof CuboidRegion)
		{
			final CuboidRegion region = (CuboidRegion) weregion;
			final Vector vSize = region.getPos1().subtract(region.getPos2());
			final Vector vMin = region.getMinimumPoint();
			final Location basis = new Location(world, vMin.getX(), vMin.getY(), vMin.getZ());
			final RectangleRegion flat = new RectangleRegion(vSize.getX(), vSize.getZ());
			final FuncRoom room = new PrismRoom(flat, vSize.getY(), false);
			result = new RealRoom<FuncRoom>(room, basis);
		}
		else if (weregion instanceof EllipsoidRegion)
		{
			final EllipsoidRegion region = (EllipsoidRegion) weregion;
			final Vector vSize = region.getRadius();
			final Vector vMin = region.getCenter();
			final Location basis = new Location(world, vMin.getX(), vMin.getY(), vMin.getZ());
			final CircleRegion flat = new RoundRegion(vSize.getX(), vSize.getZ());
			final FuncRoom room = new Elipsoid(flat, vSize.getY());
			result = new RealRoom<FuncRoom>(room, basis);
		}
		else if (weregion instanceof CylinderRegion)
		{
			final CylinderRegion region = (CylinderRegion) weregion;
			final Vector2D vSize = region.getRadius();
			final Vector vMin = region.getCenter();
			final Location basis = new Location(world, vMin.getX(), vMin.getY(), vMin.getZ());
			final CircleRegion flat = new RoundRegion(vSize.getX(), vSize.getZ());
			final FuncRoom room = new PrismRoom(flat, region.getHeight(), false);
			result = new RealRoom<FuncRoom>(room, basis);
		}
		else
			throw new CrazyWEUnsupportedGeoRegionTypeException(localWorld, weregion);
		return result;
	}

	public void setSavePlayerSelection(final Player player, final RealRoom<? extends Room> room)
	{
		if (room.getRoom() instanceof WorldEditRoom)
			setPlayerSelection(player, room.getBasis(), (WorldEditRoom) room.getRoom());
	}

	public void setSemiSavePlayerSelection(final Player player, final RealRoom<? extends Room> room) throws CrazyWEUnsupportedWERegionTypeException
	{
		if (room.getRoom() instanceof WorldEditRoom)
			setPlayerSelection(player, room.getBasis(), (WorldEditRoom) room.getRoom());
		throw new CrazyWEUnsupportedWERegionTypeException(room);
	}

	public void setPlayerSelection(final Player player, final RealRoom<WorldEditRoom> room)
	{
		setPlayerSelection(player, room.getBasis(), room.getRoom());
	}

	public void setPlayerSelection(final Player player, final Location location, final WorldEditRoom region)
	{
		final BukkitWorld bukkitWorld = new BukkitWorld(location.getWorld());
		final RegionSelector selector = region.getRegionSelector();
		we.getSession(player).setRegionSelector(bukkitWorld, selector);
	}
}
