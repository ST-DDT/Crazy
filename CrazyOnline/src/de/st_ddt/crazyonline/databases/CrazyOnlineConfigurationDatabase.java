package de.st_ddt.crazyonline.databases;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyonline.data.OnlinePlayerData;
import de.st_ddt.crazyutil.databases.ConfigurationPlayerDataDatabase;

public class CrazyOnlineConfigurationDatabase extends ConfigurationPlayerDataDatabase<OnlinePlayerData>
{

	public CrazyOnlineConfigurationDatabase(final JavaPlugin plugin, final ConfigurationSection config)
	{
		super(OnlinePlayerData.class, new String[] { "name", "firstLogin", "lastLogin", "lastLogout", "onlineTime", "onlineTimeMonth", "onlineTimeWeek", "onlineTimeDay", "ip" }, "accounts", plugin, config);
	}

	public CrazyOnlineConfigurationDatabase(final JavaPlugin plugin, final String path, final String[] columnNames)
	{
		super(OnlinePlayerData.class, new String[] { "name", "firstLogin", "lastLogin", "lastLogout", "onlineTime", "onlineTimeMonth", "onlineTimeWeek", "onlineTimeDay", "ip" }, plugin, path, columnNames);
	}
}
