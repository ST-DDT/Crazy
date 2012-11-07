package de.st_ddt.crazytimecard.databases;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazytimecard.data.PlayerTimeData;
import de.st_ddt.crazyutil.databases.FlatPlayerDataDatabase;

public final class CrazyTimeCardFlatDatabase extends FlatPlayerDataDatabase<PlayerTimeData>
{

	public CrazyTimeCardFlatDatabase(final JavaPlugin plugin, final ConfigurationSection config)
	{
		super(PlayerTimeData.class, new String[] { "name", "card", "limit" }, "accounts.db", plugin, config);
	}

	public CrazyTimeCardFlatDatabase(final JavaPlugin plugin, final String path)
	{
		super(PlayerTimeData.class, new String[] { "name", "card", "limit" }, plugin, path);
	}
}
