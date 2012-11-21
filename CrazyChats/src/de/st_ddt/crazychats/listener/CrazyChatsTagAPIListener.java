package de.st_ddt.crazychats.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.data.ChatPlayerData;

public class CrazyChatsTagAPIListener implements Listener
{

	private final CrazyChats plugin;

	public CrazyChatsTagAPIListener(final CrazyChats plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void PlayerNameTag(final PlayerReceiveNameTagEvent event)
	{
		final ChatPlayerData data = plugin.getPlayerData(event.getNamedPlayer());
		if (data == null)
			return;
		final String name = data.getHeadName();
		if (name != null)
			event.setTag(name);
	}
}
