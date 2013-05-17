package de.st_ddt.crazyloginfilter.commands;

import de.st_ddt.crazyloginfilter.CrazyLoginFilter;
import de.st_ddt.crazyloginfilter.data.PlayerAccessFilter;
import de.st_ddt.crazyplugin.commands.CrazyPlayerDataPluginCommandExecutor;

public abstract class CrazyLoginFilterCommandExecutor extends CrazyPlayerDataPluginCommandExecutor<PlayerAccessFilter, CrazyLoginFilter>
{

	public CrazyLoginFilterCommandExecutor(final CrazyLoginFilter plugin)
	{
		super(plugin);
	}
}
