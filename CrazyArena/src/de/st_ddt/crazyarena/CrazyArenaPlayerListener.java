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
	public void PlayerJoin(final PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		final Rejoin rejoin = rejoins.get(player.getName());
		if (rejoin != null)
		{
			final Arena arena = rejoin.getArena();
			rejoins.remove(player.getName());
			// Arena aktiv?
			if (!arena.isEnabled())
				return;
			// Arena läuft?
			if (!arena.isRunning())
				return;
			// noch gleicher Durchlauf?
			if (rejoin.getRun() != arena.getRunNumber())
				return;
			final Date now = new Date();
			final Date last = new Date(player.getLastPlayed() + 10 * 60 * 1000);
			// 10 Minuten Zeit für rejoin
			if (last.before(now))
				return;
			try
			{
				arena.join(player, true);
			}
			catch (CrazyCommandException e)
			{}
		}
	}

	@EventHandler
	public void PlayerQuit(final PlayerQuitEvent event)
	{
		final Player player = event.getPlayer();
		final Arena arena = CrazyArena.getPlugin().getArenas().getArena(player);
		if (arena != null)
		{
			rejoins.put(player.getName(), new Rejoin(arena));
			arena.quitgame(player);
		}
	}

	private final class Rejoin
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
