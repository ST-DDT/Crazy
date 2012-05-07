package de.st_ddt.crazyarena;

import java.util.Date;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;

public class CrazyArenaPlayerListener implements Listener
{

	private final HashMap<String, Rejoin> rejoins = new HashMap<String, Rejoin>();

	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		Rejoin rejoin = rejoins.get(player.getName());
		if (rejoin != null)
		{
			rejoins.remove(player.getName());
			// Arena aktiv?
			if (!rejoin.getArena().isEnabled())
				return;
			Date now = new Date();
			Date last = new Date(player.getLastPlayed() + 10 * 60 * 1000);
			// noch gleicher Durchlauf?
			if (rejoin.getRun() != rejoin.getArena().getRunNumber())
				return;
			// 10 Minuten Zeit für rejoin
			if (last.before(now))
				return;
			try
			{
				rejoin.getArena().join(player, true);
			}
			catch (CrazyCommandException e)
			{}
		}
	}

	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		Arena arena = CrazyArena.getPlugin().getArenas().getArena(player);
		if (arena != null)
		{
			rejoins.put(player.getName(), new Rejoin(arena));
			arena.quitgame(player);
		}
	}

	private class Rejoin
	{

		private final Arena arena;
		private final int run;

		public Rejoin(Arena arena)
		{
			super();
			this.arena = arena;
			run = arena.getRunNumber();
		}

		public Arena getArena()
		{
			return arena;
		}

		public int getRun()
		{
			return run;
		}
	}
}
