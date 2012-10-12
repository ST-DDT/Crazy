package de.st_ddt.crazyplugin.commands;

import de.st_ddt.crazyplugin.CrazyPlayerDataPluginInterface;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;

public class CrazyPlayerDataPluginCommandPlayerTree<T extends PlayerDataInterface> extends CrazyCommandTreeExecutor<CrazyPlayerDataPluginInterface<T, ? extends T>>
{

	public CrazyPlayerDataPluginCommandPlayerTree(final CrazyPlayerDataPluginInterface<T, ? extends T> plugin)
	{
		super(plugin);
		addSubCommand(new CrazyPlayerDataPluginCommandPlayerInfo<T>(plugin), "info");
		addSubCommand(new CrazyPlayerDataPluginCommandPlayerList<T>(plugin), "list");
		addSubCommand(new CrazyPlayerDataPluginCommandPlayerDelete<T>(plugin), "delete", "remove");
	}
}
