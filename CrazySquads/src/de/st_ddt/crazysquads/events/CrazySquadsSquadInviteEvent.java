package de.st_ddt.crazysquads.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import de.st_ddt.crazysquads.data.Squad;

public class CrazySquadsSquadInviteEvent extends CrazySquadsEvent implements Cancellable
{

	private static final HandlerList handlers = new HandlerList();
	private final Squad squad;
	private final Player invited;
	private boolean cancelled = false;
	private String reason;

	public CrazySquadsSquadInviteEvent(final Squad squad, final Player invited)
	{
		super();
		this.squad = squad;
		this.invited = invited;
	}

	public Squad getSquad()
	{
		return squad;
	}

	public Player getInvited()
	{
		return invited;
	}

	@Override
	public boolean isCancelled()
	{
		return cancelled;
	}

	@Override
	public void setCancelled(final boolean cancel)
	{
		this.cancelled = cancel;
	}

	public String getReason()
	{
		return reason;
	}

	public void setCancelled(final String reason)
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
