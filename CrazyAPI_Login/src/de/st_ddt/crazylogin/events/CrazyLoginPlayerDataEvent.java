package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;

import de.st_ddt.crazylogin.data.LoginData;

public abstract class CrazyLoginPlayerDataEvent extends CrazyLoginEvent
{

	protected final Player player;
	protected final LoginData data;

	public CrazyLoginPlayerDataEvent(final Player player, final LoginData data)
	{
		super();
		this.player = player;
		this.data = data;
	}

	/**
	 * @return The player who caused this event to be called.
	 */
	public Player getPlayer()
	{
		return player;
	}

	/**
	 * @return The LoginData belonging to the player who caused this event to be called.
	 */
	public LoginData getData()
	{
		return data;
	}
}
