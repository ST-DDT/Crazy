package de.st_ddt.crazygeo;

import java.util.HashMap;

import org.bukkit.entity.Player;

import de.st_ddt.crazygeo.region.RealRoom;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.poly.room.Room;

public class CrazyGeo
{

	protected final HashMap<Player, RealRoom<Room>> geos = new HashMap<Player, RealRoom<Room>>();

	public void command(final Player sender, final String commandLabel, final String[] args) throws CrazyException
	{
	}

	public void commandInfo(Player sender, String[] newArgs)
	{
		// EDIT Auto-generated method stub
	}
}
