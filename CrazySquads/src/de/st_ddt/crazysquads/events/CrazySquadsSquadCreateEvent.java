package de.st_ddt.crazysquads.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;

public class CrazySquadsSquadCreateEvent extends CrazySquadsEvent implements Cancellable
{

	private static final HandlerList handlers = new HandlerList();
	private final Squad squad;
	private boolean cancelled = false;
	private String reason;

	public CrazySquadsSquadCreateEvent(final CrazySquads plugin, final Squad squad)
	{
		super(plugin);
		this.squad = squad;
	}

	public Squad getSquad()
	{
		return squad;
	}

	@Override
	public boolean isCancelled()
	{
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel)
	{
		this.cancelled = cancel;
	}

	public String getReason()
	{
		return reason;
	}

	public void setCancelled(String reason)
	{
		this.cancelled = true;
		this.reason = reason;
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
