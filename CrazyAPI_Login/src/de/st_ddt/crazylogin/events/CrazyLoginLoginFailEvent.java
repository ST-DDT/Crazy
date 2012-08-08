package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;

import de.st_ddt.crazylogin.LoginPlugin;
import de.st_ddt.crazylogin.data.LoginData;

public class CrazyLoginLoginFailEvent<S extends LoginData> extends CrazyLoginPlayerDataEvent<S>
{

	protected final LoginFailReason reason;

	public CrazyLoginLoginFailEvent(final LoginPlugin<S> plugin, final Player player, final S data, final LoginFailReason reason)
	{
		super(plugin, player, data);
		this.reason = reason;
	}

	public LoginFailReason getReason()
	{
		return reason;
	}
}
