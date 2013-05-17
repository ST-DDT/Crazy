package de.st_ddt.crazyonline.commands;

import de.st_ddt.crazyonline.CrazyOnline;
import de.st_ddt.crazyonline.data.OnlineData;
import de.st_ddt.crazyplugin.commands.CrazyPlayerDataPluginCommandExecutor;

public abstract class CrazyOnlineCommandExecutor extends CrazyPlayerDataPluginCommandExecutor<OnlineData, CrazyOnline>
{

	public CrazyOnlineCommandExecutor(final CrazyOnline plugin)
	{
		super(plugin);
	}
}
