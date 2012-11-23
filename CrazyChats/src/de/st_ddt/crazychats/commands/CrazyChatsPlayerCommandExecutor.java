package de.st_ddt.crazychats.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public abstract class CrazyChatsPlayerCommandExecutor extends CrazyChatsCommandExecutor
{

	public CrazyChatsPlayerCommandExecutor(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	public final void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (sender instanceof Player)
			command((Player) sender, args);
		else
			throw new CrazyCommandExecutorException(false);
	}

	public abstract void command(Player player, String[] args) throws CrazyException;
}