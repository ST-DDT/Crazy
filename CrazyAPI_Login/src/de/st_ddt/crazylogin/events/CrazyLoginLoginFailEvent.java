package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;

import de.st_ddt.crazylogin.data.LoginData;

public class CrazyLoginLoginFailEvent extends CrazyLoginPlayerDataEvent
{

	protected final LoginFailReason reason;

	public CrazyLoginLoginFailEvent(final Player player, final LoginData data, final LoginFailReason reason)
	{
		super(player, data);
		this.reason = reason;
	}

	public LoginFailReason getReason()
	{
		return reason;
	}
}
