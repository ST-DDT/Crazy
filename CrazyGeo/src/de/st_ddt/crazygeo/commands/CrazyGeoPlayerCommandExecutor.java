package de.st_ddt.crazygeo.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazygeo.CrazyGeo;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public abstract class CrazyGeoPlayerCommandExecutor extends CrazyGeoCommandExecutor
{

	public CrazyGeoPlayerCommandExecutor(final CrazyGeo plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!(sender instanceof Player))
			throw new CrazyCommandExecutorException(false);
		command((Player) sender, args);
	}

	public abstract void command(Player sender, String[] args) throws CrazyException;
}
