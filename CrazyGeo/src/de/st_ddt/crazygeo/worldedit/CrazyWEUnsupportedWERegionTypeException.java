package de.st_ddt.crazygeo.worldedit;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazygeo.region.RealRoom;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.poly.room.Room;

public class CrazyWEUnsupportedWERegionTypeException extends CrazyException
{

	protected final RealRoom<? extends Room> room;
	private static final long serialVersionUID = -5041184766906291400L;

	public CrazyWEUnsupportedWERegionTypeException(final RealRoom<? extends Room> room)
	{
		super();
		this.room = room;
	}

	public RealRoom<? extends Room> getRoom()
	{
		return room;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".GEO.WORLDEDIT.EXPORT.UNSUPPORTEDREGIONTYPE";
	}

	@Override
	public void print(final CommandSender sender, final String header)
	{
		sender.sendMessage(header + locale.getLocaleMessage(sender, "Head"));
		sender.sendMessage(header + locale.getLocaleMessage(sender, "SUPPORTEDHEAD"));
		sender.sendMessage(header + locale.getLocaleMessage(sender, "SUPPORTED"));
	}
}
