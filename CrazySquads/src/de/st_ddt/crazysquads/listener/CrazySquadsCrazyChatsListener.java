package de.st_ddt.crazysquads.listener;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.channels.SquadChannel;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazysquads.events.CrazySquadsSquadCreateEvent;
import de.st_ddt.crazysquads.events.CrazySquadsSquadDeleteEvent;
import de.st_ddt.crazysquads.events.CrazySquadsSquadJoinEvent;
import de.st_ddt.crazysquads.events.CrazySquadsSquadLeaveEvent;

public class CrazySquadsCrazyChatsListener implements Listener
{

	private final CrazySquads plugin;
	private final CrazyChats chatsPlugin;
	private final Map<Squad, SquadChannel> chats = new HashMap<Squad, SquadChannel>();

	public CrazySquadsCrazyChatsListener(final CrazySquads plugin)
	{
		super();
		this.plugin = plugin;
		this.chatsPlugin = CrazyChats.getPlugin();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void SquadCreate(final CrazySquadsSquadCreateEvent event)
	{
		Squad squad = event.getSquad();
		chats.put(squad, new SquadChannel(plugin, squad, squad.getMembers()));
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void SquadJoin(final CrazySquadsSquadJoinEvent event)
	{
		final ChatPlayerData data = chatsPlugin.getPlayerData(event.getNewMember());
		if (data != null)
			data.getAccessibleChannels().add(chats.get(event.getSquad()));
	}

	@EventHandler
	public void SquadLeave(final CrazySquadsSquadLeaveEvent event)
	{
		final Squad squad = event.getSquad();
		final ChatPlayerData data = chatsPlugin.getPlayerData(event.getLeftMember());
		if (data != null)
			data.getAccessibleChannels().remove(chats.get(squad));
	}

	@EventHandler
	public void SquadDelete(final CrazySquadsSquadDeleteEvent event)
	{
		chats.remove(event.getSquad());
	}
}
