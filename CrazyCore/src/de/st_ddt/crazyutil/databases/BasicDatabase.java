package de.st_ddt.crazyutil.databases;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ChatHelper;

public abstract class BasicDatabase<S extends DatabaseEntry> implements Database<S>
{

	final Map<String, S> datas = Collections.synchronizedMap(new HashMap<String, S>());
	final DatabaseType type;
	final Class<S> clazz;
	final Constructor<S> constructor;
	final String[] defaultColumnNames;

	public BasicDatabase(final DatabaseType type, final Class<S> clazz, final String[] defaultColumnNames)
	{
		super();
		this.type = type;
		this.clazz = clazz;
		this.constructor = getConstructor(clazz);
		this.defaultColumnNames = defaultColumnNames;
	}

	@Override
	public final DatabaseType getType()
	{
		return type;
	}

	@Override
	public Class<S> getEntryClazz()
	{
		return clazz;
	}

	abstract Constructor<S> getConstructor(final Class<S> clazz);

	@Override
	public abstract void initialize() throws Exception;

	@Override
	public void checkTable() throws Exception
	{
	}

	@Override
	public abstract boolean isStaticDatabase();

	@Override
	public abstract boolean isCachedDatabase();

	@Override
	public boolean hasEntry(final String key)
	{
		return datas.containsKey(key.toLowerCase());
	}

	@Override
	public final S getEntry(final String key)
	{
		return datas.get(key.toLowerCase());
	}

	@Override
	public final S getOrLoadEntry(final String key)
	{
		final S data = getEntry(key);
		if (isStaticDatabase())
			return data;
		else if (data == null)
			return loadEntry(key);
		else
			return data;
	}

	@Override
	public Object getDatabaseLock()
	{
		return datas;
	}

	@Override
	public final Collection<S> getAllEntries()
	{
		return datas.values();
	}

	@Override
	public final int size()
	{
		return datas.size();
	}

	@Override
	public abstract S updateEntry(final String key);

	@Override
	public abstract S loadEntry(String key);

	@Override
	public abstract void loadAllEntries();

	@Override
	public boolean unloadEntry(final String key)
	{
		save(key);
		return datas.remove(key.toLowerCase()) != null;
	}

	@Override
	public void unloadAllEntries()
	{
		synchronized (datas)
		{
			saveAll(getAllEntries());
		}
		datas.clear();
	}

	@Override
	public void save(final String key)
	{
		final S entry = datas.get(key.toLowerCase());
		if (entry != null)
			save(entry);
	}

	@Override
	public void save(final S entry)
	{
		datas.put(entry.getName().toLowerCase(), entry);
	}

	@Override
	public void saveAll(final Collection<S> entries)
	{
		for (final S entry : entries)
			save(entry);
		saveDatabase();
	}

	@Override
	public boolean deleteEntry(final String key)
	{
		return datas.remove(key.toLowerCase()) != null;
	}

	@Override
	public void purgeDatabase()
	{
		datas.clear();
		saveDatabase();
	}

	@Override
	public abstract void saveDatabase();

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "saveType", type.toString());
	}

	final void shortPrintStackTrace(final Throwable main, final Throwable throwable)
	{
		ChatHelper.shortPrintStackTrace(main, throwable, this);
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " (Contains " + size() + " entries of type " + clazz.getSimpleName() + ")";
	}
}
