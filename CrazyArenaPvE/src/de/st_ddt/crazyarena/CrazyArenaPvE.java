package de.st_ddt.crazyarena;

import java.util.HashMap;
import java.util.Map;

import de.st_ddt.crazyarena.arenas.Arena;

public class CrazyArenaPvE extends CrazyArenaPlugin
{

	@Override
	public Map<String, Class<? extends Arena>> getArenaTypes()
	{
		Map<String, Class<? extends Arena>> types = new HashMap<>();
		types.put("PvE", de.st_ddt.crazyarena.pve.ArenaPvE.class);
		types.put("Monster", de.st_ddt.crazyarena.pve.ArenaPvE.class);
		types.put("Monsterarena", de.st_ddt.crazyarena.pve.ArenaPvE.class);
		return types;
	}
}
