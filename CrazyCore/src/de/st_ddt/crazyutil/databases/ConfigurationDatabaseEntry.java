package de.st_ddt.crazyutil.databases;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ConfigurationSaveable;

public interface ConfigurationDatabaseEntry extends DatabaseEntry, ConfigurationSaveable
{

	// public ConfigurationDatabaseEntry(ConfigurationSection rawData);
	public abstract void save(ConfigurationSection config, String table);
}
