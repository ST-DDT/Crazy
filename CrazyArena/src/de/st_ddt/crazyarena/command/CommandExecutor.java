package de.st_ddt.crazyarena.command;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyplugin.commands.CrazyCommandExecutor;

public abstract class CommandExecutor extends CrazyCommandExecutor<CrazyArena>
{

	public CommandExecutor(final CrazyArena plugin)
	{
		super(plugin);
	}
}
