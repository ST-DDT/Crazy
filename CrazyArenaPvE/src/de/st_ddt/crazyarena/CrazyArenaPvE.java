package de.st_ddt.crazyarena;

import de.st_ddt.crazyarena.arenas.Arena;

public class CrazyArenaPvE extends CrazyArenaPlugin
{

	@Override
	public final String getArenaType()
	{
		return "PvE";
	}

	@Override
	public Class<? extends Arena> getArenaClass()
	{
		return de.st_ddt.crazyarena.pve.ArenaPvE.class;
	}
}
