package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ConfigurationSaveable;

public interface Room extends ConfigurationSaveable
{

	public boolean isInsideRel(final double x, final double y, final double z);

	public void expand(final double x, final double y, final double z);

	public void contract(final double x, final double y, final double z);

	public void scale(final double scale);

	public void save(final ConfigurationSection config, final String path, final boolean includeType);

	public void save(final ConfigurationSection config, final String path);

	public Room clone();

	public boolean equals(Room room);
}
