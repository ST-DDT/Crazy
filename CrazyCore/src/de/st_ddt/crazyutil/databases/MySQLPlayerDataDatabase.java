package de.st_ddt.crazyutil.databases;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

public class MySQLPlayerDataDatabase<S extends MySQLPlayerDataDatabaseEntry> extends MySQLDatabase<S> implements PlayerDataDatabase<S>
{

	public MySQLPlayerDataDatabase(final Class<S> clazz, final SQLColumn[] columns, final String defaultTableName, final ConfigurationSection config)
	{
		super(clazz, columns, defaultTableName, config);
	}

	public MySQLPlayerDataDatabase(final Class<S> clazz, final SQLColumn[] columns, final String tableName, final String[] columnNames, final String host, final String port, final String database, final String user, final String password, final boolean cached, final boolean doNoUpdate)
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

	@Override
	public S updateEntry(final OfflinePlayer player)
	{
		return updateEntry(player.getName());
	}
}
