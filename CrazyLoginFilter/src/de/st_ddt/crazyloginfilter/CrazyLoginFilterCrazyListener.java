package de.st_ddt.crazyloginfilter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.st_ddt.crazyplugin.events.CrazyPlayerRemoveEvent;

public class CrazyLoginFilterCrazyListener implements Listener
{

	protected final CrazyLoginFilter plugin;

	public CrazyLoginFilterCrazyListener(CrazyLoginFilter plugin)
	{
		super();
		this.plugin = plugin;
	}

	public CrazyLoginFilter getPlugin()
	{
		return plugin;
	}

	@EventHandler
	public void CrazyPlayerRemoveEvent(CrazyPlayerRemoveEvent event)
	{
		if (plugin.deletePlayerData(event.getPlayer()))
			event.markDeletion(plugin);
	}
}
