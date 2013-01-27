package de.st_ddt.crazyarena.command;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.CrazyArenaPlugin;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.commands.CrazyCommandExecutor;

public abstract class ArenaCommandExecutor<S extends Arena<?>> extends CrazyCommandExecutor<S>
{

	public ArenaCommandExecutor(final S plugin)
	{
		super(plugin);
	}

	public final CrazyArena getArenaMainPlugin()
	{
		return CrazyArena.getPlugin();
	}
	
	public abstract CrazyArenaPlugin getArenaPlugin();
}
