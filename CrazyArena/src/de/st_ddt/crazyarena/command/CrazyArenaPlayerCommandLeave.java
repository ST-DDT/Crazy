package de.st_ddt.crazyarena.command;

import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class CrazyArenaPlayerCommandLeave extends CrazyArenaPlayerCommandExecutor
{

	public CrazyArenaPlayerCommandLeave(final CrazyArena plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final Player player, final String[] args) throws CrazyException
	{
		final Arena<?> arena = plugin.getArena(player);
		if (arena == null || !arena.isParticipant(player))
			throw new CrazyCommandCircumstanceException("when participating in an arena!");
		arena.leave(player, false);
	}
}
