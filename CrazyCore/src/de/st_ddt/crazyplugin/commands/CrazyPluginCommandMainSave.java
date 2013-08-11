package de.st_ddt.crazyplugin.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CrazyPluginCommandMainSave extends CrazyPluginCommandExecutor<CrazyPluginInterface>
{

	public CrazyPluginCommandMainSave(final CrazyPluginInterface plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("$CRAZYPLUGIN$.COMMAND.SAVED")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 0)
			throw new CrazyCommandUsageException("");
		plugin.save();
		plugin.sendLocaleMessage("COMMAND.SAVED", sender);
	}

	@Override
	@Permission("$CRAZYPLUGIN$.save")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".save");
	}
}
