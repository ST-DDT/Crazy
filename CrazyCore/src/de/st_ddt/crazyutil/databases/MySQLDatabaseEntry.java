package de.st_ddt.crazyutil.databases;

public interface MySQLDatabaseEntry extends SQLDatabaseEntry
{

	// public MySQLDatabaseEntry(ResultSet rawData, String[] columnNames);
	public String saveToMySQLDatabase(String[] columnNames);
}
