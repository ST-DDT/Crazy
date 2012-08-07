package de.st_ddt.crazyonline.databases;

import java.util.Date;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyonline.data.OnlinePlayerData;
import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyutil.databases.MySQLColumn;
import de.st_ddt.crazyutil.databases.MySQLDatabase;
import de.st_ddt.crazyutil.databases.PlayerDataDatabase;

public class CrazyOnlineMySQLDatabase extends MySQLDatabase<OnlinePlayerData> implements PlayerDataDatabase<OnlinePlayerData>
{

	public CrazyOnlineMySQLDatabase(final String tableName, final ConfigurationSection config)
	{
		super(OnlinePlayerData.class, tableName, config, getColumns(config), 0);
	}

	private static MySQLColumn[] getColumns(final ConfigurationSection config)
	{
		final MySQLColumn[] columns = new MySQLColumn[5];
		final String defaultDate = "'" + CrazyPluginInterface.DateFormat.format(new Date(0)) + "'";
		columns[0] = new MySQLColumn(config.getString("column.name", "name"), "CHAR(50)", true, false);
		columns[1] = new MySQLColumn(config.getString("column.firstLogin", "firstLogin"), "CHAR(19)", defaultDate, false, false);
		columns[2] = new MySQLColumn(config.getString("column.lastLogin", "lastLogin"), "CHAR(19)", defaultDate, false, false);
		columns[3] = new MySQLColumn(config.getString("column.lastLogout", "lastLogout"), "CHAR(19)", defaultDate, false, false);
		columns[4] = new MySQLColumn(config.getString("column.onlineTime", "onlineTime"), "INT", "0", false, false);
		columns[0] = new MySQLColumn(config.getString("column.ip", "ip"), "CHAR(50)", true, false);
		return columns;
	}

	@Override
	public OnlinePlayerData getEntry(final OfflinePlayer player)
	{
		return getEntry(player.getName());
	}

	@Override
	public boolean hasEntry(final OfflinePlayer player)
	{
		return hasEntry(player.getName());
	}

	@Override
	public boolean deleteEntry(final OfflinePlayer player)
	{
		return deleteEntry(player.getName());
	}
}
