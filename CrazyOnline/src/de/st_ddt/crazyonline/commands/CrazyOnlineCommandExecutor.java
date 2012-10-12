package de.st_ddt.crazyonline.commands;

import de.st_ddt.crazyonline.CrazyOnline;
import de.st_ddt.crazyonline.data.OnlineData;
import de.st_ddt.crazyplugin.commands.CrazyPlayerDataCommandExecutor;

public abstract class CrazyOnlineCommandExecutor extends CrazyPlayerDataCommandExecutor<OnlineData, CrazyOnline>
{

	public CrazyOnlineCommandExecutor(final CrazyOnline plugin)
	{
		super(plugin);
	}
}
