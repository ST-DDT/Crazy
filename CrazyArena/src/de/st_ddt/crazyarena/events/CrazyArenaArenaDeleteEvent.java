package de.st_ddt.crazyarena.events;

import org.bukkit.event.HandlerList;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;

public class CrazyArenaArenaDeleteEvent extends CrazyArenaArenaEvent<Arena<?>>
{

	private static final HandlerList handlers = new HandlerList();

	public CrazyArenaArenaDeleteEvent(final CrazyArena plugin, final Arena<?> arena)
	{
		super(plugin, arena);
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
