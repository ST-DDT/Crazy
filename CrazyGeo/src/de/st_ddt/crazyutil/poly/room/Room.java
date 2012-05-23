package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ConfigurationSaveable;

public interface Room extends ConfigurationSaveable
{

	public boolean isInsideRel(final double x, final double y, final double z);

	public void scale(final double scale);

	public void scale(final double scaleX, final double scaleY, final double scaleZ);

	public void save(final ConfigurationSection config, final String path, final boolean includeType);

	@Override
	public void save(final ConfigurationSection config, final String path);

	public Room clone();

	public boolean equals(Room room);
}
