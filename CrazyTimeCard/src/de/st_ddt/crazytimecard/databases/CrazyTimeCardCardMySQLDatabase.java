package de.st_ddt.crazytimecard.databases;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazytimecard.data.CardData;
import de.st_ddt.crazyutil.databases.MySQLColumn;
import de.st_ddt.crazyutil.databases.MySQLDatabase;

public final class CrazyTimeCardCardMySQLDatabase extends MySQLDatabase<CardData>
{

	public CrazyTimeCardCardMySQLDatabase(final ConfigurationSection config)
	{
		super(CardData.class, getMySQLColumns(), "CrazyTimeCard_cards", config);
	}

	public CrazyTimeCardCardMySQLDatabase(final String tableName, final String[] columnNames, final String host, final String port, final String database, final String user, final String password, final boolean cached, final boolean doNotUpdate)
	{
		super(CardData.class, getMySQLColumns(), tableName, columnNames, host, port, database, user, password, cached, doNotUpdate);
	}

	private static MySQLColumn[] getMySQLColumns()
	{
		final MySQLColumn[] columns = new MySQLColumn[4];
		columns[0] = new MySQLColumn("name", "CHAR(255)", true, false);
		columns[1] = new MySQLColumn("owner", "CHAR(255)", null, false, false);
		columns[1] = new MySQLColumn("user", "CHAR(255)", null, false, false);
		columns[2] = new MySQLColumn("duration", "BIGINT", null, false, false);
		return columns;
	}
}
