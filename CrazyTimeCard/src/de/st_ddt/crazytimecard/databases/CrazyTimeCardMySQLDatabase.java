package de.st_ddt.crazytimecard.databases;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazytimecard.data.PlayerTimeData;
import de.st_ddt.crazyutil.databases.MySQLPlayerDataDatabase;
import de.st_ddt.crazyutil.databases.SQLColumn;

public final class CrazyTimeCardMySQLDatabase extends MySQLPlayerDataDatabase<PlayerTimeData>
{

	public CrazyTimeCardMySQLDatabase(final ConfigurationSection config)
	{
		super(PlayerTimeData.class, getSQLColumns(), "CrazyTimeCard_accounts", config);
	}

	public CrazyTimeCardMySQLDatabase(final String tableName, final String[] columnNames, final String host, final String port, final String database, final String user, final String password, final boolean cached, final boolean doNotUpdate)
	{
		super(PlayerTimeData.class, getSQLColumns(), tableName, columnNames, host, port, database, user, password, cached, doNotUpdate);
	}

	private static SQLColumn[] getSQLColumns()
	{
		final SQLColumn[] columns = new SQLColumn[3];
		columns[0] = new SQLColumn("name", "CHAR(255)", true, false);
		columns[1] = new SQLColumn("card", "CHAR(255)", null, false, false);
		columns[2] = new SQLColumn("limit", "TIMESTAMP", null, false, false);
		return columns;
	}
}
