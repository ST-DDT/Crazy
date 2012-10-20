package de.st_ddt.crazyonline.databases;

import java.util.Date;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyonline.data.OnlinePlayerData;
import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyutil.databases.MySQLColumn;
import de.st_ddt.crazyutil.databases.MySQLPlayerDataDatabase;
import de.st_ddt.crazyutil.databases.PlayerDataDatabase;

public class CrazyOnlineMySQLDatabase extends MySQLPlayerDataDatabase<OnlinePlayerData> implements PlayerDataDatabase<OnlinePlayerData>
{

	public CrazyOnlineMySQLDatabase(final ConfigurationSection config)
	{
		super(OnlinePlayerData.class, getOnlineColumns(), "CrazyOnline_accounts", config);
	}

	public CrazyOnlineMySQLDatabase(final String tableName, final String[] columnNames, final String host, final String port, final String database, final String user, final String password, final boolean cached, final boolean doNotUpdate)
	{
		super(OnlinePlayerData.class, getOnlineColumns(), tableName, columnNames, host, port, database, user, password, cached, doNotUpdate);
	}

	private static MySQLColumn[] getOnlineColumns()
	{
		final MySQLColumn[] columns = new MySQLColumn[6];
		final String defaultDate = "'" + CrazyLightPluginInterface.DATETIMEFORMAT.format(new Date(0)) + "'";
		columns[0] = new MySQLColumn("name", "CHAR(255)", true, false);
		columns[1] = new MySQLColumn("firstLogin", "CHAR(19)", defaultDate, false, false);
		columns[2] = new MySQLColumn("lastLogin", "CHAR(19)", defaultDate, false, false);
		columns[3] = new MySQLColumn("lastLogout", "CHAR(19)", defaultDate, false, false);
		columns[4] = new MySQLColumn("onlineTime", "INT", "0", false, false);
		columns[5] = new MySQLColumn("ip", "CHAR(50)", "''", false, false);
		return columns;
	}
}
