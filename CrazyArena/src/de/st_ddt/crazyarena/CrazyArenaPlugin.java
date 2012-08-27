package de.st_ddt.crazyarena;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.CrazyPlugin;

public abstract class CrazyArenaPlugin extends CrazyPlugin
{

	private static final LinkedHashMap<Class<? extends CrazyArenaPlugin>, CrazyArenaPlugin> arenaPlugins = new LinkedHashMap<Class<? extends CrazyArenaPlugin>, CrazyArenaPlugin>();

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

	protected static void registerArenaType(final String mainType, final Class<? extends Arena<?>> clazz, final String... aliases)
	{
		CrazyArena.registerArenaType(mainType, clazz, aliases);
	}

	protected static CrazyArena getArenaPlugin()
	{
		return CrazyArena.getPlugin();
	}

	public HashMap<String, Set<Arena<?>>> getArenas()
	{
		final CrazyArena plugin = getArenaPlugin();
		final HashMap<String, Set<Arena<?>>> arenas = new HashMap<String, Set<Arena<?>>>();
		if (plugin == null)
			return arenas;
		for (final String type : getArenaTypes())
			arenas.put(type, plugin.getArenaByType(type));
		return arenas;
	}
}
