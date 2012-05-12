package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.poly.region.FlatRegion;

public class PrismRoom
{

	protected FlatRegion region;
	protected double height;

	public PrismRoom(final FlatRegion region, final double height)
	{
		super();
		this.region = region;
		this.height = height;
	}

	public PrismRoom(final ConfigurationSection config)
	{
		super();
		this.region = FlatRegion.load(config.getConfigurationSection("region"));
		this.height = config.getDouble("height");
	}

	public boolean isInsideRel(double x, double y, double z)
	{
		return region.isInsideRel(x, z) && y < height;
	}

	public void expand(double x, double y, double z)
	{
		region.expand(x, z);
		height += Math.abs(y);
	}

	public void contract(double x, double y, double z)
	{
		region.contract(x, z);
	}

	public void scale(double scale)
	{
		region.scale(scale);
	}

	public void save(final ConfigurationSection config, final String path, final boolean includeType)
	{
		if (includeType)
			config.set(path + "type", getClass().getName());
		save(config, path);
	}

	public void save(ConfigurationSection config, String path)
	{
		region.save(config, path + "region");
		config.set(path + "height", height);
	}

	public PrismRoom clone()
	{
		return new PrismRoom(region.clone(), height);
	}
}
