package de.st_ddt.crazyplugin.commands;

import de.st_ddt.crazyplugin.CrazyPluginInterface;

public class CrazyPluginCommandMainTree extends CrazyCommandTreeExecutor<CrazyPluginInterface> implements CrazyPluginCommandExecutorInterface<CrazyPluginInterface>
{

	protected final CrazyPluginCommandMainReload reloadCommand;

	public CrazyPluginCommandMainTree(final CrazyPluginInterface plugin)
	{
		super(plugin);
		addSubCommand(new CrazyPluginCommandMainInfo(plugin), "info");
		addSubCommand(new CrazyPluginCommandMainLogger(plugin), "logger", "log");
		addSubCommand(new CrazyPluginCommandMainHelp(plugin), "help");
		reloadCommand = new CrazyPluginCommandMainReload(plugin);
		addSubCommand(reloadCommand, "reload");
		addSubCommand(new CrazyPluginCommandMainSave(plugin), "save");
	}

	@Override
	public final CrazyPluginInterface getPlugin()
	{
		return plugin;
	}

	public CrazyPluginCommandMainReload getReloadCommand()
	{
		return reloadCommand;
	}
}
