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
		checkTable();
	}

	private static Column[] getColumns()
	{
		Column[] columns = new Column[5];
		columns[0] = getPrimaryColumn();
		columns[1] = new Column("FirstLogin", "CHAR(19)", null, false, false);
		columns[2] = new Column("LastLogin", "CHAR(19)", null, false, false);
		columns[3] = new Column("LastLogout", "CHAR(19)", null, false, false);
		columns[4] = new Column("OnlineTime", "INT", null, false, false);
		return columns;
	}

	private static Column getPrimaryColumn()
	{
		return new Column("Name", "CHAR(50)", true, false);
	}
}
