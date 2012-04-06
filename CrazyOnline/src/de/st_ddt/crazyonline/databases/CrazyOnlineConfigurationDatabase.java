package de.st_ddt.crazyonline.databases;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyonline.OnlinePlayerData;
import de.st_ddt.crazyutil.databases.ConfigurationDatabase;

public class CrazyOnlineConfigurationDatabase extends ConfigurationDatabase<OnlinePlayerData>
{

	public CrazyOnlineConfigurationDatabase(ConfigurationSection config, String table)
	{
		super(OnlinePlayerData.class, config, table);
	}
}
