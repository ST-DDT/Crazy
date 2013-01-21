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
import de.st_ddt.crazychats.channels.arena.ArenaChannel;
import de.st_ddt.crazychats.channels.arena.ArenaJudgeChannel;
import de.st_ddt.crazychats.channels.arena.ArenaPlayerChannel;
import de.st_ddt.crazychats.channels.arena.ArenaSpectatorChannel;
import de.st_ddt.crazychats.data.ChatPlayerData;

public class CrazyArenaCrazyChatsListener implements Listener
{

	private final CrazyArena plugin;
	private final CrazyChats chatsPlugin;
	private final Map<Arena<?>, ArenaChannel> chats = new HashMap<Arena<?>, ArenaChannel>();
	private final Map<Arena<?>, ArenaChannel> spectatorChats = new HashMap<Arena<?>, ArenaChannel>();
	private final Map<Arena<?>, ArenaChannel> playerChats = new HashMap<Arena<?>, ArenaChannel>();
	private final Map<Arena<?>, ArenaChannel> judgeChats = new HashMap<Arena<?>, ArenaChannel>();

	public CrazyArenaCrazyChatsListener(final CrazyArena plugin)
	{
		super();
		this.plugin = plugin;
		this.chatsPlugin = CrazyChats.getPlugin();
	}

	@EventHandler
	public void ArenaCreateEvent(final CrazyArenaArenaCreateEvent event)
	{
		final Arena<?> arena = event.getArena();
		chats.put(arena, new ArenaChannel(plugin, arena));
		spectatorChats.put(arena, new ArenaSpectatorChannel(plugin, arena));
		playerChats.put(arena, new ArenaPlayerChannel(plugin, arena));
		judgeChats.put(arena, new ArenaJudgeChannel(plugin, arena));
	}

	@EventHandler
	public void ArenaPlayerJoin(final CrazyArenaArenaPlayerJoinEvent event)
	{
		final ChatPlayerData data = chatsPlugin.getPlayerData(event.getPlayer());
		if (data != null)
		{
			data.getAccessibleChannels().add(chats.get(event.getArena()));
			data.getAccessibleChannels().add(spectatorChats.get(event.getArena()));
			data.getAccessibleChannels().add(playerChats.get(event.getArena()));
			data.getAccessibleChannels().add(judgeChats.get(event.getArena()));
		}
	}

	@EventHandler
	public void ArenaPlayerLeave(final CrazyArenaArenaPlayerLeaveEvent event)
	{
		final ChatPlayerData data = chatsPlugin.getPlayerData(event.getPlayer());
		if (data != null)
		{
			data.getAccessibleChannels().remove(chats.get(event.getArena()));
			data.getAccessibleChannels().remove(spectatorChats.get(event.getArena()));
			data.getAccessibleChannels().remove(playerChats.get(event.getArena()));
			data.getAccessibleChannels().remove(judgeChats.get(event.getArena()));
		}
	}

	@EventHandler
	public void ArenaDeleteEvent(final CrazyArenaArenaDeleteEvent event)
	{
		chats.remove(event.getArena());
		spectatorChats.remove(event.getArena());
		playerChats.remove(event.getArena());
		judgeChats.remove(event.getArena());
	}
}
