package de.st_ddt.crazyplugin.commands;

import de.st_ddt.crazyplugin.CrazyPlayerDataPluginInterface;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;

public interface CrazyPlayerDataPluginCommandExecutorInterface<T extends PlayerDataInterface, S extends CrazyPlayerDataPluginInterface<T, ? extends T>> extends CrazyPluginCommandExecutorInterface<S>
{

	@Override
	public S getPlugin();
}
