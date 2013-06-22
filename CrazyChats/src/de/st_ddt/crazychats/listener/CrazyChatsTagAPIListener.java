package de.st_ddt.crazychats.listener;

import org.bukkit.entity.Player;
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

	@EventHandler(priority = EventPriority.NORMAL)
	public void PlayerNameTag(final PlayerReceiveNameTagEvent event)
	{
		final Player player = event.getNamedPlayer();
		final String name = getCustomHeadName(player);
		if (name == null)
			event.setTag(getDefaultHeadName(player));
		else
			event.setTag(name);
	}

	private String getDefaultHeadName(final Player player)
	{
		return plugin.getGroupHeadnamePrefix(player) + player.getName();
	}

	private String getCustomHeadName(final Player player)
	{
		final ChatPlayerData data = plugin.getPlayerData(player);
		if (data == null)
			return null;
		else
			return data.getHeadName();
	}
}
