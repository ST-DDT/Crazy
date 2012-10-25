package de.st_ddt.crazyutil.databases;

public interface FlatDatabaseEntry extends DatabaseEntry
{

	// public FlatDatabaseEntry(String[] rawData);
	public String[] saveToFlatDatabase();
}
