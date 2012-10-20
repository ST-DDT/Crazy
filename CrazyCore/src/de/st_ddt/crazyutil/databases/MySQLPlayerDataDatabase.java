package de.st_ddt.crazyutil.databases;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

public class MySQLPlayerDataDatabase<S extends MySQLPlayerDataDatabaseEntry> extends MySQLDatabase<S> implements PlayerDataDatabase<S>
{

	public MySQLPlayerDataDatabase(final Class<S> clazz, final MySQLColumn[] columns, final String defaultTableName, final ConfigurationSection config)
	{
		super(clazz, columns, defaultTableName, config);
	}

	public MySQLPlayerDataDatabase(Class<S> clazz, MySQLColumn[] columns, String tableName, String[] columnNames, String host, String port, String database, String user, String password, boolean cached, boolean doNoUpdate)
	{
		super(clazz, columns, tableName, columnNames, host, port, database, user, password, cached, doNoUpdate);
	}

	@Override
	public S getEntry(final OfflinePlayer player)
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
