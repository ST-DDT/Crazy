package de.st_ddt.crazyonline.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.st_ddt.crazyonline.CrazyOnline;
import de.st_ddt.crazyplugin.events.CrazyPlayerRemoveEvent;

public class CrazyOnlineCrazyListener implements Listener
{

	protected final CrazyOnline plugin;

	public CrazyOnlineCrazyListener(final CrazyOnline plugin)
	{
		super();
		this.plugin = plugin;
	}

	public CrazyOnline getPlugin()
	{
		return plugin;
	}

	@EventHandler
	public void CrazyPlayerRemoveEvent(final CrazyPlayerRemoveEvent event)
	{
		if (plugin.deletePlayerData(event.getPlayer()))
			event.markDeletion(plugin);
	}
}
