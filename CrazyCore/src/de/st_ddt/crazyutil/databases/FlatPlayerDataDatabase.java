package de.st_ddt.crazyutil.databases;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class FlatPlayerDataDatabase<S extends FlatPlayerDataDatabaseEntry> extends FlatDatabase<S> implements PlayerDataDatabase<S>
{

	public FlatPlayerDataDatabase(final Class<S> clazz, final String[] defaultColumnNames, final JavaPlugin plugin, final String path)
	{
		super(clazz, defaultColumnNames, plugin, path);
	}

	public FlatPlayerDataDatabase(final Class<S> clazz, final String[] defaultColumnNames, final String defaultPath, final JavaPlugin plugin, final ConfigurationSection config)
	{
		super(clazz, defaultColumnNames, defaultPath, plugin, config);
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
