package de.st_ddt.crazycore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazycore.tasks.PluginUpdateCheckTask;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CommandUpdateCheck extends CommandExecutor
{

	public CommandUpdateCheck(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCORE.COMMAND.UPDATECHECK", "CRAZYCORE.COMMAND.UPDATECHECK.DISABLED", })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (plugin.isCheckingForUpdatesEnabled())
		{
			plugin.sendLocaleMessage("COMMAND.UPDATECHECK", sender);
			Bukkit.getScheduler().runTaskAsynchronously(plugin, new PluginUpdateCheckTask(plugin, sender, true));
		}
		else
			plugin.sendLocaleMessage("COMMAND.UPDATECHECK.DISABLED", sender);
	}

	@Override
	@Permission("crazycore.updatecheck")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazycore.updatecheck");
	}
}
