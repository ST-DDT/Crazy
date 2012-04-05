package de.st_ddt.crazyutil.databases;

import java.sql.ResultSet;

public abstract class MySQLDatabaseEntry<S extends DatabaseSaveable> extends DatabaseEntry<S, ResultSet>
{

	protected final MySQLConnection connection;
	protected String table;

	public MySQLDatabaseEntry(MySQLConnection connection, String table)
	{
		super();
		this.connection = connection;
		this.table = table;
	}

	@Override
	public abstract S load(ResultSet rawData);

	@Override
	public final void save(S data, ResultSet saveTo)
	{
		save(data);
	}

	public abstract void save(S data);
}
