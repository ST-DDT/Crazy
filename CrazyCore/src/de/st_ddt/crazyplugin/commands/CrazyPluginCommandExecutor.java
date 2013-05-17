package de.st_ddt.crazyplugin.commands;

import de.st_ddt.crazyplugin.CrazyPluginInterface;

public abstract class CrazyPluginCommandExecutor<S extends CrazyPluginInterface> extends CrazyCommandExecutor<S> implements CrazyPluginCommandExecutorInterface<S>
{

	public CrazyPluginCommandExecutor(final S plugin)
	{
		super(plugin);
	}

	@Override
	public final S getPlugin()
	{
		return plugin;
	}
}
