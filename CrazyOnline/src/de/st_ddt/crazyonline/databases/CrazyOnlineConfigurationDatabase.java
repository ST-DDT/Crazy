package de.st_ddt.crazyonline.databases;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyonline.OnlinePlayerData;
import de.st_ddt.crazyutil.databases.ConfigurationDatabase;

public class CrazyOnlineConfigurationDatabase extends ConfigurationDatabase<OnlinePlayerData>
{

	public CrazyOnlineConfigurationDatabase(final ConfigurationSection config, final String table, final String colName, final String colFirstLogin, final String colLastLogin, final String colLastLogout, final String colOnlineTime)
	{
		super(OnlinePlayerData.class, config, table, new String[] { colName, colFirstLogin, colLastLogin, colLastLogout, colOnlineTime });
	}
}
