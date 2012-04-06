package de.st_ddt.crazyonline.databases;

import de.st_ddt.crazyonline.OnlinePlayerData;
import de.st_ddt.crazyutil.databases.Column;
import de.st_ddt.crazyutil.databases.MySQLConnection;
import de.st_ddt.crazyutil.databases.MySQLDatabase;

public class CrazyOnlineMySQLDatabase extends MySQLDatabase<OnlinePlayerData>
{

	public CrazyOnlineMySQLDatabase(MySQLConnection connection, String table)
	{
		super(OnlinePlayerData.class, connection, table, getColumns(), getPrimaryColumn());
	}

	private static Column[] getColumns()
	{
		return null;
	}

	private static Column getPrimaryColumn()
	{
		return null;
	}
}
