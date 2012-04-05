package de.st_ddt.crazyutil.databases;

import org.bukkit.configuration.ConfigurationSection;

public abstract class ConfigurationDatabaseEntry<S extends DatabaseSaveable> extends DatabaseEntry<S, ConfigurationSection>
{

	protected final String path;

	public ConfigurationDatabaseEntry(String path)
	{
		super();
		this.path = path;
	}

	@Override
	public abstract S load(ConfigurationSection rawData);

	@Override
	public abstract void save(S data, ConfigurationSection saveTo);
}
