package de.st_ddt.crazyarena.commands.race;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.race.RaceArena;
import de.st_ddt.crazyarena.command.ArenaPlayerCommandExecutor;

public abstract class RacePlayerCommandExecutor extends ArenaPlayerCommandExecutor<RaceArena>
{

	public RacePlayerCommandExecutor(final CrazyArena plugin, final RaceArena arena)
	{
		super(plugin, arena);
	}
}
