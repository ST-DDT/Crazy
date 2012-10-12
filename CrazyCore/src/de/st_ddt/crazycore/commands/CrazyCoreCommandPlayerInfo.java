package de.st_ddt.crazycore.commands;

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
		if (!sender.hasPermission("crazycore.player.info"))
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
}
