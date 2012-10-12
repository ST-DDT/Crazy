package de.st_ddt.crazyutil;

import org.bukkit.configuration.ConfigurationSection;

public interface ConfigurationSaveable
{

	// public ConfigurationSaveable(ConfigurationSection config);
	/**
	 * Saves this object to config.
	 * 
	 * @param config
	 *            The config this object should be saved too.
	 * @param path
	 *            The path within this configto save this object too (Should end with "." in most cases)
	 */
	public void save(ConfigurationSection config, String path);
}
