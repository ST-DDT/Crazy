package de.st_ddt.crazyarena;

import de.st_ddt.crazyarena.CrazyArenaPlugin;
import de.st_ddt.crazyarena.arenas.race.RaceArena;

public class CrazyArenaRace extends CrazyArenaPlugin
{

	protected void registerArenaTypes()
	{
		registerArenaType("Race", RaceArena.class);
	}

	public String[] getArenaTypes()
	{
		return new String[] { "Race" };
	}
}
