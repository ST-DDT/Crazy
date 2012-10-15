package de.st_ddt.crazyutil.databases;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MySQLConnectionPool
{

	private final Queue<Connection> idleConenctions = new LinkedList<Connection>();
	private int busyConnections = 0;
	private final int maxConnections;
	private final MySQLConnection mysqlConnection;
	private final Lock lock = new ReentrantLock();

	public MySQLConnectionPool(final MySQLConnection mysqlConnection)
	{
		this(mysqlConnection, 10);
	}

	public MySQLConnectionPool(final MySQLConnection mysqlConnection, final int maxConnections)
	{
		super();
		this.mysqlConnection = mysqlConnection;
		this.maxConnections = maxConnections;
	}

	public MySQLConnection getMysqlConnection()
	{
		return mysqlConnection;
	}

	public int getActiveConnections()
	{
		return busyConnections;
	}

	public void reset()
	{
		lock.lock();
		Iterator<Connection> it = idleConenctions.iterator();
		while (it.hasNext())
			try
			{
				it.next().close();
			}
			catch (SQLException e)
			{}
			finally
			{
				it.remove();
			}
		busyConnections = 0;
		lock.unlock();
	}

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
					return mysqlConnection.openConnection();
				}
			else if (connection.isValid(1))
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

	public void releaseConnection(final Connection connection)
	{
		if (connection == null)
			return;
		lock.lock();
		try
		{
			if (busyConnections == 0)
				connection.close();
			else
			{
				busyConnections--;
				if (!connection.isClosed())
					idleConenctions.add(connection);
			}
		}
		catch (final SQLException e)
		{}
		finally
		{
			lock.unlock();
		}
	}
}
