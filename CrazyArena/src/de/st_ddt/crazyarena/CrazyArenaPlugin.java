package de.st_ddt.crazyarena;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.CrazyPlugin;

public abstract class CrazyArenaPlugin extends CrazyPlugin
{

	private final static Map<Class<? extends CrazyArenaPlugin>, CrazyArenaPlugin> arenaPlugins = new LinkedHashMap<Class<? extends CrazyArenaPlugin>, CrazyArenaPlugin>();
	protected final CrazyArena arenaPlugin = CrazyArena.getPlugin();

	public static Collection<CrazyArenaPlugin> getCrazyArenaPlugins()
	{
		return arenaPlugins.values();
	}

	@Override
	public void onLoad()
	{
		arenaPlugins.put(this.getClass(), this);
		registerArenaTypes();
		super.onEnable();
	}

	protected abstract void registerArenaTypes();

	public abstract String[] getArenaTypes();

	protected void registerArenaType(final String mainType, final Class<? extends Arena<?>> clazz, final String... aliases)
	{
		arenaPlugin.registerArenaType(mainType, clazz, aliases);
	}

	public Map<String, Set<Arena<?>>> getArenas()
	{
		final Map<String, Set<Arena<?>>> arenas = new HashMap<String, Set<Arena<?>>>();
		if (arenaPlugin == null)
			return arenas;
		for (final String type : getArenaTypes())
			arenas.put(type, arenaPlugin.getArenasByType(type));
		return arenas;
	}
}
