package de.st_ddt.crazyutil.databases;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

public class SQLitePlayerDataDatabase<S extends SQLitePlayerDataDatabaseEntry> extends SQLiteDatabase<S> implements PlayerDataDatabase<S>
{

	public SQLitePlayerDataDatabase(final Class<S> clazz, final SQLColumn[] columns, final String defaultPath, final String defaultTableName, final ConfigurationSection config)
	{
		super(clazz, columns, defaultPath, defaultTableName, config);
	}

	public SQLitePlayerDataDatabase(final Class<S> clazz, final SQLColumn[] columns, final String tableName, final String[] columnNames, final String path, final boolean cached, final boolean doNoUpdate)
	{
		super(clazz, columns, tableName, columnNames, path, cached, doNoUpdate);
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
