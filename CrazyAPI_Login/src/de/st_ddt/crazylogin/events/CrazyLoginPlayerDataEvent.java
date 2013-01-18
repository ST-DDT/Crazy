package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;

import de.st_ddt.crazylogin.data.LoginData;

public class CrazyLoginPlayerDataEvent extends CrazyLoginEvent
{

	protected final Player player;
	protected final LoginData data;

	public CrazyLoginPlayerDataEvent(final Player player, final LoginData data)
	{
		super();
		this.player = player;
		this.data = data;
	}

	public Player getPlayer()
	{
		return player;
	}

	public LoginData getData()
	{
		return data;
	}
}
