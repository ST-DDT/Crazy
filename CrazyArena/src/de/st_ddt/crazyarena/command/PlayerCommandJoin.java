package de.st_ddt.crazyarena.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.PlayerParamitrisable;

public class PlayerCommandJoin extends PlayerCommandExecutor
{

	public PlayerCommandJoin(final CrazyArena plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final Player player, final String[] args) throws CrazyException
	{
		final Arena<?> oldArena = plugin.getArena(player);
		Arena<?> arena = null;
		switch (args.length)
		{
			case 0:
				arena = oldArena;
				if (arena == null)
					arena = plugin.getInvitations().get(player.getName().toLowerCase());
				if (arena == null)
					throw new CrazyCommandUsageException("<Arena/Player>");
				break;
			case 1:
				final String name = args[0];
				arena = plugin.getArenaByName(name);
				if (arena == null)
				{
					Player to = Bukkit.getPlayerExact(name);
					if (to == null)
						to = Bukkit.getPlayer(name);
					arena = plugin.getArena(to);
				}
				if (arena == null)
					throw new CrazyCommandNoSuchException("Arena/Player", name, plugin.searchArenaNames(name));
				break;
			default:
				throw new CrazyCommandUsageException("<Arena/Player>");
		}
		if (oldArena != null)
			if (!oldArena.isParticipant(player, ParticipantType.SPECTATOR))
				throw new CrazyCommandCircumstanceException("when not in arena.", "(Currently in " + oldArena.getName() + ")");
			else
				oldArena.leave(player, false);
		if (!arena.getStatus().allowJoins())
			if (arena.getStatus().isActive())
			{
				if (!arena.allowJoin(player))
					throw new CrazyCommandCircumstanceException("when arena is ready for joins", arena.getStatus().toString());
			}
			else
				throw new CrazyCommandCircumstanceException("when arena is ready for joins", arena.getStatus().toString());
		arena.join(player, false);
	}

	@Override
	public List<String> tab(final Player player, final String[] args)
	{
		if (args.length != 1)
			return null;
		final List<String> res = new ArrayList<String>(plugin.searchArenaNames(args[0]));
		res.addAll(PlayerParamitrisable.tabHelp(args[0]));
		return res;
	}

	@Override
	public boolean hasAccessPermission(final Player player)
	{
		return PermissionModule.hasPermission(player, "crazyarena.join");
	}
}
