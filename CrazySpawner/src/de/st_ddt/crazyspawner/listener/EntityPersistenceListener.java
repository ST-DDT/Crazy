package de.st_ddt.crazyspawner.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.persistance.PersistanceManager;

public class EntityPersistenceListener implements Listener
{

	protected final CrazySpawner plugin;
	protected final PersistanceManager manager;

	public EntityPersistenceListener(final CrazySpawner plugin, final PersistanceManager manager)
	{
		super();
		this.plugin = plugin;
		this.manager = manager;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void ChunkLoad(final ChunkLoadEvent event)
	{
		manager.loadChunk(event.getChunk());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void ChunkUnload(final ChunkUnloadEvent event)
	{
		manager.unloadChunk(event.getChunk());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void EntityDeath(final EntityDeathEvent event)
	{
		manager.delete(event.getEntity());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void ItemDespawn(final ItemDespawnEvent event)
	{
		manager.delete(event.getEntity());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void EntityExplode(final EntityExplodeEvent event)
	{
		manager.delete(event.getEntity());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void EntityChangeBlock(final EntityChangeBlockEvent event)
	{
		final Entity entity = event.getEntity();
		final EntityType type = entity.getType();
		switch (type)
		{
			case FALLING_BLOCK:
				manager.delete(entity);
			default:
		}
	}
}
