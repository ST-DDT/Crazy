package de.st_ddt.crazyloginrank.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyloginrank.CrazyLoginRank;
import de.st_ddt.crazyplugin.events.CrazyPlayerRemoveEvent;

public class CrazyLoginRankCrazyListener implements Listener
{

	protected final CrazyLoginRank plugin;

	public CrazyLoginRankCrazyListener(final CrazyLoginRank plugin)
	{
		super();
		this.plugin = plugin;
	}

	public CrazyLoginRank getPlugin()
	{
		return plugin;
	}

	@EventHandler
	public void CrazyPlayerRemoveEvent(final CrazyPlayerRemoveEvent event)
	{
		if (plugin.deletePlayerData(event.getPlayer()))
			event.markDeletion((JavaPlugin) plugin);
	}
}
