package de.st_ddt.crazygeo;

import java.util.HashMap;

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

	protected final HashMap<Player, RealRoom<? extends Room>> geos = new HashMap<Player, RealRoom<? extends Room>>();
	protected final HashMap<String, Class<Room>> type = new HashMap<String, Class<Room>>();
	protected WorldEditBridge webridge;

	@Override
	public void onEnable()
	{
		this.webridge = WorldEditBridge.getWorldEditBridge();
		super.onEnable();
	}

	@Override
	public boolean command(CommandSender sender, String commandLabel, String[] args) throws CrazyException
	{
		if (sender instanceof ConsoleCommandSender)
			throw new CrazyCommandExecutorException(false);
		return super.command((Player) sender, commandLabel, args);
	}

	public boolean command(Player sender, String commandLabel, String[] args) throws CrazyException
	{
		return false;
	}

	public void commandInfo(Player sender, String[] newArgs)
	{
		// EDIT Auto-generated method stub
	}

	@Override
	public boolean commandMain(CommandSender sender, String commandLabel, String[] args) throws CrazyException
	{
		if (sender instanceof ConsoleCommandSender)
			throw new CrazyCommandExecutorException(false);
		return super.commandMain((Player) sender, commandLabel, args);
	}

	public boolean commandMain(final Player sender, final String commandLabel, final String[] args) throws CrazyException
	{
		if (commandLabel.equalsIgnoreCase("selType"))
		{
			commandMainSelect(sender, args);
			return true;
		}
		if (commandLabel.equalsIgnoreCase("we") || commandLabel.equalsIgnoreCase("weimport"))
		{
			commandMainWEImport(sender, args);
			return true;
		}
		return false;
	}

	private void commandMainSelect(Player sender, String[] args)
	{
	}

	private void commandMainWEImport(Player sender, String[] args) throws CrazyException
	{
		if (webridge == null)
			throw new CrazyCommandCircumstanceException("when WorldEdit is enabled!");
		RealRoom<FuncRoom> region;
		region = webridge.getSemiSavePlayerSelection(sender);
		geos.put(sender, region);
	}
}
