package de.st_ddt.crazyplugin.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyPluginCommandMainReload extends CrazyCommandExecutor<CrazyPluginInterface>
{

	@SuppressWarnings("deprecation")
	public CrazyPluginCommandMainReload(final CrazyPluginInterface plugin)
	{
		// temp method to avoid version dismatching
		super(plugin, true);
	}

	@Override
	@Localized("CRAZYPLUGIN.COMMAND.CONFIG.RELOADED")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 0)
			throw new CrazyCommandUsageException("");
		plugin.reloadConfig();
		plugin.loadConfiguration();
		plugin.sendLocaleMessage("COMMAND.CONFIG.RELOADED", sender);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".reload");
	}
}
