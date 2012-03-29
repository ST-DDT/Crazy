package de.st_ddt.crazyutil.databases;

import java.util.List;

public abstract class Database<S extends Saveable, T extends DatabaseEntry<S, ?>>
{

	private final DatabaseTypes type;

	public Database(DatabaseTypes type)
	{
		super();
		this.type = type;
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
