package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;

public class CrazyLoginPasswordEvent extends CrazyLoginEvent
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
