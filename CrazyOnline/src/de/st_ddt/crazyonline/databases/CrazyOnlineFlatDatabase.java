package de.st_ddt.crazyonline.databases;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyonline.data.OnlinePlayerData;
import de.st_ddt.crazyutil.databases.FlatDatabase;
import de.st_ddt.crazyutil.databases.PlayerDataDatabase;

public class CrazyOnlineFlatDatabase extends FlatDatabase<OnlinePlayerData> implements PlayerDataDatabase<OnlinePlayerData>
{

	public CrazyOnlineFlatDatabase(final JavaPlugin plugin, final ConfigurationSection config)
	{
		super(OnlinePlayerData.class, new String[] { "name", "firstLogin", "lastLogin", "lastLogout", "onlineTime", "ip" }, "accounts.db", plugin, config);
	}

	public CrazyOnlineFlatDatabase(final JavaPlugin plugin, final String path)
	{
		super(OnlinePlayerData.class, new String[] { "name", "firstLogin", "lastLogin", "lastLogout", "onlineTime", "ip" }, plugin, path);
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
