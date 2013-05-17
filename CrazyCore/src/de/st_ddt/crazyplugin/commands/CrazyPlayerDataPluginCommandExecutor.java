package de.st_ddt.crazyplugin.commands;

import de.st_ddt.crazyplugin.CrazyPlayerDataPluginInterface;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;

public abstract class CrazyPlayerDataPluginCommandExecutor<T extends PlayerDataInterface, S extends CrazyPlayerDataPluginInterface<T, ? extends T>> extends CrazyPluginCommandExecutor<S> implements CrazyPlayerDataPluginCommandExecutorInterface<T, S>
{

	public CrazyPlayerDataPluginCommandExecutor(final S plugin)
	{
		super(plugin);
	}
}
