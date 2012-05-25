package de.st_ddt.crazyutil.databases;

import java.util.Collection;
import java.util.List;

public interface Database<S extends DatabaseEntry>
{

	public abstract DatabaseType getType();

	public abstract String getTableName();

	public abstract void checkTable();

	public abstract S getEntry(String key);

	public abstract List<S> getEntries(String key);

	public abstract List<S> getAllEntries();

	public abstract void delete(String key);

	public abstract void save(S entry);

	public abstract void saveAll(Collection<S> entries);

	public abstract String[] getColumnNames();

	public abstract Class<S> getEntryClazz();
}
