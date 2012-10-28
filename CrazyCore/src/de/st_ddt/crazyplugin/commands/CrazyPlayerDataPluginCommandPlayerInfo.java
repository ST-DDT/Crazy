package de.st_ddt.crazyplugin.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

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
		if (!PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".player.info." + (sender.getName().equals(name) ? "self" : "other")))
			throw new CrazyCommandPermissionException();
		final T data = plugin.getPlayerData(name);
		if (data == null)
			throw new CrazyCommandNoSuchException("PlayerData", name);
		data.show(sender, plugin.getChatHeader(), detailed);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length != 1)
			return null;
		final List<String> res = new ArrayList<String>();
		final Pattern pattern = Pattern.compile(args[0], Pattern.CASE_INSENSITIVE);
		for (final OfflinePlayer player : Bukkit.getOfflinePlayers())
			if (pattern.matcher(player.getName()).find())
				if (plugin.hasPlayerData(player))
					res.add(player.getName());
		return res;
	}
}
