package de.st_ddt.crazyarena.listener.pve;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import de.st_ddt.crazyarena.arenas.pve.PvEArena;

public class PvEArenaEntityListener implements Listener
{

	private final PvEArena arena;

	public PvEArenaEntityListener(final PvEArena arena)
	{
		this.arena = arena;
	}

	public PvEArena getArena()
	{
		return arena;
	}

	@EventHandler
	public void EntityDeathEvent(final EntityDeathEvent event)
	{
		final LivingEntity entity = event.getEntity();
		if (entity instanceof Player)
			arena.playerDeath((Player) event.getEntity());
		else
			arena.creatureDeath(entity);
	}
}
