package de.st_ddt.crazyarena.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.MapParamitrisable;
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
		final Arena<?> oldArena = plugin.getArenaByPlayer(player);
		Arena<?> arena = null;
		switch (args.length)
		{
			case 0:
				arena = oldArena;
				if (arena == null)
					arena = plugin.getInvitations().get(player);
				if (arena == null)
					throw new CrazyCommandUsageException("<Arena/Player>");
				break;
			case 1:
				final String name = args[0];
				arena = plugin.getArenaAdvanced(name);
				if (arena == null)
					throw new CrazyCommandNoSuchException("Arena/Player", name, plugin.searchArenaNames(name));
				break;
			default:
				throw new CrazyCommandUsageException("<Arena/Player>");
		}
		if (oldArena != null)
			if (!oldArena.isParticipant(player, ParticipantType.SPECTATOR))
				throw new CrazyCommandCircumstanceException("while not in arena.", "(Currently in " + oldArena.getName() + ")");
			else if (oldArena.leave(player, false))
				plugin.getArenaByPlayer().remove(player);
		if (!arena.getStatus().allowJoins())
			if (arena.getStatus().isActive())
			{
				if (!arena.allowJoin(player))
					throw new CrazyCommandCircumstanceException("while arena is ready for joins", arena.getStatus().toString());
			}
			else
				throw new CrazyCommandCircumstanceException("while arena is ready for joins", arena.getStatus().toString());
		if (arena.join(player, false))
			plugin.getArenaByPlayer().put(player, arena);
	}

	@Override
	public List<String> tab(final Player player, final String[] args)
	{
		if (args.length != 1)
			return null;
		final List<String> res = new ArrayList<String>(plugin.searchArenaNames(args[0]));
		if (args[0].startsWith("?_") || "?_".startsWith(args[0]))
			for (final String type : MapParamitrisable.tabHelp(plugin.getArenasByType(), args[0].substring(2)))
				res.add("?_" + type);
		res.addAll(PlayerParamitrisable.tabHelp(args[0]));
		return res;
	}

	@Override
	public boolean hasAccessPermission(final Player player)
	{
		return PermissionModule.hasPermission(player, "crazyarena.join");
	}
}
