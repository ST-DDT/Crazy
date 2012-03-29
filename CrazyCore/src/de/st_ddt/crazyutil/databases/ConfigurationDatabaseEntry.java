package de.st_ddt.crazyutil.databases;

import org.bukkit.configuration.ConfigurationSection;

public abstract class ConfigurationDatabaseEntry<S extends Saveable> extends DatabaseEntry<S, ConfigurationSection>
{

	public ConfigurationDatabaseEntry(ConfigurationSection rawData)
	{
		super(rawData);
	}

	public ConfigurationDatabaseEntry(S infoObject)
	{
		super(infoObject);
	}
}
