package de.st_ddt.crazyonline.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyonline.CrazyOnline;
import de.st_ddt.crazyonline.data.OnlinePlayerData;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyOnlineCommandPlayerReset extends CrazyOnlineCommandExecutor
{

	public CrazyOnlineCommandPlayerReset(final CrazyOnline plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYONLINE.COMMAND.RESET $Name$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("<Name>");
		final String name = args[0];
		final OnlinePlayerData data = plugin.getPlayerData(name);
		if (data == null)
			throw new CrazyCommandNoSuchException("Player", name);
		data.resetOnlineTime();
		plugin.sendLocaleMessage("COMMAND.RESET", sender, data.getName());
		plugin.getCrazyDatabase().save(data);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyonline.player.reset");
	}
}
