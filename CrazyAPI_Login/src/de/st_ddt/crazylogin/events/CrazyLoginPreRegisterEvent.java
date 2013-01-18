package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import de.st_ddt.crazylogin.data.LoginData;

public class CrazyLoginPreRegisterEvent<S extends LoginData> extends CrazyLoginPlayerDataEvent implements Cancellable
{

	protected boolean cancelled = false;

	public CrazyLoginPreRegisterEvent(final Player player, final S data)
	{
		super(player, data);
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
