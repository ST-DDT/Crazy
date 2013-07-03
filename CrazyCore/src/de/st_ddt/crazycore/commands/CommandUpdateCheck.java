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

	@SuppressWarnings("deprecation")
	@Override
	@Localized("CRAZYCORE.COMMAND.UPDATECHECK")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		plugin.sendLocaleMessage("COMMAND.UPDATECHECK", sender);
		Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new PluginUpdateCheckTask(true));
	}

	@Override
	@Permission("crazycore.updatecheck")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazycore.updatecheck");
	}
}
