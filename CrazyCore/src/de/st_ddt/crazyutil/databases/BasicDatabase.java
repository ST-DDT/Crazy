package de.st_ddt.crazyutil.databases;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;

public abstract class BasicDatabase<S extends DatabaseEntry> implements Database<S>
{

	private final DatabaseType type;
	protected final Class<S> clazz;
	protected final String tableName;
	protected final String[] columnNames;
	protected final Constructor<S> constructor;
	protected boolean bulkOperation = false;
	protected final HashMap<String, S> datas = new HashMap<String, S>();

	public BasicDatabase(final DatabaseType type, final Class<S> clazz, final String tableName, final ConfigurationSection config, final String[] columnNames, final Constructor<S> constructor)
	{
		super();
		this.type = type;
		this.clazz = clazz;
		this.tableName = tableName;
		this.columnNames = columnNames;
		for (int i = 0; i < columnNames.length; i++)
		{
			final String column = columnNames[i];
			columnNames[i] = config.getString("database.column." + column, column);
			config.set("database.column." + column, column);
		}
		this.constructor = constructor;
	}

	@Override
	public final DatabaseType getType()
	{
		return type;
	}

	@Override
	public final boolean isStaticDatabase()
	{
		return type.isStaticDatabase();
	}

	@Override
	public final String getTableName()
	{
		return tableName;
	}

	@Override
	public void checkTable()
	{
	}

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
	public final Collection<S> getAllEntries()
	{
		return datas.values();
	}

	@Override
	public void save(S entry)
	{
		datas.put(entry.getName().toLowerCase(), entry);
	}

	@Override
	public final void saveAll(final Collection<S> entries)
	{
		bulkOperation = true;
		for (final S entry : entries)
			save(entry);
		bulkOperation = false;
		saveDatabase();
	}

	public abstract void saveDatabase();

	@Override
	public boolean deleteEntry(final String key)
	{
		return datas.remove(key.toLowerCase()) != null;
	}

	@Override
	public void deleteAllEntries()
	{
		datas.clear();
	}

	@Override
	public String[] getColumnNames()
	{
		return columnNames;
	}

	/*
	 * (non-Javadoc)
	 * @see de.st_ddt.crazyutil.databases.Database#getEntryClazz()
	 */
	@Override
	public Class<S> getEntryClazz()
	{
		return clazz;
	}
}
