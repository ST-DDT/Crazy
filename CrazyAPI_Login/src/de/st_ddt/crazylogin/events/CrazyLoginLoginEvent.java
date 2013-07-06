package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;

import de.st_ddt.crazylogin.data.LoginData;

/**
 * This event is called after a player logged in successfully.
 */
public class CrazyLoginLoginEvent extends CrazyLoginPlayerDataEvent
{

	public CrazyLoginLoginEvent(final Player player, final LoginData data)
	{
		super(player, data);
	}
}
