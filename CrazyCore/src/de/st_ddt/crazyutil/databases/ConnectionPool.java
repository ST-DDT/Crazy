package de.st_ddt.crazyutil.databases;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool
{

	protected final Queue<Connection> idleConenctions = new LinkedList<Connection>();
	protected int busyConnections = 0;
	protected final int maxConnections;
	protected final MainConnection mainConnection;
	protected final Lock lock = new ReentrantLock();

	public ConnectionPool(final MainConnection mysqlConnection)
	{
		this(mysqlConnection, 10);
	}

	public ConnectionPool(final MainConnection mysqlConnection, final int maxConnections)
	{
		super();
		this.mainConnection = mysqlConnection;
		this.maxConnections = maxConnections;
	}

	public MainConnection getMainConnection()
	{
		return mainConnection;
	}

	public int getActiveConnections()
	{
		return busyConnections;
	}

	public void reset()
	{
		lock.lock();
		final Iterator<Connection> it = idleConenctions.iterator();
		while (it.hasNext())
			try
			{
				it.next().close();
			}
			catch (final SQLException e)
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
					return mainConnection.openConnection();
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
