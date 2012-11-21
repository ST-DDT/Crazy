package de.st_ddt.crazychats.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.data.ChatPlayerData;
import de.st_ddt.crazyplugin.events.CrazyPlayerRemoveEvent;

public final class CrazyChatsCrazyListener implements Listener
{

	protected final CrazyChats plugin;

	public CrazyChatsCrazyListener(final CrazyChats plugin)
	{
		super();
		this.plugin = plugin;
	}

	public CrazyChats getPlugin()
	{
		return plugin;
	}

	@EventHandler
	public void CrazyPlayerRemoveEvent(final CrazyPlayerRemoveEvent event)
	{
		final String player = event.getPlayer().toLowerCase();
		if (plugin.deletePlayerData(player))
			event.markDeletion((Plugin) plugin);
		synchronized (plugin.getPlayerDataLock())
		{
			for (final ChatPlayerData data : plugin.getPlayerData())
				data.getMutedPlayers().remove(player);
		}
	}
}
