package de.st_ddt.crazychats.databases;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyutil.databases.ConfigurationPlayerDataDatabase;

public class CrazyChatsConfigurationDatabase extends ConfigurationPlayerDataDatabase<ChatPlayerData>
{

	public CrazyChatsConfigurationDatabase(final JavaPlugin plugin, final ConfigurationSection config)
	{
		super(ChatPlayerData.class, new String[] { "name", "muted", "displayName", "listName", "headName", "silenced" }, "accounts", plugin, config);
	}

	public CrazyChatsConfigurationDatabase(final JavaPlugin plugin, final String path, final String[] columnNames)
	{
		super(ChatPlayerData.class, new String[] { "name", "muted", "displayName", "listName", "headName", "silenced" }, plugin, path, columnNames);
	}
}
