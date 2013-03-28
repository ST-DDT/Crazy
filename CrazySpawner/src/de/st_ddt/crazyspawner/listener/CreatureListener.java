package de.st_ddt.crazyspawner.listener;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.data.CreatureMeta;

public class CreatureListener implements Listener
{

	private final CrazySpawner plugin;

	public CreatureListener(final CrazySpawner plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void CreatureDamage(final EntityDamageEvent event)
	{
		if (!(event.getEntity() instanceof LivingEntity))
			return;
		final LivingEntity entity = (LivingEntity) event.getEntity();
		final List<MetadataValue> metas = entity.getMetadata("CreatureMeta");
		for (final MetadataValue meta : metas)
			if (meta.getOwningPlugin() == plugin)
			{
				final CreatureMeta name = (CreatureMeta) meta;
				entity.setCustomName(name.asString() + " (" + entity.getHealth() + ")");
			}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void CreatureDeath(final EntityDeathEvent event)
	{
		final LivingEntity entity = event.getEntity();
		final List<MetadataValue> metas = entity.getMetadata("CreatureMeta");
		for (final MetadataValue meta : metas)
			if (meta.getOwningPlugin() == plugin)
			{
				final CreatureMeta name = (CreatureMeta) meta;
				entity.setCustomName(name.asString());
			}
	}
}
