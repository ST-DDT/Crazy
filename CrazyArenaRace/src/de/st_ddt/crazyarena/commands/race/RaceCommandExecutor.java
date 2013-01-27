package de.st_ddt.crazyarena.commands.race;

import de.st_ddt.crazyarena.CrazyArenaRace;
import de.st_ddt.crazyarena.arenas.race.RaceArena;
import de.st_ddt.crazyarena.command.ArenaCommandExecutor;

public abstract class RaceCommandExecutor extends ArenaCommandExecutor<RaceArena>
{

	public RaceCommandExecutor(final RaceArena arena)
	{
		super(arena);
	}

	@Override
	public CrazyArenaRace getArenaPlugin()
	{
		return CrazyArenaRace.getPlugin();
	}
}
