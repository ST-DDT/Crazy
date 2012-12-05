package de.st_ddt.crazyutil.databases;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteConnectionPool extends ConnectionPool
{

	public SQLiteConnectionPool(final MainConnection mysqlConnection)
	{
		super(mysqlConnection);
	}

	public SQLiteConnectionPool(final MainConnection mysqlConnection, final int maxConnections)
	{
		super(mysqlConnection, maxConnections);
	}

	@Override
	public Connection getConnection()
	{
		lock.lock();
		try
		{
			final Connection connection = idleConenctions.poll();
			if (connection == null)
				if (busyConnections >= maxConnections)
					return null;
				else
				{
					busyConnections++;
					return mainConnection.openConnection();
				}
			else if (!connection.isClosed())
				return connection;
			else
			{
				connection.close();
				return getConnection();
			}
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			lock.unlock();
		}
	}
}
