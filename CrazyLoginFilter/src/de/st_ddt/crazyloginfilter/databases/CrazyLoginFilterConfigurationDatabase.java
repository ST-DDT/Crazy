package de.st_ddt.crazyloginfilter.databases;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyloginfilter.PlayerAccessFilter;
import de.st_ddt.crazyutil.databases.ConfigurationDatabase;

public class CrazyLoginFilterConfigurationDatabase extends ConfigurationDatabase<PlayerAccessFilter>
{

	public CrazyLoginFilterConfigurationDatabase(ConfigurationSection config, String table, String colName, String colCheckIPs, String colWhitelistIPs, String colIPs, String colCheckConnections, String colWhitelistConnections, String colConnections)
	{
		super(PlayerAccessFilter.class, config, table, new String[] { colName, colCheckIPs, colWhitelistIPs, colIPs, colCheckConnections, colWhitelistConnections, colConnections });
	}
}
