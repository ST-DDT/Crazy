package de.st_ddt.crazyonline;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;

public class CrazyOnlinePlayerListener implements Listener
{

	protected final CrazyOnline plugin;

	public CrazyOnlinePlayerListener(final CrazyOnline plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void PlayerJoin(final PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		OnlinePlayerData data = plugin.getPlayerData(player);
		if (data == null)
		{
			data = new OnlinePlayerData(player);
			plugin.getDatas().put(player.getName().toLowerCase(), data);
		}
		data.login();
		if (plugin.isShowOnlineInfoEnabled())
			if (player.hasPermission("crazyonline.since.auto"))
				try
				{
					plugin.commandSince(player);
				}
				catch (final CrazyCommandException e)
				{
					e.printStackTrace();
				}
		plugin.getCrazyLogger().log("Join", player.getName() + " @ " + player.getAddress().getAddress().getHostAddress() + " joined the server");
	}

	@EventHandler
	public void PlayerQuit(final PlayerQuitEvent event)
	{
		final Player player = event.getPlayer();
		plugin.getCrazyLogger().log("Quit", player.getName() + " @ " + player.getAddress().getAddress().getHostAddress() + " left the server");
		final OnlinePlayerData data = plugin.getPlayerData(player);
		if (data == null)
			return;
		data.logout();
	}
}
