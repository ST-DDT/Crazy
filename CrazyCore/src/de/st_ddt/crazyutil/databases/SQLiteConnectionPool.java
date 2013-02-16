package de.st_ddt.crazyutil.databases;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteConnectionPool extends SQLConnectionPool
{

	public SQLiteConnectionPool(final SQLConnection mysqlConnection)
	{
		super(mysqlConnection);
	}

	public SQLiteConnectionPool(final SQLConnection mysqlConnection, final int maxConnections)
	{
		super(mysqlConnection, maxConnections);
	}

	@Override
	public boolean isValid(final Connection connection) throws SQLException
	{
		return !connection.isClosed();
	}
}
