package de.st_ddt.crazyarena.listener.pve;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;

import de.st_ddt.crazyarena.arenas.pve.PvEArena;

public class ArenaPvEEntityListener implements Listener
{

	private final PvEArena arena;

	public ArenaPvEEntityListener(final PvEArena arena)
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
		final Entity entity = event.getEntity();
		if (!(entity instanceof Creature))
			return;
		arena.removeEnemy(entity);
	}

	@EventHandler
	public void PlayerDeathEvent(final PlayerDeathEvent event)
	{
		arena.PlayerDeath(event.getEntity());
	}
}
