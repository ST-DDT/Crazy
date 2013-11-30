package de.st_ddt.crazyspawner.entities.persistance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataValue;

public class PersistanceManager
{

	public static void registerPersistableState(final Class<? extends PersistantState> stateClass)
	{
		ConfigurationSerialization.registerClass(stateClass);
	}

	private final Map<UUID, Map<String, PersistantState>> entities = new HashMap<UUID, Map<String, PersistantState>>();
	protected final File dataDir;

	public PersistanceManager(final File dataDir)
	{
		super();
		this.dataDir = dataDir;
		this.dataDir.mkdirs();
	}

	public void watch(final Entity entity, final String key, final PersistantState state)
	{
		if (entity == null)
			return;
		if (entity.isDead())
		{
			delete(entity);
			return;
		}
		if (!entities.containsKey(entity.getUniqueId()))
			entities.put(entity.getUniqueId(), new TreeMap<String, PersistantState>(String.CASE_INSENSITIVE_ORDER));
		final Map<String, PersistantState> map = entities.get(entity.getUniqueId());
		map.put(key, state);
	}

	public void unwatch(final Entity entity, final String key)
	{
		if (entity == null)
			return;
		if (entity.isDead())
		{
			delete(entity);
			return;
		}
		if (!entities.containsKey(entity.getUniqueId()))
			return;
		final Map<String, PersistantState> map = entities.get(entity.getUniqueId());
		map.remove(key);
	}

	public void loadChunk(final Chunk chunk)
	{
		final Entity[] list = chunk.getEntities();
		for (final Entity entity : list)
			load(entity);
	}

	public void unloadChunk(final Chunk chunk)
	{
		final Entity[] list = chunk.getEntities();
		for (final Entity entity : list)
		{
			persist(entity);
			entities.remove(entity.getUniqueId());
		}
	}

	public void load(final Entity entity)
	{
		if (entity == null)
			return;
		final File file = getEntityDataFile(entity);
		if (!file.exists())
			return;
		final YamlConfiguration data = new YamlConfiguration();
		try
		{
			data.load(file);
		}
		catch (final Exception e)
		{
			System.err.println("Could not load " + file.getPath());
			e.printStackTrace();
			return;
		}
		final List<PersistantState> persistables = new ArrayList<PersistantState>();
		final ConfigurationSection dataSection = data.getConfigurationSection("data");
		for (final String key : dataSection.getKeys(false))
		{
			final Object obj = dataSection.get(key);
			System.out.println("Restoring: " + key + " - " + obj);
			if (obj instanceof PersistantState)
				persistables.add((PersistantState) obj);
			else
				System.err.println("Trashdata found: " + obj);
		}
		for (final PersistantState persistable : persistables)
			persistable.attachTo(entity, this);
	}

	public void persist(final Entity entity)
	{
		if (entity == null)
			return;
		if (entity.isDead())
		{
			entities.remove(entity.getUniqueId());
			delete(entity);
			return;
		}
		final Map<String, PersistantState> map = entities.get(entity.getUniqueId());
		if (map == null)
		{
			delete(entity);
			return;
		}
		final YamlConfiguration data = new YamlConfiguration();
		data.set("type", entity.getType().name());
		data.set("timeSaved", System.currentTimeMillis());
		data.set("world", entity.getWorld().getName());
		for (final Entry<String, PersistantState> entry : map.entrySet())
			if (entry.getValue() == null)
			{
				final List<MetadataValue> metadatas = entity.getMetadata(entry.getKey());
				for (final MetadataValue metadata : metadatas)
					if (metadata instanceof PersistantState)
					{
						System.out.println("Persisting: " + entry.getKey() + " - " + metadata.toString());
						data.set("data." + entry.getKey(), metadata);
						return;
					}
			}
			else
			{
				System.out.println("Persisting: " + entry.getKey() + " - " + entry.getValue().toString());
				data.set("data." + entry.getKey(), entry.getValue());
			}
		try
		{
			data.save(getEntityDataFile(entity));
		}
		catch (final IOException e)
		{
			System.err.println("Could not save EntityProperties " + getEntityDataFile(entity).getPath());
			e.printStackTrace();
		}
	}

	public void delete(final Entity entity)
	{
		if (entity == null)
			return;
		final File file = getEntityDataFile(entity);
		if (!file.exists())
			return;
		if (!file.delete())
			file.deleteOnExit();
	}

	protected File getEntityDataFile(final Entity entity)
	{
		return new File(dataDir, entity.getUniqueId().toString() + ".yml");
	}
}
