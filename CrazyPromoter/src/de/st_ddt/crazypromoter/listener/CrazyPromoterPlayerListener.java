package de.st_ddt.crazypromoter.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.st_ddt.crazypromoter.CrazyPromoter;

public class CrazyPromoterPlayerListener implements Listener
{

	private final CrazyPromoter plugin;

	public CrazyPromoterPlayerListener(final CrazyPromoter plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		plugin.checkStatus(player);
	}
}
