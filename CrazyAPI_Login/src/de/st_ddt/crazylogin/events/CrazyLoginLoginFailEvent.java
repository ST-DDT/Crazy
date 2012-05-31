package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;

import de.st_ddt.crazylogin.LoginData;
import de.st_ddt.crazylogin.LoginPlugin;

public class CrazyLoginLoginFailEvent extends CrazyLoginEvent
{

	protected final Player player;
	protected final LoginData data;
	protected final LoginFailReason reason;

	public CrazyLoginLoginFailEvent(final LoginPlugin plugin, final LoginData data, final Player player, final LoginFailReason reason)
	{
		super(plugin);
		this.player = player;
		this.data = data;
		this.reason = reason;
	}

	public Player getPlayer()
	{
		return player;
	}

	public LoginData getData()
	{
		return data;
	}

	public LoginFailReason getReason()
	{
		return reason;
	}
}
