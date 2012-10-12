package de.st_ddt.crazycore.commands;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.commands.CrazyCommandExecutor;

public abstract class CrazyCoreCommandExecutor extends CrazyCommandExecutor<CrazyCore>
{

	public CrazyCoreCommandExecutor(final CrazyCore plugin)
	{
		super(plugin);
	}
}
