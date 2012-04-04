package de.st_ddt.crazypunisher.events;

import java.util.Date;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;

public class CrazyPunisherJailEvent extends CrazyPunisherEvent
{

	private static final HandlerList handlers = new HandlerList();
	protected final OfflinePlayer player;
	protected final Date jailedUntil;

	public CrazyPunisherJailEvent(OfflinePlayer player, Date jailedUntil)
	{
		super();
		this.player = player;
		this.jailedUntil = jailedUntil;
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

	public Date getJailedUntil()
	{
		return jailedUntil;
	}
}
