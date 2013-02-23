package de.st_ddt.crazyarena.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.PlayerParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class CommandMainKick extends CommandExecutor
{

	public CommandMainKick(final CrazyArena plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYARENA.COMMAND.ARENA.KICK $Kicker$ $Arena$ $Kicked$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length == 0)
			throw new CrazyCommandUsageException("<Player> [Player...]");
		for (final String name : args)
		{
			Player player = Bukkit.getPlayerExact(name);
			if (player == null)
			{
				player = Bukkit.getPlayer(name);
				if (player == null)
					throw new CrazyCommandNoSuchException("Player", name);
			}
			final Arena<?> arena = plugin.getArenaByPlayer(player);
			if (arena == null)
				throw new CrazyCommandCircumstanceException("when target is in an arena!");
			if (arena.leave(player, true))
				plugin.getArenaByPlayer().remove(player);
			plugin.sendLocaleMessage("COMMAND.ARENA.KICK", Bukkit.getOnlinePlayers(), sender.getName(), arena.getName(), player.getName());
			plugin.sendLocaleMessage("COMMAND.ARENA.KICK", Bukkit.getConsoleSender(), sender.getName(), arena.getName(), player.getName());
		}
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final String last = args[args.length - 1];
		return PlayerParamitrisable.tabHelp(last);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyarena.kick");
	}
}
