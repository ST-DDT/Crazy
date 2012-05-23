package de.st_ddt.crazyutil.poly.region;

import org.bukkit.configuration.ConfigurationSection;

public interface Region
{

	public abstract boolean isInsideRel(double x, double z);

	public abstract void expand(double x, double z);

	public abstract void contract(double x, double z);

	public abstract void scale(double scale);

	public abstract void scale(double scaleX, double scaleZ);

	public abstract double getArea();

	public abstract void save(final ConfigurationSection config, final String path, final boolean includeType);

	public abstract void save(ConfigurationSection config, String path);

	public abstract Region clone();

	public abstract boolean equals(Region region);
}
