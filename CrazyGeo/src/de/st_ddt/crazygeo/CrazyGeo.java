package de.st_ddt.crazygeo;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.st_ddt.crazygeo.commands.CrazyGeoCommandWorldEditExport;
import de.st_ddt.crazygeo.commands.CrazyGeoCommandWorldEditImport;
import de.st_ddt.crazygeo.region.RealRoom;
import de.st_ddt.crazygeo.worldedit.WorldEditBridge;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.poly.room.FuncRoom;
import de.st_ddt.crazyutil.poly.room.Room;

public class CrazyGeo extends CrazyPlugin
{

	private static CrazyGeo plugin;
	protected final HashMap<String, RealRoom<? extends Room>> geos = new HashMap<String, RealRoom<? extends Room>>();
	protected final HashMap<String, Class<Room>> type = new HashMap<String, Class<Room>>();
	protected WorldEditBridge weBridge;

	public static CrazyGeo getPlugin()
	{
		return plugin;
	}

	@Override
	public void onLoad()
	{
		plugin = this;
		super.onLoad();
	}

	@Override
	public void onEnable()
	{
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldEdit"))
			this.weBridge = WorldEditBridge.getWorldEditBridge();
		super.onEnable();
		registerCommands();
	}

	private void registerCommands()
	{
		mainCommand.addSubCommand(new CrazyGeoCommandWorldEditImport(this), "we", "wei", "weimport", "worldeditimport");
		mainCommand.addSubCommand(new CrazyGeoCommandWorldEditExport(this), "wee", "weexport", "worldeditexport");
	}

	/**
	 * Import a region from WorldEdit
	 * 
	 * @param player
	 *            The Player whos selection should be imported
	 * @throws CrazyException
	 */
	public void importWE(final Player player) throws CrazyException
	{
		if (weBridge == null)
			return;
		final RealRoom<FuncRoom> region = weBridge.getSemiSavePlayerSelection(player);
		geos.put(player.getName().toLowerCase(), region);
	}

	/**
	 * Import a region from WorldEdit
	 * 
	 * @param player
	 *            The Player whos selection should be imported
	 * @return
	 * @throws CrazyException
	 */
	public RealRoom<FuncRoom> directImportWE(final Player player) throws CrazyException
	{
		if (weBridge == null)
			return null;
		return weBridge.getSemiSavePlayerSelection(player);
	}

	/**
	 * Export a region to WorldEdit
	 * 
	 * @param player
	 *            The Player whos selection should be exported
	 * @throws CrazyException
	 */
	public void exportWE(final Player player) throws CrazyException
	{
		if (weBridge == null)
			return;
		final RealRoom<? extends Room> region = getPlayerSelection(player);
		weBridge.setSemiSavePlayerSelection(player, region);
	}

	/**
	 * Export a region to WorldEdit
	 * 
	 * @param player
	 *            The Player whos selection should be exported
	 * @throws CrazyException
	 */
	public void directExportWE(final Player player, final RealRoom<? extends Room> region) throws CrazyException
	{
		if (weBridge == null)
			return;
		weBridge.setSemiSavePlayerSelection(player, region);
	}

	public RealRoom<Room> getPlayerSelection(final Player player)
	{
		return geos.get(player.getName().toLowerCase()).cloneAsRealRoom();
	}

	public void setPlayerSelection(final Player player, final RealRoom<? extends Room> room)
	{
		geos.put(player.getName().toLowerCase(), room);
	}

	public void clearPlayerSelection(final Player player)
	{
		geos.remove(player.getName().toLowerCase());
	}

	public RealRoom<Room> getAndClearPlayerSelection(final Player player)
	{
		return geos.remove(player.getName().toLowerCase()).cloneAsRealRoom();
	}

	public WorldEditBridge getWorldEditBridge()
	{
		return weBridge;
	}
}
