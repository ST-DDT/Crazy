package de.st_ddt.crazyonline.databases;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyonline.data.OnlinePlayerData;
import de.st_ddt.crazyutil.databases.ConfigurationPlayerDataDatabase;
import de.st_ddt.crazyutil.databases.PlayerDataDatabase;

public class CrazyOnlineConfigurationDatabase extends ConfigurationPlayerDataDatabase<OnlinePlayerData> implements PlayerDataDatabase<OnlinePlayerData>
{

	public CrazyOnlineConfigurationDatabase(final JavaPlugin plugin, final ConfigurationSection config)
	{
		super(OnlinePlayerData.class, new String[] { "name", "firstLogin", "lastLogin", "lastLogout", "onlineTime", "ip" }, "accounts", plugin, config);
	}

	public CrazyOnlineConfigurationDatabase(final JavaPlugin plugin, final String path, final String[] columnNames)
	{
		super(OnlinePlayerData.class, new String[] { "name", "firstLogin", "lastLogin", "lastLogout", "onlineTime", "ip" }, plugin, path, columnNames);
	}
}
