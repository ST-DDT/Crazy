package de.st_ddt.crazypunisher.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class CrazyPunisherVisibilityChangeEvent extends CrazyPunisherEvent implements Cancellable
{

	private static final HandlerList handlers = new HandlerList();
	protected final OfflinePlayer player;
	protected final boolean newVisibility;
	protected boolean cancelled;

	public CrazyPunisherVisibilityChangeEvent(OfflinePlayer player, boolean willbeVisible)
	{
		super();
		this.player = player;
		this.newVisibility = willbeVisible;
	}

	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	public OfflinePlayer getPlayer()
	{
		return player;
	}

	public boolean getNewVisibility()
	{
		return newVisibility;
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
}
