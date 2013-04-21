package de.st_ddt.crazyenchanter.commands;

import de.st_ddt.crazyenchanter.CrazyEnchanter;
import de.st_ddt.crazyplugin.commands.CrazyCommandExecutor;

public abstract class CommandExecutor extends CrazyCommandExecutor<CrazyEnchanter>
{

	public CommandExecutor(final CrazyEnchanter plugin)
	{
		super(plugin);
	}
}
