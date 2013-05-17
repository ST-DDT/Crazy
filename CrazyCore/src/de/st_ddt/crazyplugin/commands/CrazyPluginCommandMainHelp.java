package de.st_ddt.crazyplugin.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class CrazyPluginCommandMainHelp extends CrazyPluginCommandExecutor<CrazyPluginInterface>
{

	public CrazyPluginCommandMainHelp(final CrazyPluginInterface plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		plugin.show(sender, plugin.getChatHeader(), true);
	}
}
