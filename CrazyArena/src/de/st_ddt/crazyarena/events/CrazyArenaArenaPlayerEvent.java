package de.st_ddt.crazyarena.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;

public class CrazyArenaArenaPlayerEvent<S extends Arena<?>> extends CrazyArenaArenaEvent<S>
{

	private static final HandlerList handlers = new HandlerList();
	protected final Player player;

	public CrazyArenaArenaPlayerEvent(final CrazyArena plugin, final S arena, final Player player)
	{
		super(plugin, arena);
		this.player = player;
	}

	public final Player getPlayer()
	{
		return player;
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
