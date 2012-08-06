package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;

import de.st_ddt.crazylogin.LoginPlugin;
import de.st_ddt.crazylogin.data.LoginData;

public class CrazyLoginPlayerDataEvent<S extends LoginData<S>> extends CrazyLoginEvent<S>
{

	protected final Player player;
	protected final S data;

	public CrazyLoginPlayerDataEvent(final LoginPlugin<S> plugin, final Player player, final S data)
	{
		super(plugin);
		this.player = player;
		this.data = data;
	}

	public Player getPlayer()
	{
		return player;
	}

	public S getData()
	{
		return data;
	}
}
