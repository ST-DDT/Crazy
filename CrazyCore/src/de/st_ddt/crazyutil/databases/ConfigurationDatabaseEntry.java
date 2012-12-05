package de.st_ddt.crazyutil.databases;

import org.bukkit.configuration.ConfigurationSection;

public interface ConfigurationDatabaseEntry extends DatabaseEntry
{

	// public ConfigurationDatabaseEntry(ConfigurationSection rawData, String[] columnNames);
	public void saveToConfigDatabase(ConfigurationSection config, String table, String[] columnNames);
}
