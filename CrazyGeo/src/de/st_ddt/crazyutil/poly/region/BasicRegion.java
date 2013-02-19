package de.st_ddt.crazyutil.poly.region;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public abstract class BasicRegion implements Region
{

	public static Region load(final ConfigurationSection config)
	{
		return ObjectSaveLoadHelper.load(config, Region.class, new Class<?>[] { ConfigurationSection.class }, new Object[] { config });
	}

	public BasicRegion()
	{
		super();
	}

	public BasicRegion(final ConfigurationSection config)
	{
		super();
	}

	@Override
	public abstract boolean isInsideRel(double x, double z);

	@Override
	public abstract void expand(double x, double z);

	@Override
	public abstract void contract(double x, double z);

	@Override
	public abstract void scale(double scale);

	@Override
	public abstract void scale(double scaleX, double scaleZ);

	@Override
	public abstract double getArea();

	@Override
	public final void save(final ConfigurationSection config, final String path, final boolean includeType)
	{
		if (includeType)
			config.set(path + "type", getClass().getName());
		save(config, path);
	}

	@Override
	public abstract void save(ConfigurationSection config, String path);

	@Override
	public abstract BasicRegion clone();

	@Override
	public abstract boolean equals(Region region);

	@Override
	public String toString()
	{
		return getClass().getSimpleName();
	}
}
