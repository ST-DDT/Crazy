package de.st_ddt.crazyarena.listener;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.events.CrazyArenaArenaCreateEvent;
import de.st_ddt.crazyarena.events.CrazyArenaArenaDeleteEvent;
import de.st_ddt.crazyarena.events.CrazyArenaArenaPlayerJoinEvent;
import de.st_ddt.crazyarena.events.CrazyArenaArenaPlayerLeaveEvent;
import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.channels.ArenaChannel;
import de.st_ddt.crazychats.data.ChatPlayerData;

public class CrazyArenaCrazyChatsListener implements Listener
{

	private final CrazyArena plugin;
	private final CrazyChats chatsPlugin;
	private final Map<Arena<?>, ArenaChannel> chats = new HashMap<Arena<?>, ArenaChannel>();

	public CrazyArenaCrazyChatsListener(final CrazyArena plugin)
	{
		super();
		this.plugin = plugin;
		this.chatsPlugin = CrazyChats.getPlugin();
	}

	@EventHandler
	public void ArenaCreateEvent(CrazyArenaArenaCreateEvent event)
	{
		Arena<?> arena = event.getArena();
		chats.put(arena, new ArenaChannel(plugin, arena));
	}

	@EventHandler
	public void ArenaPlayerJoin(CrazyArenaArenaPlayerJoinEvent event)
	{
		final ChatPlayerData data = chatsPlugin.getPlayerData(event.getPlayer());
		if (data != null)
			data.getAccessibleChannels().add(chats.get(event.getArena()));
	}

	@EventHandler
	public void ArenaPlayerLeave(final CrazyArenaArenaPlayerLeaveEvent event)
	{
		final ChatPlayerData data = chatsPlugin.getPlayerData(event.getPlayer());
		if (data != null)
			data.getAccessibleChannels().remove(chats.get(event.getArena()));
	}

	@EventHandler
	public void ArenaDeleteEvent(CrazyArenaArenaDeleteEvent event)
	{
		chats.remove(event.getArena());
	}
}
