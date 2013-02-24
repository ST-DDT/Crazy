package de.st_ddt.crazyplugin.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyPluginCommandMainSave extends CrazyCommandExecutor<CrazyPluginInterface>
{

	public CrazyPluginCommandMainSave(final CrazyPluginInterface plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("$CRAZYPLUGIN$.COMMAND.CONFIG.SAVED")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 0)
			throw new CrazyCommandUsageException("");
		plugin.save();
		plugin.sendLocaleMessage("COMMAND.CONFIG.SAVED", sender);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".save");
	}
}
