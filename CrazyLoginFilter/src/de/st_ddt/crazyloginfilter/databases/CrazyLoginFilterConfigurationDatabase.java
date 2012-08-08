package de.st_ddt.crazyloginfilter.databases;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyloginfilter.data.PlayerAccessFilter;
import de.st_ddt.crazyutil.databases.ConfigurationDatabase;
import de.st_ddt.crazyutil.databases.PlayerDataDatabase;

public class CrazyLoginFilterConfigurationDatabase extends ConfigurationDatabase<PlayerAccessFilter> implements PlayerDataDatabase<PlayerAccessFilter>
{

	public CrazyLoginFilterConfigurationDatabase(final String tableName, final ConfigurationSection config, JavaPlugin plugin)
	{
		super(PlayerAccessFilter.class, tableName, config, new String[] { "name", "checkIPs", "whitelistIPs", "IPs", "checkConnections", "whitelistConnections", "connections" }, plugin);
	}

	@Override
	public PlayerAccessFilter getEntry(final OfflinePlayer player)
	{
		return getEntry(player.getName());
	}

	@Override
	public boolean hasEntry(final OfflinePlayer player)
	{
		return hasEntry(player.getName());
	}

	@Override
	public boolean deleteEntry(final OfflinePlayer player)
	{
		return deleteEntry(player.getName());
	}
}
