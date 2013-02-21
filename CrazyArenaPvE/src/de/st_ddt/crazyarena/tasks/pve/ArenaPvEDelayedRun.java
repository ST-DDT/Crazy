package de.st_ddt.crazyarena.tasks.pve;

import de.st_ddt.crazyarena.arenas.pve.PvEArena;

public class ArenaPvEDelayedRun extends ArenaPvEDelayed
{

	public ArenaPvEDelayedRun(final PvEArena arena)
	{
		super(arena);
	}

	@Override
	public void run()
	{
		if (arena != null)
			arena.delayedStartRound();
	}
}
