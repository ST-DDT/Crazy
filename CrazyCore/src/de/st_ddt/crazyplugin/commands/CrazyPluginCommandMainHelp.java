package de.st_ddt.crazyplugin.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class CrazyPluginCommandMainHelp extends CrazyCommandExecutor<CrazyPluginInterface>
{

	@SuppressWarnings("deprecation")
	public CrazyPluginCommandMainHelp(final CrazyPluginInterface plugin)
	{
		// temp method to avoid version dismatching
		super(plugin, true);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		plugin.show(sender, plugin.getChatHeader(), true);
	}
}
