package de.st_ddt.crazyplugin.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyPluginCommandMainMode extends CrazyCommandModeEditor<CrazyPluginInterface>
{

	public CrazyPluginCommandMainMode(final CrazyPluginInterface plugin)
	{
		super(plugin);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".mode");
	}
}
