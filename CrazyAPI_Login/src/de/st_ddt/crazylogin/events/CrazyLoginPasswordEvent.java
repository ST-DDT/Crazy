package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;

import de.st_ddt.crazylogin.data.LoginData;

/**
 * This event is called after the player registered successfully or changed his password.<br>
 * This event is only called if
 */
public class CrazyLoginPasswordEvent extends CrazyLoginPlayerDataEvent
{

	protected final String password;

	public CrazyLoginPasswordEvent(final Player player, final LoginData data, final String password)
	{
		super(player, data);
		this.password = password;
	}

	/**
	 * @return The new raw password the player selected.<br>
	 *         Do not store it unencryptedly!
	 */
	public String getPassword()
	{
		return password;
	}
}
