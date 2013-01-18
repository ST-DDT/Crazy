package de.st_ddt.crazysquads.events;

import org.bukkit.event.HandlerList;

import de.st_ddt.crazysquads.data.Squad;

public class CrazySquadsSquadDeleteEvent extends CrazySquadsEvent
{

	private static final HandlerList handlers = new HandlerList();
	private final Squad squad;

	public CrazySquadsSquadDeleteEvent(final Squad squad)
	{
		super();
		this.squad = squad;
	}

	public Squad getSquad()
	{
		return squad;
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
