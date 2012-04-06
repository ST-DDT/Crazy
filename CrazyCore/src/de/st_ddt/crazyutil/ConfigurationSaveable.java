package de.st_ddt.crazyutil;

import org.bukkit.configuration.ConfigurationSection;

public interface ConfigurationSaveable
{

	public void save(ConfigurationSection config, String path);
}
