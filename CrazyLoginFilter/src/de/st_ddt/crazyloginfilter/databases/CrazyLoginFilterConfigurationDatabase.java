package de.st_ddt.crazyloginfilter.databases;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyloginfilter.data.PlayerAccessFilter;
import de.st_ddt.crazyutil.databases.ConfigurationPlayerDataDatabase;

public class CrazyLoginFilterConfigurationDatabase extends ConfigurationPlayerDataDatabase<PlayerAccessFilter>
{

	public CrazyLoginFilterConfigurationDatabase(final JavaPlugin plugin, final ConfigurationSection config)
	{
		super(PlayerAccessFilter.class, new String[] { "name", "checkIPs", "whitelistIPs", "IPs", "checkConnections", "whitelistConnections", "connections" }, "accounts", plugin, config);
	}

	public CrazyLoginFilterConfigurationDatabase(final JavaPlugin plugin, final String path, final String[] columnNames)
	{
		super(PlayerAccessFilter.class, new String[] { "name", "checkIPs", "whitelistIPs", "IPs", "checkConnections", "whitelistConnections", "connections" }, plugin, path, columnNames);
	}
}
