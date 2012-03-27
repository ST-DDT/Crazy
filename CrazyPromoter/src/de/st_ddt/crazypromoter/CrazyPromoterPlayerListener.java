package de.st_ddt.crazypromoter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CrazyPromoterPlayerListener implements Listener
{

	protected final CrazyPromoter plugin;

	public CrazyPromoterPlayerListener(CrazyPromoter plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		plugin.checkStatus(player);
	}
}
