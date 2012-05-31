package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import de.st_ddt.crazylogin.LoginData;
import de.st_ddt.crazylogin.LoginPlugin;

public class CrazyLoginPreLoginEvent extends CrazyLoginEvent implements Cancellable
{

	protected final Player player;
	protected boolean cancelled = false;
	protected final LoginData data;

	public CrazyLoginPreLoginEvent(final LoginPlugin plugin, final Player player, final LoginData data)
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

	@Override
	public boolean isCancelled()
	{
		return cancelled;
	}

	@Override
	public void setCancelled(final boolean cancel)
	{
		this.cancelled = cancel;
	}
}
