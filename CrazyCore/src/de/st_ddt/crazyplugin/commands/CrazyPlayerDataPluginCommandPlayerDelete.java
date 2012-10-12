package de.st_ddt.crazyplugin.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPlayerDataPluginInterface;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyPlayerDataPluginCommandPlayerDelete<T extends PlayerDataInterface> extends CrazyPlayerDataCommandExecutor<T, CrazyPlayerDataPluginInterface<T, ? extends T>>
{

	public CrazyPlayerDataPluginCommandPlayerDelete(final CrazyPlayerDataPluginInterface<T, ? extends T> plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYPLUGIN.COMMAND.PLAYER.DELETE.SUCCESS")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!sender.hasPermission(plugin.getName().toLowerCase() + ".player.delete"))
			throw new CrazyCommandPermissionException();
		if (args.length != 1)
			throw new CrazyCommandUsageException("<Player>");
		final String name = ChatHelper.listingString(" ", args);
		if (!plugin.deletePlayerData(name))
			throw new CrazyCommandNoSuchException("PlayerData", name);
		plugin.sendLocaleMessage("COMMAND.PLAYER.DELETE.SUCCESS", sender, name);
	}
}
