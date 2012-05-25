package de.st_ddt.crazyutil.databases;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;

public abstract class BasicDatabase<S extends DatabaseEntry> implements Database<S>
{

	private final DatabaseType type;
	protected final Class<S> clazz;
	protected final String[] columnNames;
	protected final Constructor<S> constructor;
	protected boolean bulkOperation = false;

	public BasicDatabase(final DatabaseType type, final Class<S> clazz, final String[] columnNames, final Constructor<S> constructor)
	{
		super();
		this.type = type;
		this.clazz = clazz;
		this.columnNames = columnNames;
		this.constructor = constructor;
	}

	@Override
	public DatabaseType getType()
	{
		return type;
	}

	@Override
	public abstract String getTableName();

	@Override
	public abstract void checkTable();

	@Override
	public abstract S getEntry(String key);

	@Override
	public abstract List<S> getEntries(String key);

	@Override
	public abstract List<S> getAllEntries();

	@Override
	public abstract void delete(String key);

	@Override
	public abstract void save(S entry);

	@Override
	public final void saveAll(Collection<S> entries)
	{
		bulkOperation = true;
		for (S entry : entries)
			save(entry);
		bulkOperation = false;
		saveDatabase();
	}

	protected abstract void saveDatabase();

	/*
	 * (non-Javadoc)
	 * @see de.st_ddt.crazyutil.databases.Database#getColumnNames()
	 */
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
