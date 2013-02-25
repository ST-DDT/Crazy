package de.st_ddt.crazyarena;

import de.st_ddt.crazyarena.arenas.race.RaceArena;
import de.st_ddt.crazyutil.source.LocalizedVariable;

@LocalizedVariable(variables="CRAZYPLUGIN",values="CRAZYARENARACE")
public class CrazyArenaRace extends CrazyArenaPlugin
{

	private static CrazyArenaRace plugin;

	@Override
	public void onLoad()
	{
		super.onLoad();
		plugin = this;
	}

	public static CrazyArenaRace getPlugin()
	{
		return plugin;
	}

	@Override
	protected void registerArenaTypes()
	{
		registerArenaType("Race", RaceArena.class);
	}

	@Override
	public String[] getArenaTypes()
	{
		return new String[] { "Race" };
	}
}
