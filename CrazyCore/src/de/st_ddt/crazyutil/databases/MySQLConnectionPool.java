package de.st_ddt.crazyutil.databases;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnectionPool extends SQLConnectionPool
{

	public MySQLConnectionPool(final SQLConnection mysqlConnection, final int maxConnections)
	{
		super(mysqlConnection, maxConnections);
	}

	public MySQLConnectionPool(final SQLConnection mysqlConnection)
	{
		super(mysqlConnection);
	}

	@Override
	public boolean isValid(final Connection connection) throws SQLException
	{
		return connection.isValid(1);
	}
}
