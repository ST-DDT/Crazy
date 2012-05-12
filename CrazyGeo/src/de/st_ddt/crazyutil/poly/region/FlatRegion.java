package de.st_ddt.crazyutil.poly.region;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public abstract class FlatRegion implements ConfigurationSaveable
{

	public static FlatRegion load(final ConfigurationSection config)
	{
		return ObjectSaveLoadHelper.load(config, FlatRegion.class, new Class<?>[] { ConfigurationSection.class }, new Object[] { config });
	}

	public FlatRegion()
	{
		super();
	}

	public FlatRegion(final ConfigurationSection config)
	{
		super();
	}

	public abstract boolean isInsideRel(double x, double z);

	public abstract void expand(double x, double z);

	public abstract void contract(double x, double z);

	public abstract void scale(double scale);

	public abstract double getArea();

	public final void save(final ConfigurationSection config, final String path, final boolean includeType)
	{
		if (includeType)
			config.set(path + "type", getClass().getName());
		save(config, path);
	}

	@Override
	public abstract void save(ConfigurationSection config, String path);

	@Override
	public abstract FlatRegion clone();

	public abstract boolean equals(FlatRegion region);
}
