package de.st_ddt.crazyarena.tasks.pve;

import de.st_ddt.crazyarena.arenas.pve.PvEArena;

public abstract class ArenaPvEDelayed implements Runnable
{

	protected final PvEArena arena;

	public ArenaPvEDelayed(final PvEArena arena)
	{
		super();
		this.arena = arena;
	}
}
