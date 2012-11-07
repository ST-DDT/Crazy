package de.st_ddt.crazytimecard.databases;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazytimecard.data.PlayerTimeData;
import de.st_ddt.crazyutil.databases.ConfigurationPlayerDataDatabase;

public final class CrazyTimeCardConfigurationDatabase extends ConfigurationPlayerDataDatabase<PlayerTimeData>
{

	public CrazyTimeCardConfigurationDatabase(final JavaPlugin plugin, final ConfigurationSection config)
	{
		super(PlayerTimeData.class, new String[] { "name", "card", "limit" }, "accounts", plugin, config);
	}

	public CrazyTimeCardConfigurationDatabase(final JavaPlugin plugin, final String path, final String[] columnNames)
	{
		super(PlayerTimeData.class, new String[] { "name", "card", "limit" }, plugin, path, columnNames);
	}
}
