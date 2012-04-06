package de.st_ddt.crazyutil.databases;

import java.util.List;

public abstract class Database<S extends DatabaseEntry>
{

	private final DatabaseTypes type;
	protected final Class<S> clazz;

	public Database(DatabaseTypes type, Class<S> clazz)
	{
		super();
		this.type = type;
		this.clazz = clazz;
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

	public final void saveAll(List<S> entries)
	{
		for (S entry : entries)
			save(entry);
	}
}
