package de.st_ddt.crazyutil.databases;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;

public abstract class Database<S extends DatabaseEntry>
{

	private final DatabaseTypes type;
	protected final Class<S> clazz;
	protected final String[] columnNames;
	protected final Constructor<S> constructor;
	protected boolean bulkOperation = false;

	public Database(final DatabaseTypes type, final Class<S> clazz, final String[] columnNames, final Constructor<S> constructor)
	{
		super();
		this.type = type;
		this.clazz = clazz;
		this.columnNames = columnNames;
		this.constructor = constructor;
	}

	public DatabaseTypes getType()
	{
		return type;
	}

	public abstract void checkTable();

	public abstract S getEntry(String key);

	public abstract List<S> getEntries(String key);

	public abstract List<S> getAllEntries();

	public abstract void delete(String key);

	public abstract void save(S entry);

	public final void saveAll(Collection<S> entries)
	{
		bulkOperation = true;
		for (S entry : entries)
			save(entry);
		bulkOperation = false;
		saveDatabase();
	}

	protected abstract void saveDatabase();

	public String[] getColumnNames()
	{
		return columnNames;
	}

	public Class<S> getEntryClazz()
	{
		return clazz;
	}
}
