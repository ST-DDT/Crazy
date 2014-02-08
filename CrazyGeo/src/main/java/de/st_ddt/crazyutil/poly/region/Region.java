package de.st_ddt.crazyutil.poly.region;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ConfigurationSaveable;

public interface Region extends ConfigurationSaveable
{

	public boolean isInsideRel(double x, double z);

	public void expand(double x, double z);

	public void contract(double x, double z);

	public void scale(double scale);

	public void scale(double scaleX, double scaleZ);

	public double getArea();

	public void save(ConfigurationSection config, String path, boolean includeType);

	@Override
	public void save(ConfigurationSection config, String path);

	public Region clone();

	public boolean equals(Region region);

	@Override
	public String toString();
}
