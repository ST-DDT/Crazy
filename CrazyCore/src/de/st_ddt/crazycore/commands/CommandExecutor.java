package de.st_ddt.crazycore.commands;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.commands.CrazyCommandExecutor;

public abstract class CommandExecutor extends CrazyCommandExecutor<CrazyCore>
{

	public CommandExecutor(final CrazyCore plugin)
	{
		super(plugin);
	}
}
