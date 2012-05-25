package de.st_ddt.crazyarena;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.CrazyPlugin;

public abstract class CrazyArenaPlugin extends CrazyPlugin
{

	private static final HashMap<Class<? extends CrazyPlugin>, CrazyPlugin> arenaPlugins = new HashMap<Class<? extends CrazyPlugin>, CrazyPlugin>();

	public static Collection<CrazyPlugin> getCrazyArenaPlugins()
	{
		return arenaPlugins.values();
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
