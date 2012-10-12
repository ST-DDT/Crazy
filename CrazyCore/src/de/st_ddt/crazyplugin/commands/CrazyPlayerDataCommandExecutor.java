package de.st_ddt.crazyplugin.commands;

import de.st_ddt.crazyplugin.CrazyPlayerDataPluginInterface;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;

public abstract class CrazyPlayerDataCommandExecutor<T extends PlayerDataInterface, S extends CrazyPlayerDataPluginInterface<T, ? extends T>> extends CrazyCommandExecutor<S>
{

	public CrazyPlayerDataCommandExecutor(final S plugin)
	{
		super(plugin);
	}
}
