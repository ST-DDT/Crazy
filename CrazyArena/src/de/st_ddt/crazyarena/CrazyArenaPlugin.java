package de.st_ddt.crazyarena;

import java.util.ArrayList;
import java.util.Map;

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
		CrazyArena.registerArenaTypes(getArenaTypes());
		super.onLoad();
	}

	@Override
	public void onDisable()
	{
		CrazyArena.unregisterArenaType(getArenaTypes().values());
		super.onDisable();
	}

	/**
	 * Gibt die Arenatyp zurück
	 * 
	 * @return Arenatypen
	 */
	public abstract Map<String, Class<? extends Arena>> getArenaTypes();
}
