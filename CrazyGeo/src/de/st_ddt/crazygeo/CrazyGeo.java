package de.st_ddt.crazygeo;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazygeo.region.RealRoom;
import de.st_ddt.crazygeo.worldedit.WorldEditBridge;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
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
	protected String getShortPluginName()
	{
		return "geo";
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		if (Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") != null)
			this.weBridge = WorldEditBridge.getWorldEditBridge();
		super.onEnable();
	}

	@Override
	public boolean command(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		if (sender instanceof ConsoleCommandSender)
			throw new CrazyCommandExecutorException(false);
		return super.command(sender, commandLabel, args);
	}

	public boolean command(final Player player, final String commandLabel, final String[] args) throws CrazyException
	{
		return false;
	}

	public void commandInfo(final Player player, final String[] newArgs)
	{
		// RealRoom<? extends Room> region = getPlayerSelection(player);
		// region.getBasis();
		// region.getRoom();
		// Rotated?
	}

	@Override
	public boolean commandMain(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		if (sender instanceof ConsoleCommandSender)
			throw new CrazyCommandExecutorException(false);
		return super.commandMain(sender, commandLabel, args);
	}

	public boolean commandMain(final Player player, final String commandLabel, final String[] args) throws CrazyException
	{
		// if (commandLabel.equalsIgnoreCase("selType"))
		// {
		// commandMainSelect(player, args);
		// return true;
		// }
		if (commandLabel.equalsIgnoreCase("we") || commandLabel.equalsIgnoreCase("wei") || commandLabel.equalsIgnoreCase("weimport"))
		{
			commandMainWEImport(player, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("wee") || commandLabel.equalsIgnoreCase("weexport"))
		{
			commandMainWEExport(player, args);
			return true;
		}
		return false;
	}

	// private void commandMainSelect(Player player, String[] args)
	// {
	// }
	private void commandMainWEImport(final Player player, final String[] args) throws CrazyException
	{
		if (weBridge == null)
			throw new CrazyCommandCircumstanceException("when WorldEdit is enabled!");
		final RealRoom<FuncRoom> region = weBridge.getSemiSavePlayerSelection(player);
		geos.put(player.getName().toLowerCase(), region);
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

	private void commandMainWEExport(final Player player, final String[] args) throws CrazyException
	{
		if (weBridge == null)
			throw new CrazyCommandCircumstanceException("when WorldEdit is enabled!");
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
	public void exportWE(final Player player) throws CrazyException
	{
		if (weBridge == null)
			return;
		final RealRoom<? extends Room> region = getPlayerSelection(player);
		weBridge.setSemiSavePlayerSelection(player, region);
	}

	public RealRoom<? extends Room> getPlayerSelection(final Player player)
	{
		return geos.get(player.getName().toLowerCase());
	}

	public void setPlayerSelection(final Player player, final RealRoom<? extends Room> room)
	{
		geos.put(player.getName().toLowerCase(), room);
	}

	public void clearPlayerSelection(final Player player)
	{
		geos.remove(player.getName().toLowerCase());
	}
}
