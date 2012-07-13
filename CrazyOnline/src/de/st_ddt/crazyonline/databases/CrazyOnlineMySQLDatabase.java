package de.st_ddt.crazyonline.databases;

import java.util.Date;

import de.st_ddt.crazyonline.OnlinePlayerData;
import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyutil.databases.MySQLColumn;
import de.st_ddt.crazyutil.databases.MySQLConnection;
import de.st_ddt.crazyutil.databases.MySQLDatabase;

public class CrazyOnlineMySQLDatabase extends MySQLDatabase<OnlinePlayerData>
{

	public CrazyOnlineMySQLDatabase(final MySQLConnection connection, final String table, final String colName, final String colFirstLogin, final String colLastLogin, final String colLastLogout, final String colOnlineTime)
	{
		super(OnlinePlayerData.class, connection, table, getColumns(colName, colFirstLogin, colLastLogin, colLastLogout, colOnlineTime), 0);
		checkTable();
	}

	private static MySQLColumn[] getColumns(final String colName, final String colFirstLogin, final String colLastLogin, final String colLastLogout, final String colOnlineTime)
	{
		final MySQLColumn[] columns = new MySQLColumn[5];
		final String defaultDate = "'" + CrazyPluginInterface.DateFormat.format(new Date(0)) + "'";
		columns[0] = new MySQLColumn(colName, "CHAR(50)", true, false);
		columns[1] = new MySQLColumn(colFirstLogin, "CHAR(19)", defaultDate, false, false);
		columns[2] = new MySQLColumn(colLastLogin, "CHAR(19)", defaultDate, false, false);
		columns[3] = new MySQLColumn(colLastLogout, "CHAR(19)", defaultDate, false, false);
		columns[4] = new MySQLColumn(colOnlineTime, "INT", "0", false, false);
		return columns;
	}
}
