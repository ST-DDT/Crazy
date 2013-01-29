package de.st_ddt.crazyarena.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class CrazyArenaPlayerListener implements Listener
{

	private final Map<String, Rejoin> rejoins = new HashMap<String, Rejoin>();
	private final CrazyArena plugin;

	public CrazyArenaPlayerListener(final CrazyArena plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void PlayerJoin(final PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		final Rejoin rejoin = rejoins.get(player.getName());
		if (rejoin != null)
		{
			final Arena<?> arena = rejoin.getArena();
			rejoins.remove(player.getName());
			// Arena aktiv?
			if (!arena.getStatus().isActive())
				return;
			// noch gleicher Durchlauf?
			if (rejoin.getRun() != arena.getRunNumber())
				return;
			final Date now = new Date();
			final Date last = new Date(player.getLastPlayed() + arena.getRejoinTime());
			// Been away to long?
			if (last.before(now))
				return;
			try
			{
				if (arena.join(player, true))
					plugin.getArenaByPlayer().put(player, arena);
			}
			catch (final CrazyException e)
			{}
		}
	}

	@EventHandler
	public void PlayerQuit(final PlayerQuitEvent event)
	{
		final Player player = event.getPlayer();
		final Arena<?> arena = plugin.getArenaByPlayer().remove(player);
		if (arena != null)
		{
			rejoins.put(player.getName(), new Rejoin(arena));
			final ParticipantType type = arena.getParticipant(player).getParticipantType();
			if (type.isPlaying() || type.isJudge())
				arena.quitgame(player);
			else
				try
				{
					arena.leave(player, true);
				}
				catch (final CrazyException e)
				{}
		}
		plugin.getSelections().remove(player);
		plugin.getInvitations().remove(player);
	}

	private final class Rejoin
	{

		private final Arena<?> arena;
		private final int run;

		public Rejoin(final Arena<?> arena)
		{
			super();
			this.arena = arena;
			run = arena.getRunNumber();
		}

		public Arena<?> getArena()
		{
			return arena;
		}

		public int getRun()
		{
			return run;
		}
	}
}
