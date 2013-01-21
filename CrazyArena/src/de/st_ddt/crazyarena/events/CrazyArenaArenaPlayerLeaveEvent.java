package de.st_ddt.crazyarena.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import de.st_ddt.crazyarena.arenas.Arena;

public class CrazyArenaArenaPlayerLeaveEvent extends CrazyArenaArenaPlayerEvent<Arena<?>>
{

	private static final HandlerList handlers = new HandlerList();

	public CrazyArenaArenaPlayerLeaveEvent(final Arena<?> arena, final Player player)
	{
		super(arena, player);
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
