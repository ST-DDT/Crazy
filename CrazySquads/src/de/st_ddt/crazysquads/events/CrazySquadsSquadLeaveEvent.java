package de.st_ddt.crazysquads.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import de.st_ddt.crazysquads.data.Squad;

public class CrazySquadsSquadLeaveEvent extends CrazySquadsEvent
{

	private static final HandlerList handlers = new HandlerList();
	private final Squad squad;
	private final Player player;

	public CrazySquadsSquadLeaveEvent(final Squad squad, final Player left)
	{
		super();
		this.squad = squad;
		this.player = left;
	}

	public Squad getSquad()
	{
		return squad;
	}

	public Player getLeftMember()
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
