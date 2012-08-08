package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;

import de.st_ddt.crazylogin.LoginPlugin;
import de.st_ddt.crazylogin.data.LoginData;

public class CrazyLoginPasswordEvent<S extends LoginData> extends CrazyLoginEvent<S>
{

	protected final Player player;
	protected final String password;

	public CrazyLoginPasswordEvent(final LoginPlugin<S> plugin, final Player player, final String password)
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
