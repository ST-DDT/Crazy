package de.st_ddt.crazyutil.databases;

import java.sql.ResultSet;

public abstract class MySQLDatabaseEntry<S extends DatabaseSaveable> extends DatabaseEntry<S, ResultSet>
{

	protected final MySQLConnection connection;

	public MySQLDatabaseEntry(MySQLConnection connection)
	{
		super();
		this.connection = connection;
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
