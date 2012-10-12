package de.st_ddt.crazyutil.databases;

public interface MySQLDatabaseEntry extends DatabaseEntry
{

	// public MySQLDatabaseEntry(ResultSet rawData, String[] columnNames);
	public String saveToMySQLDatabase(String[] columnNames);
}
