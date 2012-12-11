package de.st_ddt.crazygeo.commands;

import org.bukkit.entity.Player;

import de.st_ddt.crazygeo.CrazyGeo;
import de.st_ddt.crazygeo.region.RealRoom;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.poly.room.FuncRoom;

public class CrazyGeoCommandWorldEditImport extends CrazyGeoPlayerCommandExecutor
{

	public CrazyGeoCommandWorldEditImport(final CrazyGeo plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final Player sender, final String[] args) throws CrazyException
	{
		if (plugin.getWorldEditBridge() == null)
			throw new CrazyCommandCircumstanceException("when WorldEdit is enabled!");
		final RealRoom<FuncRoom> region = plugin.getWorldEditBridge().getSemiSavePlayerSelection(sender);
		plugin.setPlayerSelection(sender, region);
		sender.sendMessage("Imported worldedit region");
	}
}
