package de.st_ddt.crazyarena;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.CrazyPlugin;

public abstract class CrazyArenaPlugin extends CrazyPlugin
{

	private final static Map<Class<? extends CrazyArenaPlugin>, CrazyArenaPlugin> arenaPlugins = new LinkedHashMap<Class<? extends CrazyArenaPlugin>, CrazyArenaPlugin>();
	private final static Map<String, Class<? extends Arena<?>>> arenaTypes = new TreeMap<String, Class<? extends Arena<?>>>();

	public static Collection<CrazyArenaPlugin> getCrazyArenaPlugins()
	{
		return arenaPlugins.values();
	}

	public static Map<String, Class<? extends Arena<?>>> getRegisteredArenaTypes()
	{
		return arenaTypes;
	}

	@Override
	public void onLoad()
	{
		arenaPlugins.put(this.getClass(), this);
		registerArenaTypes();
		super.onLoad();
	}

	protected abstract void registerArenaTypes();

	public abstract String[] getArenaTypes();

	protected final void registerArenaType(final String mainType, final Class<? extends Arena<?>> clazz, final String... aliases)
	{
		arenaTypes.put(mainType.toLowerCase(), clazz);
		for (final String alias : aliases)
			arenaTypes.put(alias.toLowerCase(), clazz);
	}

	public Map<String, Set<Arena<?>>> getArenas()
	{
		final Map<String, Set<Arena<?>>> arenas = new HashMap<String, Set<Arena<?>>>();
		final CrazyArena arenaPlugin = getArenaMainPlugin();
		for (final String type : getArenaTypes())
			arenas.put(type, arenaPlugin.getArenasByType(type));
		return arenas;
	}

	public final CrazyArena getArenaMainPlugin()
	{
		return CrazyArena.getPlugin();
	}
}
