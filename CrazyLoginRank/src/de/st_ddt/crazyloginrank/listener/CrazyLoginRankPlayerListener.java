package de.st_ddt.crazyloginrank.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import de.st_ddt.crazyloginrank.CrazyLoginRank;

public class CrazyLoginRankPlayerListener implements Listener
{

	private final CrazyLoginRank plugin;

	public CrazyLoginRankPlayerListener(final CrazyLoginRank plugin)
	{
		super();
		this.plugin = plugin;
	}

	public CrazyLoginRank getPlugin()
	{
		return plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void PlayerLoginRankCheck(final PlayerLoginEvent event)
	{
		if (Bukkit.getOnlinePlayers().length < Bukkit.getMaxPlayers())
			return;
		final Player lowest = plugin.getLowestRank();
		if (lowest == null)
			return;
		final Player player = event.getPlayer();
		if (player.equals(lowest))
			return;
		if (plugin.getAvailablePlayerData(player).compareTo(plugin.getAvailablePlayerData(lowest)) == 1)
		{
			lowest.kickPlayer("The Server is full (Your Rank is too low)");
			event.setResult(Result.ALLOWED);
		}
		else
		{
			event.setKickMessage("The Server is full");
			event.setResult(Result.KICK_FULL);
		}
	}
}
