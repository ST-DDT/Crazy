package de.st_ddt.crazyspawner.tasks;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.meta.NameMeta;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.VersionComparator;

public class HealthTask implements Runnable
{

	private final Collection<LivingEntity> entities = new LinkedList<LivingEntity>();
	private final CrazySpawner plugin;
	private final HealthGetter getHealth;
	private int taskID = -1;

	public HealthTask(final CrazySpawner plugin)
	{
		super();
		this.plugin = plugin;
		HealthGetter getHealth;
		if (VersionComparator.compareVersions(ChatHelper.getMinecraftVersion(), "1.6.1") >= 0)
			getHealth = new HealthGetter_161();
		else if (VersionComparator.compareVersions(ChatHelper.getMinecraftVersion(), "1.4.6") >= 0)
			try
			{
				getHealth = new HealthGetter_146();
			}
			catch (final Exception e)
			{
				getHealth = new HealthGetter_Pre146();
				e.printStackTrace();
			}
		else
			getHealth = new HealthGetter_Pre146();
		this.getHealth = getHealth;
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
						entity.setCustomName(name.asString() + " (" + getHealth.getHealth(entity) + ")");
					else
						entity.setCustomName(name.asString());
				}
		}
		entities.clear();
		taskID = -1;
	}

	private interface HealthGetter
	{

		public String getHealth(LivingEntity entity);
	}

	private final static class HealthGetter_Pre146 implements HealthGetter
	{

		@Override
		public String getHealth(final LivingEntity entity)
		{
			return "?";
		}
	}

	private final static class HealthGetter_146 implements HealthGetter
	{

		private final Method getHealth;

		private HealthGetter_146() throws SecurityException, NoSuchMethodException
		{
			getHealth = Damageable.class.getDeclaredMethod("getHealth");
		}

		@Override
		public String getHealth(final LivingEntity entity)
		{
			try
			{
				return getHealth.invoke(entity).toString();
			}
			catch (final Exception e)
			{
				return "?";
			}
		}
	}

	private final static class HealthGetter_161 implements HealthGetter
	{

		@Override
		public String getHealth(final LivingEntity entity)
		{
			return Long.toString((long) entity.getHealth());
		}
	}
}
