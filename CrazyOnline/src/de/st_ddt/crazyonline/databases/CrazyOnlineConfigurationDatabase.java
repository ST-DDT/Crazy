package de.st_ddt.crazyonline.databases;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyonline.OnlinePlayerData;
import de.st_ddt.crazyutil.databases.ConfigurationDatabase;

public class CrazyOnlineConfigurationDatabase extends ConfigurationDatabase<OnlinePlayerData>
{

	public CrazyOnlineConfigurationDatabase(ConfigurationSection config, String table, String colName, String colFirstLogin, String colLastLogin, String colLastLogout, String colOnlineTime)
	{
		super(OnlinePlayerData.class, config, table, new String[] { colName, colFirstLogin, colLastLogin, colLastLogout, colOnlineTime });
	}
}
