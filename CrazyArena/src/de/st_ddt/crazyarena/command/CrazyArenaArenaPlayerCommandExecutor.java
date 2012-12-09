package de.st_ddt.crazyarena.command;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;

public abstract class CrazyArenaArenaPlayerCommandExecutor<S extends Arena<?>> extends CrazyArenaPlayerCommandExecutor
{

	protected final S arena;

	public CrazyArenaArenaPlayerCommandExecutor(final CrazyArena plugin, final S arena)
	{
		super(plugin);
		this.arena = arena;
	}

	public S getArena()
	{
		return arena;
	}
}
