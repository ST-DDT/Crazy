package de.st_ddt.crazyarena;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.CrazyPlugin;

public abstract class CrazyArenaPlugin extends CrazyPlugin
{

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
