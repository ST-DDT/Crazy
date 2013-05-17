package de.st_ddt.crazyplugin.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHeaderProvider;

public abstract class CrazyPlayerCommandExecutor<S extends ChatHeaderProvider> extends CrazyCommandExecutor<S> implements CrazyPlayerCommandExecutorInterface
{

	public CrazyPlayerCommandExecutor(final S chatHeaderProvider)
	{
		super(chatHeaderProvider);
	}

	@Override
	public final void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (sender instanceof Player)
			command((Player) sender, args);
		else
			throw new CrazyCommandExecutorException(false);
	}

	@Override
	public abstract void command(Player player, String[] args) throws CrazyException;

	@Override
	public final List<String> tab(final CommandSender sender, final String[] args)
	{
		if (sender instanceof Player)
			return tab((Player) sender, args);
		else
			return null;
	}

	@Override
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

	@Override
	public boolean hasAccessPermission(final Player player)
	{
		return true;
	}

	@Override
	public final boolean isAccessible(final CommandSender sender)
	{
		if (sender instanceof Player)
			return isAccessible((Player) sender);
		else
			return false;
	}

	@Override
	public boolean isAccessible(final Player player)
	{
		return hasAccessPermission(player);
	}
}
