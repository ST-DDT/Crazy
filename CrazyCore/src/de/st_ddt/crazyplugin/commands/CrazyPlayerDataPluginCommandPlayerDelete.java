package de.st_ddt.crazyplugin.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPlayerDataPluginInterface;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.OfflinePlayerParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyPlayerDataPluginCommandPlayerDelete<T extends PlayerDataInterface> extends CrazyPlayerDataCommandExecutor<T, CrazyPlayerDataPluginInterface<T, ? extends T>>
{

	public CrazyPlayerDataPluginCommandPlayerDelete(final CrazyPlayerDataPluginInterface<T, ? extends T> plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("$CRAZYPLAYERDATAPLUGIN$.COMMAND.PLAYER.DELETE.SUCCESS $Name$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final String name = ChatHelper.listingString(" ", args);
		if (!plugin.deletePlayerData(name))
			throw new CrazyCommandNoSuchException("PlayerData", name);
		plugin.sendLocaleMessage("COMMAND.PLAYER.DELETE.SUCCESS", sender, name);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length != 1)
			return null;
		return OfflinePlayerParamitrisable.tabHelp(args[0]);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".player.delete");
	}
}
