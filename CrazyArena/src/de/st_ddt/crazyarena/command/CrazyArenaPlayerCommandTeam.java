package de.st_ddt.crazyarena.command;

import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class CrazyArenaPlayerCommandTeam extends CrazyArenaPlayerCommandExecutor
{

	public CrazyArenaPlayerCommandTeam(final CrazyArena plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final Player player, final String[] args) throws CrazyException
	{
		final Arena<?> arena = plugin.getArena(player);
		if (arena == null || !arena.getParticipant(player).isPlayer())
			throw new CrazyCommandCircumstanceException("when playing in an arena!");
		arena.team(player);
	}
}
