package de.st_ddt.crazyonline.databases;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyonline.data.OnlinePlayerData;
import de.st_ddt.crazyutil.databases.FlatPlayerDataDatabase;
import de.st_ddt.crazyutil.databases.PlayerDataDatabase;

public class CrazyOnlineFlatDatabase extends FlatPlayerDataDatabase<OnlinePlayerData> implements PlayerDataDatabase<OnlinePlayerData>
{

	public CrazyOnlineFlatDatabase(final JavaPlugin plugin, final ConfigurationSection config)
	{
		super(OnlinePlayerData.class, new String[] { "name", "firstLogin", "lastLogin", "lastLogout", "onlineTime", "onlineTimeMonth", "onlineTimeWeek", "onlineTimeDay", "ip" }, "accounts.db", plugin, config);
	}

	public CrazyOnlineFlatDatabase(final JavaPlugin plugin, final String path)
	{
		super(OnlinePlayerData.class, new String[] { "name", "firstLogin", "lastLogin", "lastLogout", "onlineTime", "onlineTimeMonth", "onlineTimeWeek", "onlineTimeDay", "ip" }, plugin, path);
	}
}
