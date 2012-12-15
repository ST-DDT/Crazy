package de.st_ddt.crazyarena.events;

import org.bukkit.event.HandlerList;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyplugin.events.CrazyEvent;

public class CrazyArenaEvent extends CrazyEvent<CrazyArena>
{

	private static final HandlerList handlers = new HandlerList();

	public CrazyArenaEvent(final CrazyArena plugin)
	{
		super(plugin);
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
