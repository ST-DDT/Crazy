package de.st_ddt.crazygeo;

import java.util.HashMap;

import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.geo.Geo;

public class CrazyGeo
{

	protected final HashMap<Player, Geo> geos = new HashMap<Player, Geo>();

	public void command(final Player sender, final String commandLabel, final String[] args) throws CrazyException
	{
	}

	public void commandInfo(Player sender, String[] newArgs)
	{
		// EDIT Auto-generated method stub
	}
}
