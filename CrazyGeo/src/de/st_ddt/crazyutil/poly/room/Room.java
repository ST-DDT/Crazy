package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ConfigurationSaveable;

public interface Room extends ConfigurationSaveable
{

	public boolean isInsideRel(double x, double y, double z);

	public void scale(double scale);

	public void scale(double scaleX, double scaleY, double scaleZ);

	public void save(ConfigurationSection config, String path, boolean includeType);

	@Override
	public void save(ConfigurationSection config, String path);

	public Room clone();

	public boolean equals(Room room);

	@Override
	public String toString();
}
