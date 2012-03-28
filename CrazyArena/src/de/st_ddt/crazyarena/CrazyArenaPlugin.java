package de.st_ddt.crazyarena;

import java.util.ArrayList;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyutil.PairList;

public abstract class CrazyArenaPlugin extends CrazyPlugin
{

	private static final PairList<Class<? extends CrazyPlugin>, CrazyPlugin> arenaPlugins = new PairList<Class<? extends CrazyPlugin>, CrazyPlugin>();

	public static ArrayList<CrazyPlugin> getCrazyArenaPlugins()
	{
		return arenaPlugins.getData2List();
	}

	@Override
	public void onLoad()
	{
		CrazyArena.registerArenaType(getArenaType(), getArenaClass());
		super.onLoad();
	}

	/**
	 * Gibt den Arenatyp zurück
	 * 
	 * @return Arenatypen
	 */
	public abstract String getArenaType();

	/**
	 * Gibt die Arenaklasse zurück
	 * 
	 * @return Arenaklassen
	 */
	public abstract Class<? extends Arena> getArenaClass();
}
