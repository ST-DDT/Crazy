package de.st_ddt.crazyspawner.tasks;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.CompatibilityHelper;
import de.st_ddt.crazyspawner.entities.meta.NameMeta;

public class HealthTask implements Runnable
{

	private final Collection<LivingEntity> entities = new LinkedList<LivingEntity>();
	private final CrazySpawner plugin;
	private int taskID = -1;

	public HealthTask(final CrazySpawner plugin)
	{
		super();
		this.plugin = plugin;
	}

	public synchronized void queue(final LivingEntity entity)
	{
		entities.add(entity);
		if (taskID == -1)
			taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this);
	}

	@Override
	public synchronized void run()
	{
		for (final LivingEntity entity : entities)
		{
			final List<MetadataValue> metas = entity.getMetadata(NameMeta.METAHEADER);
			for (final MetadataValue meta : metas)
				if (meta.getOwningPlugin() == plugin)
				{
					final NameMeta name = (NameMeta) meta;
					if (entity.isValid())
					{
						final double health = CompatibilityHelper.getHealth(entity);
						if (health == -1)
							entity.setCustomName(name.asString());
						else
							entity.setCustomName(name.asString() + " (" + ((long) health) + ")");
					}
					else
						entity.setCustomName(name.asString());
				}
		}
		entities.clear();
		taskID = -1;
	}
}
