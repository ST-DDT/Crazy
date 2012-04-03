package de.st_ddt.crazyutil.databases;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.Named;

public interface Saveable extends Named
{

	public void save(ConfigurationSection config, String path);
}
