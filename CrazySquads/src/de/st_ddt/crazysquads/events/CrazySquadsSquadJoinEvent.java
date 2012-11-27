package de.st_ddt.crazysquads.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;

public class CrazySquadsSquadJoinEvent extends CrazySquadsEvent implements Cancellable
{

	private static final HandlerList handlers = new HandlerList();
	private final Squad squad;
	private final Player newMember;
	private boolean cancelled = false;
	private String reason;

	public CrazySquadsSquadJoinEvent(final CrazySquads plugin, final Squad squad, final Player newMember)
	{
		super(plugin);
		this.squad = squad;
		this.newMember = newMember;
	}

	public Squad getSquad()
	{
		return squad;
	}

	public Player getNewMember()
	{
		return newMember;
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
