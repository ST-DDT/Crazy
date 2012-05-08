package de.st_ddt.crazyarena;

import java.util.HashMap;
import java.util.Map;

import de.st_ddt.crazyarena.arenas.Arena;

public class CrazyArenaSpleef extends CrazyArenaPlugin
{

	@Override
	public Map<String, Class<? extends Arena>> getArenaTypes()
	{
		Map<String, Class<? extends Arena>> types = new HashMap<String, Class<? extends Arena>>();
		types.put("Spleef", de.st_ddt.crazyarena.spleef.ArenaSpleef.class);
		return types;
	}
}
