package de.st_ddt.crazytimecard.databases;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazytimecard.data.CardData;
import de.st_ddt.crazyutil.databases.ConfigurationDatabase;

public final class CrazyTimeCardCardConfigurationDatabase extends ConfigurationDatabase<CardData>
{

	public CrazyTimeCardCardConfigurationDatabase(final JavaPlugin plugin, final ConfigurationSection config)
	{
		super(CardData.class, new String[] { "name", "owner", "user", "duration" }, "cards", plugin, config);
	}

	public CrazyTimeCardCardConfigurationDatabase(final JavaPlugin plugin, final String path, final String[] columnNames)
	{
		super(CardData.class, new String[] { "name", "owner", "user", "duration" }, plugin, path, columnNames);
	}
}
