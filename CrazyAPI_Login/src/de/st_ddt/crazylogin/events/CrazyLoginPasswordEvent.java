package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;

import de.st_ddt.crazylogin.data.LoginData;

public class CrazyLoginPasswordEvent<S extends LoginData> extends CrazyLoginEvent
{

	protected final Player player;
	protected final String password;

	public CrazyLoginPasswordEvent(final Player player, final String password)
	{
		super();
		this.player = player;
		this.password = password;
	}

	public Player getPlayer()
	{
		return player;
	}

	public String getPassword()
	{
		return password;
	}
}
