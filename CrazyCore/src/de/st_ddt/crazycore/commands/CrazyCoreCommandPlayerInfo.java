package de.st_ddt.crazycore.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazycore.data.PseudoPlayerData;
import de.st_ddt.crazyplugin.CrazyPlayerDataPlugin;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyCoreCommandPlayerInfo extends CrazyCoreCommandExecutor
{

	public CrazyCoreCommandPlayerInfo(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYCORE.PLAYERINFO.SEPARATOR")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!PermissionModule.hasPermission(sender, "crazycore.player.info"))
			throw new CrazyCommandPermissionException();
		final String name = ChatHelper.listingString(" ", args);
		OfflinePlayer player = Bukkit.getPlayer(name);
		if (player == null)
			player = Bukkit.getOfflinePlayer(name);
		if (player == null)
			throw new CrazyCommandNoSuchException("Player", name);
		new PseudoPlayerData(player.getName()).show(sender);
		for (final CrazyPlayerDataPlugin<? extends PlayerDataInterface, ? extends PlayerDataInterface> plugin : CrazyPlayerDataPlugin.getCrazyPlayerDataPlugins())
		{
			final PlayerDataInterface data = plugin.getAvailablePlayerData(name);
			if (data != null)
			{
				plugin.sendLocaleMessage("PLAYERINFO.SEPARATOR", sender);
				data.showDetailed(sender, plugin.getChatHeader());
			}
		}
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
				res.add(player.getName());
		return res;
	}
}
