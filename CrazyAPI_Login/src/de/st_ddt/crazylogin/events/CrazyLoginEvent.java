package de.st_ddt.crazylogin.events;

import org.bukkit.event.HandlerList;

import de.st_ddt.crazylogin.LoginPlugin;
import de.st_ddt.crazylogin.data.LoginData;
import de.st_ddt.crazyplugin.events.CrazyEvent;

public class CrazyLoginEvent<S extends LoginData> extends CrazyEvent<LoginPlugin<S>>
{

	private static final HandlerList handlers = new HandlerList();

	public CrazyLoginEvent(final LoginPlugin<S> plugin)
	{
		super(plugin);
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
