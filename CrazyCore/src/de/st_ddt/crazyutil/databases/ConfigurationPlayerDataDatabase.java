package de.st_ddt.crazyutil.databases;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationPlayerDataDatabase<S extends ConfigurationPlayerDataDatabaseEntry> extends ConfigurationDatabase<S> implements PlayerDataDatabase<S>
{

	public ConfigurationPlayerDataDatabase(final Class<S> clazz, final String[] defaultColumnNames, final String defaultPath, final JavaPlugin plugin, final ConfigurationSection config)
	{
		super(clazz, defaultColumnNames, defaultPath, plugin, config);
	}

	public ConfigurationPlayerDataDatabase(final Class<S> clazz, final String[] defaultColumnNames, final JavaPlugin plugin, final String path, final String[] columnNames)
	{
		super(clazz, defaultColumnNames, plugin, path, columnNames);
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
