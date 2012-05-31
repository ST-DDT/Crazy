package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;

import de.st_ddt.crazylogin.LoginData;
import de.st_ddt.crazylogin.LoginPlugin;

public class CrazyLoginLoginEvent extends CrazyLoginEvent
{

	protected final Player player;
	protected final LoginData data;

	public CrazyLoginLoginEvent(final LoginPlugin plugin, final LoginData data, final Player player)
	{
		super(plugin);
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
