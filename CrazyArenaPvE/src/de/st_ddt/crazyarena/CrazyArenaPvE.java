package de.st_ddt.crazyarena;

import de.st_ddt.crazyarena.arenas.pve.PvEArena;
import de.st_ddt.crazyutil.source.LocalizedVariable;

@LocalizedVariable(variables="CRAZYPLUGIN",values="CRAZYARENAPVE")
public class CrazyArenaPvE extends CrazyArenaPlugin
{

	private static CrazyArenaPvE plugin;

	@Override
	public void onLoad()
	{
		super.onLoad();
		plugin = this;
	}

	public static CrazyArenaPvE getPlugin()
	{
		return plugin;
	}

	@Override
	protected void registerArenaTypes()
	{
		registerArenaType("PvE", PvEArena.class, "Monster");
	}

	@Override
	public String[] getArenaTypes()
	{
		return new String[] { "PvE", "Monster" };
	}
}
