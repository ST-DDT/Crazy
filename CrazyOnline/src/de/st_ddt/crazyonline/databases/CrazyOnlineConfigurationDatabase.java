package de.st_ddt.crazyonline.databases;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyonline.data.OnlinePlayerData;
import de.st_ddt.crazyutil.databases.ConfigurationDatabase;
import de.st_ddt.crazyutil.databases.PlayerDataDatabase;

public class CrazyOnlineConfigurationDatabase extends ConfigurationDatabase<OnlinePlayerData> implements PlayerDataDatabase<OnlinePlayerData>
{

	public CrazyOnlineConfigurationDatabase(final JavaPlugin plugin, final ConfigurationSection config)
	{
		super(OnlinePlayerData.class, new String[] { "name", "firstLogin", "lastLogin", "lastLogout", "onlineTime", "ip" }, "accounts", plugin, config);
	}

	public CrazyOnlineConfigurationDatabase(final JavaPlugin plugin, final String path, final String[] columnNames)
	{
		super(OnlinePlayerData.class, new String[] { "name", "firstLogin", "lastLogin", "lastLogout", "onlineTime", "ip" }, plugin, path, columnNames);
	}

	@Override
	public OnlinePlayerData getEntry(final OfflinePlayer player)
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
