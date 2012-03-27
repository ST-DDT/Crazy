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

	public CrazyOnlinePlayerListener(CrazyOnline plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		OnlinePlayerData data = plugin.getPlayerData(player);
		if (data == null)
		{
			data = new OnlinePlayerData(player);
			plugin.getDatas().add(player.getName().toLowerCase(), data);
		}
		data.login();
		if (player.hasPermission("crazyonline.since.auto"))
			if (CrazyOnline.getPlugin() != null)
				try
				{
					CrazyOnline.getPlugin().CommandSince(player);
				}
				catch (CrazyCommandException e)
				{
					e.printStackTrace();
				}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		OnlinePlayerData data = plugin.getPlayerData(player);
		if (data == null)
			return;
		data.logout();
	}
}
