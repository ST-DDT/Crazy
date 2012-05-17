package de.st_ddt.crazygeo.worldedit;

import org.bukkit.command.CommandSender;

import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.regions.Region;

import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class CrazyWEUnsupportedGeoRegionTypeException extends CrazyException
{

	private static final long serialVersionUID = -56978119580962345L;
	protected final LocalWorld world;
	protected final Region region;

	public CrazyWEUnsupportedGeoRegionTypeException(final LocalWorld world, final Region region)
	{
		super();
		this.world = world;
		this.region = region;
	}

	public LocalWorld getWorld()
	{
		return world;
	}

	public Region getRegion()
	{
		return region;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".GEO.WORLDEDIT.IMPORT.UNSUPPORTEDREGIONTYPE";
	}

	@Override
	public void print(final CommandSender sender, final String header)
	{
		sender.sendMessage(header + locale.getLocaleMessage(sender, "Head"));
		sender.sendMessage(header + locale.getLocaleMessage(sender, "SUPPORTEDHEAD"));
		sender.sendMessage(header + locale.getLocaleMessage(sender, "SUPPORTED"));
	}
}
