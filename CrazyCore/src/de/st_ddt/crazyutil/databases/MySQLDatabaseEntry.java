package de.st_ddt.crazyutil.databases;

public interface MySQLDatabaseEntry extends DatabaseEntry
{

	// public MySQLDatabaseEntry(ResultSet rawData, String[] columnNames);
	public abstract void saveToMySQLDatabase(MySQLConnection connection, String table, String[] columnNames);
}
