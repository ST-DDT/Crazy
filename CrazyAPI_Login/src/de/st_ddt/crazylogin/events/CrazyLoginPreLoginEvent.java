package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import de.st_ddt.crazylogin.data.LoginData;

/**
 * This event is called after the player entered his password and before the player is logged in.<br>
 * Cancelling this event will cancel the login and a NoPermission message will be shown.<br>
 * Cancelling this event will call an {@link CrazyLoginLoginFailEvent}.
 */
public class CrazyLoginPreLoginEvent extends CrazyLoginPlayerDataEvent implements Cancellable
{

	protected boolean cancelled = false;

	public CrazyLoginPreLoginEvent(final Player player, final LoginData data)
	{
		super(player, data);
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
}
