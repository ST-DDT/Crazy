package de.st_ddt.crazytimecard.databases;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazytimecard.data.CardData;
import de.st_ddt.crazyutil.databases.FlatDatabase;

public final class CrazyTimeCardCardFlatDatabase extends FlatDatabase<CardData>
{

	public CrazyTimeCardCardFlatDatabase(final JavaPlugin plugin, final ConfigurationSection config)
	{
		super(CardData.class, new String[] { "name", "owner", "user", "duration" }, "cards.db", plugin, config);
	}

	public CrazyTimeCardCardFlatDatabase(final JavaPlugin plugin, final String path)
	{
		super(CardData.class, new String[] { "name", "owner", "user", "duration" }, plugin, path);
	}
}
