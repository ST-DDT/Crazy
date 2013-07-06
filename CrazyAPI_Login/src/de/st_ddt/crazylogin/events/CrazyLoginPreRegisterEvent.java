package de.st_ddt.crazylogin.events;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import de.st_ddt.crazylogin.data.LoginData;

public class CrazyLoginPreRegisterEvent extends CrazyLoginEvent implements Cancellable
{

	protected final Player player;
	protected final Set<? extends LoginData> associates;
	protected boolean cancelled = false;

	public CrazyLoginPreRegisterEvent(final Player player, final Set<? extends LoginData> associates)
	{
		super();
		this.player = player;
		this.associates = associates;
	}

	/**
	 * @return The player who tries to register.
	 */
	public Player getPlayer()
	{
		return player;
	}

	/**
	 * @return All known associates who have an account in CrazyLogin.
	 */
	public Set<? extends LoginData> getAssociates()
	{
		return associates;
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
