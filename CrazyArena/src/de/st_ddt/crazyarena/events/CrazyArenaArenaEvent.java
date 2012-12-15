package de.st_ddt.crazyarena.events;

import org.bukkit.event.HandlerList;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;

public class CrazyArenaArenaEvent<S extends Arena<?>> extends CrazyArenaEvent
{

	private static final HandlerList handlers = new HandlerList();
	protected final S arena;

	public CrazyArenaArenaEvent(final CrazyArena plugin, final S arena)
	{
		super(plugin);
		this.arena = arena;
	}

	public final S getArena()
	{
		return arena;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
