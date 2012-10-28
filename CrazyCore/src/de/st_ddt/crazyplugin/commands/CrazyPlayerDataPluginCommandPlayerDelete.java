package de.st_ddt.crazyplugin.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPlayerDataPluginInterface;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyPlayerDataPluginCommandPlayerDelete<T extends PlayerDataInterface> extends CrazyPlayerDataCommandExecutor<T, CrazyPlayerDataPluginInterface<T, ? extends T>>
{

	public CrazyPlayerDataPluginCommandPlayerDelete(final CrazyPlayerDataPluginInterface<T, ? extends T> plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYPLUGIN.COMMAND.PLAYER.DELETE.SUCCESS $Name$")
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
		final List<String> res = new ArrayList<String>();
		final Pattern pattern = Pattern.compile(args[0], Pattern.CASE_INSENSITIVE);
		for (final OfflinePlayer player : Bukkit.getOfflinePlayers())
			if (pattern.matcher(player.getName()).find())
				if (plugin.hasPlayerData(player))
					res.add(player.getName());
		return res;
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".player.delete");
	}
}
