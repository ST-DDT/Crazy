package de.st_ddt.crazyarena;

import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyutil.PairList;

public class CrazyArenaPlayerListener implements Listener
{

	private PairList<Player, Arena> rejoins = null;

	public CrazyArenaPlayerListener()
	{
		rejoins = new PairList<Player, Arena>();
	}

	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if (rejoins.findDataVia1(player) != null)
		{
			Date now = new Date();
			Date last = new Date();
			last.setTime(player.getLastPlayed() + 10 * 60 * 1000);
			// 10 Minuten Zeit für rejoin
			if (last.after(now))
				try
				{
					rejoins.findDataVia1(player).join(player, true);
				}
				catch (CrazyCommandException e)
				{
					// e.printStackTrace();
				}
			rejoins.removeDataVia1(player);
		}
	}

	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		Arena arena = CrazyArena.getPlugin().getArenas().getArena(player);
		if (arena != null)
		{
			rejoins.setDataVia1(player, arena);
			arena.quitgame(player);
		}
	}
}
