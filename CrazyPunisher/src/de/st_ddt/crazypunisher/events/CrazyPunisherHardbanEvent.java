package de.st_ddt.crazypunisher.events;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;

public class CrazyPunisherHardbanEvent extends CrazyPunisherEvent
{

	private static final HandlerList handlers = new HandlerList();
	protected final OfflinePlayer player;

	public CrazyPunisherHardbanEvent(final OfflinePlayer player)
	{
		super();
		this.player = player;
	}

	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	public OfflinePlayer getPlayer()
	{
		return player;
	}
}
