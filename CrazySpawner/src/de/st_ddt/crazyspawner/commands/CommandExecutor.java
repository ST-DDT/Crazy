package de.st_ddt.crazyspawner.commands;

import de.st_ddt.crazyplugin.commands.CrazyCommandExecutor;
import de.st_ddt.crazyspawner.CrazySpawner;

public abstract class CommandExecutor extends CrazyCommandExecutor<CrazySpawner>
{

	public CommandExecutor(final CrazySpawner plugin)
	{
		super(plugin);
	}
}
