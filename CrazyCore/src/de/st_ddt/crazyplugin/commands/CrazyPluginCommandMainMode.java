package de.st_ddt.crazyplugin.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyutil.modes.Mode;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Permission;

public class CrazyPluginCommandMainMode extends CrazyCommandModeEditor<CrazyPluginInterface> implements CrazyPluginCommandExecutorInterface<CrazyPluginInterface>
{

	public CrazyPluginCommandMainMode(final CrazyPluginInterface plugin)
	{
		super(plugin);
	}

	@Override
	@Permission("$CRAZYPLUGIN$.mode")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".mode");
	}

	@Override
	public final CrazyPluginInterface getPlugin()
	{
		return plugin;
	}

	@Override
	@Permission({ "$CRAZYPLUGIN$.mode.*", "$CRAZYPLUGIN$.mode.<MODENAME>" })
	public boolean hasAccessPermission(final CommandSender sender, final Mode<?> mode)
	{
		return PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".mode.*") || PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".mode." + mode.getName().toLowerCase());
	}
}
