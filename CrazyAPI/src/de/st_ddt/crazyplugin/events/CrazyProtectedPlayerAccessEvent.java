package de.st_ddt.crazyplugin.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * This event is called if a player tries to access a protected player.
 */
public class CrazyProtectedPlayerAccessEvent extends CrazyEvent implements Cancellable
{

	private static final HandlerList handlers = new HandlerList();
	protected final String accessedPlayer;
	protected final Player accessingPlayer;
	protected final String plugin;
	protected final String task;
	protected final boolean accessAllowed;
	protected boolean cancelled = false;

	public CrazyProtectedPlayerAccessEvent(final String accessedPlayer, final Player accessingPlayer, final String plugin, final String task)
	{
		super();
		this.accessedPlayer = accessedPlayer;
		this.accessingPlayer = accessingPlayer;
		this.plugin = plugin;
		this.task = task;
		this.accessAllowed = true;
	}

	CrazyProtectedPlayerAccessEvent(final String accessedPlayer, final Player accessingPlayer, final String plugin, final String task, final boolean accessAllowed)
	{
		super();
		this.accessedPlayer = accessedPlayer;
		this.accessingPlayer = accessingPlayer;
		this.plugin = plugin;
		this.task = task;
		this.accessAllowed = accessAllowed;
	}

	/**
	 * @return The name of the protected player, that has been accessed.
	 */
	public final String getAccessedPlayer()
	{
		return accessedPlayer;
	}

	/**
	 * @return The player who tries access the protected player.
	 */
	public final Player getAccessingPlayer()
	{
		return accessingPlayer;
	}

	/**
	 * @return The plugin which tries to validate the access of the given player.
	 */
	public final String getPlugin()
	{
		return plugin;
	}

	/**
	 * @return The task that should be executed.
	 */
	public final String getTask()
	{
		return task;
	}

	/**
	 * @return True if the accessing player has the permission to access the protected player.<br>
	 *         False otherwise.
	 */
	public final boolean isAccessAllowed()
	{
		return accessAllowed;
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
