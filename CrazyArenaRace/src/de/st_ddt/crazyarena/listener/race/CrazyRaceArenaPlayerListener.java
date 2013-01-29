package de.st_ddt.crazyarena.listener.race;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

import de.st_ddt.crazyarena.arenas.ArenaStatus;
import de.st_ddt.crazyarena.arenas.race.RaceArena;
import de.st_ddt.crazyarena.participants.race.RaceParticipant;

public class CrazyRaceArenaPlayerListener implements Listener
{

	private final RaceArena arena;

	public CrazyRaceArenaPlayerListener(final RaceArena arena)
	{
		this.arena = arena;
	}

	@EventHandler
	public void PlayerMove(final PlayerMoveEvent event)
	{
		final Player player = event.getPlayer();
		final RaceParticipant participant = arena.getParticipant(player);
		if (participant == null)
			return;
		if (!participant.isPlayer())
			return;
		if (arena.getStatus() == ArenaStatus.WAITING)
		{
			event.setCancelled(true);
			Bukkit.getScheduler().scheduleSyncDelayedTask(arena.getArenaPlugin(), new Runnable()
			{

				@Override
				public void run()
				{
					player.teleport(participant.getStart(), TeleportCause.PLUGIN);
				}
			});
		}
		else if (arena.getStatus() == ArenaStatus.PLAYING)
			if (participant.getStage() != null)
				if (participant.getStage().isInside(player))
					participant.reachStage();
	}

	@EventHandler
	public void VehicleMoveEvent(final VehicleMoveEvent event)
	{
		final Vehicle vehicle = event.getVehicle();
		if (!(vehicle.getPassenger() instanceof Player))
			return;
		final Player player = (Player) vehicle.getPassenger();
		final RaceParticipant participant = arena.getParticipant(player);
		if (participant == null)
			return;
		if (!participant.isPlayer())
			return;
		if (arena.getStatus() == ArenaStatus.WAITING)
		{
			vehicle.setVelocity(new Vector());
			Bukkit.getScheduler().scheduleSyncDelayedTask(arena.getArenaPlugin(), new Runnable()
			{

				@Override
				public void run()
				{
					vehicle.teleport(participant.getStart(), TeleportCause.PLUGIN);
				}
			});
		}
		else if (arena.getStatus() == ArenaStatus.PLAYING)
			if (participant.getStage().isInside(player))
				participant.reachStage();
	}
}
