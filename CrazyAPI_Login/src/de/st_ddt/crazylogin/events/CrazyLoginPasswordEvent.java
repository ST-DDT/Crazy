package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;

import de.st_ddt.crazylogin.LoginPlugin;

public class CrazyLoginPasswordEvent extends CrazyLoginEvent
{

	protected final Player player;
	protected final String password;

	public CrazyLoginPasswordEvent(final LoginPlugin plugin, final Player player, final String password)
	{
		super(plugin);
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
