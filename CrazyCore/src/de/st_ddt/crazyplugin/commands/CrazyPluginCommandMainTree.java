package de.st_ddt.crazyplugin.commands;

import de.st_ddt.crazyplugin.CrazyPluginInterface;

public class CrazyPluginCommandMainTree extends CrazyCommandTreeExecutor<CrazyPluginInterface>
{

	@SuppressWarnings("deprecation")
	public CrazyPluginCommandMainTree(final CrazyPluginInterface plugin)
	{
		// temp method to avoid version dismatching
		super(plugin, true);
		addSubCommand(new CrazyPluginCommandMainInfo(plugin), "info");
		addSubCommand(new CrazyPluginCommandMainLogger(plugin), "logger", "log");
		addSubCommand(new CrazyPluginCommandMainHelp(plugin), "help");
		addSubCommand(new CrazyPluginCommandMainReload(plugin), "reload");
		addSubCommand(new CrazyPluginCommandMainSave(plugin), "save");
	}
}
