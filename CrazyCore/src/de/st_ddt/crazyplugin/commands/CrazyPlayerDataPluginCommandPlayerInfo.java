package de.st_ddt.crazyplugin.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.CrazyPlayerDataPluginInterface;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;

public class CrazyPlayerDataPluginCommandPlayerInfo<T extends PlayerDataInterface> extends CrazyPlayerDataCommandExecutor<T, CrazyPlayerDataPluginInterface<T, ? extends T>>
{

	public CrazyPlayerDataPluginCommandPlayerInfo(final CrazyPlayerDataPluginInterface<T, ? extends T> plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length == 0 && !(sender instanceof Player))
			throw new CrazyCommandUsageException("<Player>");
		String name = sender.getName();
		if (args.length > 0)
			name = ChatHelper.listingString(" ", args);
		commandPlayerInfo(sender, name, true);
	}

	protected void commandPlayerInfo(final CommandSender sender, final String name, final boolean detailed) throws CrazyCommandException
	{
		final T data = plugin.getPlayerData(name);
		if (data == null)
			throw new CrazyCommandNoSuchException("PlayerData", name);
		if (!sender.hasPermission(plugin.getName().toLowerCase() + ".player.info." + (sender.getName().equals(name) ? "self" : "other")))
			throw new CrazyCommandPermissionException();
		data.show(sender, plugin.getChatHeader(), detailed);
	}
}
