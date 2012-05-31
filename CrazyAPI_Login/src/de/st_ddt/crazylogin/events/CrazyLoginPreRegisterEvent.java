package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import de.st_ddt.crazylogin.LoginData;
import de.st_ddt.crazylogin.LoginPlugin;

public class CrazyLoginPreRegisterEvent extends CrazyLoginEvent implements Cancellable
{

	protected final Player player;
	protected final LoginData data;
	protected boolean cancelled = false;

	public CrazyLoginPreRegisterEvent(final LoginPlugin plugin, final Player player, final LoginData data)
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
