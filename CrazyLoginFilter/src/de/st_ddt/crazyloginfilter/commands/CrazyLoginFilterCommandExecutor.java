package de.st_ddt.crazyloginfilter.commands;

import de.st_ddt.crazyloginfilter.CrazyLoginFilter;
import de.st_ddt.crazyloginfilter.data.PlayerAccessFilter;
import de.st_ddt.crazyplugin.commands.CrazyPlayerDataCommandExecutor;

public abstract class CrazyLoginFilterCommandExecutor extends CrazyPlayerDataCommandExecutor<PlayerAccessFilter, CrazyLoginFilter>
{

	public CrazyLoginFilterCommandExecutor(final CrazyLoginFilter plugin)
	{
		super(plugin);
	}
}
