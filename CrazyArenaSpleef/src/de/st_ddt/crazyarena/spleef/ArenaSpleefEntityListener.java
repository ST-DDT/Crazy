package de.st_ddt.crazyarena.spleef;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

import de.st_ddt.crazyarena.participants.Participant;
import de.st_ddt.crazyarena.participants.ParticipantType;

public class ArenaSpleefEntityListener implements Listener
{

	private final ArenaSpleef arena;
	private final HashMap<Player, Integer> escapes = new HashMap<Player, Integer>();

	public ArenaSpleefEntityListener(ArenaSpleef arena)
	{
		this.arena = arena;
	}

	public ArenaSpleef getArena()
	{
		return arena;
	}

	@EventHandler
	public void PlayerDeathEvent(PlayerDeathEvent event)
	{
		arena.PlayerDeath(event.getEntity());
	}

	@EventHandler
	public void PlayerMoveEvent(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		Participant participant = arena.getParticipant(player);
		if (participant == null)
			return;
		if (participant.getParticipantType() != ParticipantType.PARTICIPANT)
			return;
		// Player dropped
		if (arena.getOut().isInside(event.getTo()))
		{
			arena.PlayerDeath(player);
			return;
		}
		// Player escaped
		if (!arena.getArena().isInside(player))
		{
			Integer ID = escapes.get(player);
			if (ID == null)
			{
				ID = Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(arena.getPlugin(), new ArenaSpleefKicker(arena, player), 120);
				arena.sendLocaleMessage("WARNINGLEAVE", player);
			}
		}
		else
		{
			Integer ID = escapes.remove(player);
			if (ID != null)
				Bukkit.getServer().getScheduler().cancelTask(ID);
		}
	}
}
