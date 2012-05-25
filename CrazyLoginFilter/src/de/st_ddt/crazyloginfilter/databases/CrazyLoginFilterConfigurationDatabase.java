package de.st_ddt.crazyloginfilter.databases;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyloginfilter.PlayerAccessFilter;
import de.st_ddt.crazyutil.databases.ConfigurationDatabase;

public class CrazyLoginFilterConfigurationDatabase extends ConfigurationDatabase<PlayerAccessFilter>
{

	public CrazyLoginFilterConfigurationDatabase(ConfigurationSection config, String table, String colName, String colCheckIPs, String colCheckConnections, String colWhitelistIPs, String colWhitelistConnections, String colIPs, String colConnections)
	{
		super(PlayerAccessFilter.class, config, table, new String[] { colName, colCheckIPs, colCheckConnections, colWhitelistIPs, colWhitelistConnections, colIPs, colConnections });
	}
}
