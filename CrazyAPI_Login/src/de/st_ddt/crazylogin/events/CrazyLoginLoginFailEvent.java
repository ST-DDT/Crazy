package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;

import de.st_ddt.crazylogin.data.LoginData;

/**
 * This event is called after a player fails to login.
 */
public class CrazyLoginLoginFailEvent extends CrazyLoginPlayerDataEvent
{

	protected final LoginFailReason reason;

	public CrazyLoginLoginFailEvent(final Player player, final LoginData data, final LoginFailReason reason)
	{
		super(player, data);
		this.reason = reason;
	}

	/**
	 * @return The reason why the login failed.
	 */
	public LoginFailReason getReason()
	{
		return reason;
	}
}
