package de.st_ddt.crazyarena.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;

public class CrazyArenaArenaPlayerJoinEvent extends CrazyArenaArenaPlayerEvent<Arena<?>>
{

	private static final HandlerList handlers = new HandlerList();

	public CrazyArenaArenaPlayerJoinEvent(final CrazyArena plugin, final Arena<?> arena, Player player)
	{
		super(plugin, arena, player);
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
