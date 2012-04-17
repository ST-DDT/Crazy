package de.st_ddt.crazyonline.databases;

import de.st_ddt.crazyonline.OnlinePlayerData;
import de.st_ddt.crazyutil.databases.MySQLColumn;
import de.st_ddt.crazyutil.databases.MySQLConnection;
import de.st_ddt.crazyutil.databases.MySQLDatabase;

public class CrazyOnlineMySQLDatabase extends MySQLDatabase<OnlinePlayerData>
{

	public CrazyOnlineMySQLDatabase(MySQLConnection connection, String table, String colName, String colFirstLogin, String colLastLogin, String colLastLogout, String colOnlineTime)
	{
		super(OnlinePlayerData.class, connection, table, getColumns(colName, colFirstLogin, colLastLogin, colLastLogout, colOnlineTime), 0);
		checkTable();
	}

	private static MySQLColumn[] getColumns(String colName, String colFirstLogin, String colLastLogin, String colLastLogout, String colOnlineTime)
	{
		MySQLColumn[] columns = new MySQLColumn[5];
		columns[0] = new MySQLColumn(colName, "CHAR(50)", true, false);
		columns[1] = new MySQLColumn(colFirstLogin, "CHAR(19)", null, false, false);
		columns[2] = new MySQLColumn(colLastLogin, "CHAR(19)", null, false, false);
		columns[3] = new MySQLColumn(colLastLogout, "CHAR(19)", null, false, false);
		columns[4] = new MySQLColumn(colOnlineTime, "INT", null, false, false);
		return columns;
	}
}
