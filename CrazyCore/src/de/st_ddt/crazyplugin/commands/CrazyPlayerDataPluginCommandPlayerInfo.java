package de.st_ddt.crazyplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
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
		commandPlayerInfo(sender, ChatHelper.listingString(" ", args), true);
	}

	protected void commandPlayerInfo(final CommandSender sender, final String name, final boolean detailed) throws CrazyCommandException
	{
		OfflinePlayer target = null;
		if (name == null)
		{
			if (sender instanceof ConsoleCommandSender)
				throw new CrazyCommandUsageException("<Player>");
			target = (Player) sender;
		}
		else if (name.equals(""))
		{
			if (sender instanceof ConsoleCommandSender)
				throw new CrazyCommandUsageException("<Player>");
			target = (Player) sender;
		}
		else
		{
			target = Bukkit.getPlayer(name);
			if (target == null)
				target = Bukkit.getOfflinePlayer(name);
			if (target == null)
				throw new CrazyCommandNoSuchException("Player", name);
		}
		if (sender == target)
			if (!sender.hasPermission(plugin.getName().toLowerCase() + ".player.info.self"))
				throw new CrazyCommandPermissionException();
			else if (!sender.hasPermission(plugin.getName().toLowerCase() + ".player.info.other"))
				throw new CrazyCommandPermissionException();
		final T data = plugin.getPlayerData(target);
		if (data == null)
			throw new CrazyCommandNoSuchException("PlayerData", target.getName());
		data.show(sender, plugin.getChatHeader(), detailed);
	}
}
