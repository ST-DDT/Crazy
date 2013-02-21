package de.st_ddt.crazyarena.tasks.pve;

import de.st_ddt.crazyarena.arenas.pve.PvEArena;

public class ArenaPvEDelayedNext extends ArenaPvEDelayed
{

	private boolean canceled;

	public ArenaPvEDelayedNext(final PvEArena arena)
	{
		super(arena);
	}

	@Override
	public void run()
	{
		if (arena != null && !canceled)
			arena.startRound();
	}

	public void cancel()
	{
		this.canceled = true;
	}
}
