package de.st_ddt.crazysquads.events;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;

public class CrazySquadsSquadCreateEvent extends CrazySquadsEvent
{

	private static final HandlerList handlers = new HandlerList();
	private final Squad squad;
	private final Set<Player> members;

	public CrazySquadsSquadCreateEvent(final CrazySquads plugin, final Squad squad, final Set<Player> members)
	{
		super(plugin);
		this.squad = squad;
		this.members = members;
	}

	public Squad getSquad()
	{
		return squad;
	}

	public Set<Player> getMembers()
	{
		return members;
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
