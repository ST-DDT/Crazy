package de.st_ddt.crazyonline.databases;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyonline.OnlinePlayerData;
import de.st_ddt.crazyutil.databases.ConfigurationDatabase;
import de.st_ddt.crazyutil.databases.ConfigurationDatabaseEntry;

public class CrazyOnlineConfigurationDatabase extends ConfigurationDatabase<OnlinePlayerData, ConfigurationDatabaseEntry<OnlinePlayerData>>
{

	public CrazyOnlineConfigurationDatabase(String tableName, ConfigurationSection config)
	{
		super(config, "players", OnlinePlayerData.class);
	}
}
