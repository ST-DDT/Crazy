package de.st_ddt.crazyonline.databases;

import java.io.File;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyonline.data.OnlinePlayerData;
import de.st_ddt.crazyutil.databases.FlatDatabase;
import de.st_ddt.crazyutil.databases.PlayerDataDatabase;

public class CrazyOnlineFlatDatabase extends FlatDatabase<OnlinePlayerData> implements PlayerDataDatabase<OnlinePlayerData>
{

	public CrazyOnlineFlatDatabase(final String tableName, final ConfigurationSection config, final File file)
	{
		super(OnlinePlayerData.class, tableName, config, new String[] { "name", "firstLogin", "lastLogin", "lastLogout", "onlineTime","ip" }, file);
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
