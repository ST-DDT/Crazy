package de.st_ddt.crazychats.commands;

import java.util.List;

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

	@Override
	public final List<String> tab(final CommandSender sender, final String[] args)
	{
		if (sender instanceof Player)
			return tab((Player) sender, args);
		else
			return null;
	}

	public List<String> tab(final Player player, final String[] args)
	{
		return null;
	}

	@Override
	public final boolean hasAccessPermission(final CommandSender sender)
	{
		if (sender instanceof Player)
			return hasAccessPermission((Player) sender);
		else
			return true;
	}

	public boolean hasAccessPermission(final Player player)
	{
		return true;
	}
}
