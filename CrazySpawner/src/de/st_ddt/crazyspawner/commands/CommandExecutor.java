package de.st_ddt.crazyspawner.commands;

import de.st_ddt.crazyplugin.commands.CrazyPluginCommandExecutor;
import de.st_ddt.crazyspawner.CrazySpawner;

public abstract class CommandExecutor extends CrazyPluginCommandExecutor<CrazySpawner>
{

	public CommandExecutor(final CrazySpawner plugin)
	{
		super(plugin);
	}
}
