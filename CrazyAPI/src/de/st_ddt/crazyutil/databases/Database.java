package de.st_ddt.crazyutil.databases;

import java.util.Collection;

public interface Database<S extends DatabaseEntry>
{

	public DatabaseType getType();

	public Class<S> getEntryClazz();

	public String getTableName();

	public String[] getColumnNames();

	public void checkTable();

	public boolean isStaticDatabase();

	public boolean hasEntry(String key);

	public S getEntry(String key);

	public Collection<S> getAllEntries();

	public S loadEntry(String key);

	public void loadAllEntries();

	public void saveDatabase();

	public void save(S entry);

	public void saveAll(Collection<S> entries);

	public boolean deleteEntry(String key);
	
	public void deleteAllEntries();
}
