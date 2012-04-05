package de.st_ddt.crazyonline.databases;

import java.sql.ResultSet;
import de.st_ddt.crazyonline.OnlinePlayerData;
import de.st_ddt.crazyutil.databases.Column;
import de.st_ddt.crazyutil.databases.MySQLConnection;
import de.st_ddt.crazyutil.databases.MySQLDatabase;
import de.st_ddt.crazyutil.databases.MySQLDatabaseEntry;

public class CrazyOnlineMySQLDatabase extends MySQLDatabase<OnlinePlayerData, MySQLDatabaseEntry<OnlinePlayerData>>
{

	public CrazyOnlineMySQLDatabase(MySQLConnection connection, String table)
	{
		super(new MySQLDatabaseEntry<OnlinePlayerData>(connection, table)
		{

			@Override
			public OnlinePlayerData load(ResultSet rawData)
			{
				return new OnlinePlayerData(rawData);
			}

			@Override
			public void save(OnlinePlayerData data)
			{
				// EDIT
			}
		}, connection, table, getColumns(), getPrimaryColumn());
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
