package de.st_ddt.crazygeo.worldedit;

import org.bukkit.command.CommandSender;

import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.regions.Region;

import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class CrazyWEUnsupportedRegionTypeException extends CrazyException
{

	private static final long serialVersionUID = -56978119580962345L;
	protected final LocalWorld world;
	protected final Region region;

	public CrazyWEUnsupportedRegionTypeException(LocalWorld world, Region region)
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
		return super.getLangPath() + ".GEO.WORLDEDIT.UNSUPPORTEDREGIONTYPE";
	}

	@Override
	public void print(CommandSender sender, String header)
	{
		sender.sendMessage(header + locale.getLocaleMessage(sender, "Head"));
	}
}
