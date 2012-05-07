package de.st_ddt.crazyarena.pve;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

public class ArenaPvEEntityListener implements Listener
{

	private final ArenaPvE arena;

	public ArenaPvEEntityListener(ArenaPvE arena)
	{
		this.arena = arena;
	}

	public ArenaPvE getArena()
	{
		return arena;
	}

	@EventHandler
	public void EntityDeathEvent(EntityDeathEvent event)
	{
		Entity entity = event.getEntity();
		if (!(entity instanceof Creature))
			return;
		arena.removeEnemy(entity);
	}
}
