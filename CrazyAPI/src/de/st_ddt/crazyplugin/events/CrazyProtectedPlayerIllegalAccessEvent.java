package de.st_ddt.crazyplugin.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * This event is called if a player tries to access a protected player but don't have the permission to do that.
 */
public class CrazyProtectedPlayerIllegalAccessEvent extends CrazyProtectedPlayerAccessEvent
{

	private static final HandlerList handlers = new HandlerList();

	public CrazyProtectedPlayerIllegalAccessEvent(final String accessedPlayer, final Player accessingPlayer, final String plugin, final String task)
	{
		super(accessedPlayer, accessingPlayer, plugin, task, false);
		setCancelled(true);
	}

	/**
	 * This event is called due to illegal access of a protected player.<br>
	 * This event represents the cancelled access. It is always cancelled.
	 * 
	 * @return True.
	 */
	@Override
	public final boolean isCancelled()
	{
		return true;
	}

	/**
	 * This event is called due to illegal access of a protected player.<br>
	 * This event represents the cancelled access. It is always cancelled.
	 * 
	 * @param cancel
	 *            Does not have any influence on this event.
	 */
	@Override
	public final void setCancelled(final boolean cancel)
	{
		this.cancelled = true;
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
