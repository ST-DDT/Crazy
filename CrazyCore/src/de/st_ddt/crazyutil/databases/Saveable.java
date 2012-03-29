package de.st_ddt.crazyutil.databases;

import org.bukkit.configuration.ConfigurationSection;

public interface Saveable
{

	public void save(ConfigurationSection config, String path);

	public String getName();
}
