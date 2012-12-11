package de.st_ddt.crazyonline.databases;

import java.util.Date;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyonline.data.OnlinePlayerData;
import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyutil.databases.PlayerDataDatabase;
import de.st_ddt.crazyutil.databases.SQLColumn;
import de.st_ddt.crazyutil.databases.SQLitePlayerDataDatabase;

public class CrazyOnlineSQLiteDatabase extends SQLitePlayerDataDatabase<OnlinePlayerData> implements PlayerDataDatabase<OnlinePlayerData>
{

	public CrazyOnlineSQLiteDatabase(final ConfigurationSection config)
	{
		super(OnlinePlayerData.class, getOnlineColumns(), "plugins/CrazyOnline/accounts.sqlite", "CrazyOnline_accounts", config);
	}

	public CrazyOnlineSQLiteDatabase(final String tableName, final String[] columnNames, final String path, final boolean cached, final boolean doNotUpdate)
	{
		super(OnlinePlayerData.class, getOnlineColumns(), tableName, columnNames, path, cached, doNotUpdate);
	}

	private static SQLColumn[] getOnlineColumns()
	{
		final SQLColumn[] columns = new SQLColumn[9];
		final String defaultDate = "'" + CrazyLightPluginInterface.DATETIMEFORMAT.format(new Date(0)) + "'";
		columns[0] = new SQLColumn("name", "CHAR(255)", true, false);
		columns[1] = new SQLColumn("firstLogin", "CHAR(19)", defaultDate, false, false);
		columns[2] = new SQLColumn("lastLogin", "CHAR(19)", defaultDate, false, false);
		columns[3] = new SQLColumn("lastLogout", "CHAR(19)", defaultDate, false, false);
		columns[4] = new SQLColumn("onlineTime", "INT", "0", false, false);
		columns[5] = new SQLColumn("onlineTimeMonth", "INT", "0", false, false);
		columns[6] = new SQLColumn("onlineTimeWeek", "INT", "0", false, false);
		columns[7] = new SQLColumn("onlineTimeDay", "INT", "0", false, false);
		columns[8] = new SQLColumn("ip", "CHAR(50)", "''", false, false);
		return columns;
	}
}
