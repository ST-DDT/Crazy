package de.st_ddt.crazyplugin.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyPluginCommandMainSave extends CrazyCommandExecutor<CrazyPluginInterface>
{

	public CrazyPluginCommandMainSave(final CrazyPluginInterface plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYPLUGIN.COMMAND.CONFIG.SAVED")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".save"))
			throw new CrazyCommandPermissionException();
		if (args.length != 0)
			throw new CrazyCommandUsageException("");
		plugin.save();
		plugin.sendLocaleMessage("COMMAND.CONFIG.SAVED", sender);
	}
}
