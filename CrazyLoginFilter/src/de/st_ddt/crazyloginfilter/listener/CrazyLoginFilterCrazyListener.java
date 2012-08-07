package de.st_ddt.crazyloginfilter.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.st_ddt.crazyloginfilter.CrazyLoginFilter;
import de.st_ddt.crazyplugin.events.CrazyPlayerRemoveEvent;

public class CrazyLoginFilterCrazyListener implements Listener
{

	protected final CrazyLoginFilter plugin;

	public CrazyLoginFilterCrazyListener(final CrazyLoginFilter plugin)
	{
		super();
		this.plugin = plugin;
	}

	public CrazyLoginFilter getPlugin()
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
