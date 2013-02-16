package de.st_ddt.crazyutil.databases;

public interface SQLiteDatabaseEntry extends SQLDatabaseEntry
{

	// public SQLiteDatabaseEntry(ResultSet rawData, String[] columnNames);
	public String saveInsertToSQLiteDatabase(String[] columnNames);

	public String saveUpdateToSQLiteDatabase(String[] columnNames);
}
