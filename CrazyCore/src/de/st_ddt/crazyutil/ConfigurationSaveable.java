package de.st_ddt.crazyutil;

import org.bukkit.configuration.ConfigurationSection;

public interface ConfigurationSaveable
{

	// public ConfigurationSaveable(ConfigurationSection config);
	public void save(ConfigurationSection config, String path);
}
