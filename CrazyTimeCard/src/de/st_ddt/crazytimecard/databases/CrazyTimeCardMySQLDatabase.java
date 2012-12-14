package de.st_ddt.crazytimecard.databases;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazytimecard.data.PlayerTimeData;
import de.st_ddt.crazyutil.databases.MySQLColumn;
import de.st_ddt.crazyutil.databases.MySQLPlayerDataDatabase;

public final class CrazyTimeCardMySQLDatabase extends MySQLPlayerDataDatabase<PlayerTimeData>
{

	public CrazyTimeCardMySQLDatabase(final ConfigurationSection config)
	{
		super(PlayerTimeData.class, getMySQLColumns(), "CrazyTimeCard_accounts", config);
	}

	public CrazyTimeCardMySQLDatabase(final String tableName, final String[] columnNames, final String host, final String port, final String database, final String user, final String password, final boolean cached, final boolean doNotUpdate)
	{
		super(PlayerTimeData.class, getMySQLColumns(), tableName, columnNames, host, port, database, user, password, cached, doNotUpdate);
	}

	private static MySQLColumn[] getMySQLColumns()
	{
		final MySQLColumn[] columns = new MySQLColumn[3];
		columns[0] = new MySQLColumn("name", "CHAR(255)", true, false);
		columns[1] = new MySQLColumn("card", "CHAR(255)", null, false, false);
		columns[2] = new MySQLColumn("limit", "TIMESTAMP", null, false, false);
		return columns;
	}
}
