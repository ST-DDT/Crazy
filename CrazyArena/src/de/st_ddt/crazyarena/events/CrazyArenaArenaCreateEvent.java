package de.st_ddt.crazyarena.events;

import org.bukkit.event.HandlerList;

import de.st_ddt.crazyarena.arenas.Arena;

public class CrazyArenaArenaCreateEvent extends CrazyArenaArenaEvent<Arena<?>>
{

	private static final HandlerList handlers = new HandlerList();
	private final boolean loaded;

	public CrazyArenaArenaCreateEvent(final Arena<?> arena, final boolean loaded)
	{
		super(arena);
		this.loaded = loaded;
	}

	public boolean isLoaded()
	{
		return loaded;
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
