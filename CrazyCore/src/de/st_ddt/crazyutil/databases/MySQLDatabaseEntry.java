package de.st_ddt.crazyutil.databases;

public interface MySQLDatabaseEntry extends DatabaseEntry
{

	// public MySQLDatabaseEntry(ResultSet rawData);
	public abstract void save(MySQLConnection connection, String table);
}
