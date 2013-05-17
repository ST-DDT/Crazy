package de.st_ddt.crazyplugin.commands;

import de.st_ddt.crazyplugin.CrazyPluginInterface;

public interface CrazyPluginCommandExecutorInterface<S extends CrazyPluginInterface> extends CrazyCommandExecutorInterface
{

	public S getPlugin();
}
