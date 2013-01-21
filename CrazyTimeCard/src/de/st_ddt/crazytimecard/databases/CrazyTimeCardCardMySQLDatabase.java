package de.st_ddt.crazytimecard.databases;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazytimecard.data.CardData;
import de.st_ddt.crazyutil.databases.SQLColumn;
import de.st_ddt.crazyutil.databases.MySQLDatabase;

public final class CrazyTimeCardCardMySQLDatabase extends MySQLDatabase<CardData>
{

	public CrazyTimeCardCardMySQLDatabase(final ConfigurationSection config)
	{
		super(CardData.class, getSQLColumns(), "CrazyTimeCard_cards", config);
	}

	public CrazyTimeCardCardMySQLDatabase(final String tableName, final String[] columnNames, final String host, final String port, final String database, final String user, final String password, final boolean cached, final boolean doNotUpdate)
	{
		super(CardData.class, getSQLColumns(), tableName, columnNames, host, port, database, user, password, cached, doNotUpdate);
	}

	private static SQLColumn[] getSQLColumns()
	{
		final SQLColumn[] columns = new SQLColumn[4];
		columns[0] = new SQLColumn("name", "CHAR(255)", true, false);
		columns[1] = new SQLColumn("owner", "CHAR(255)", null, false, false);
		columns[1] = new SQLColumn("user", "CHAR(255)", null, false, false);
		columns[2] = new SQLColumn("duration", "BIGINT", null, false, false);
		return columns;
	}
}
