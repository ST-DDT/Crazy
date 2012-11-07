package de.st_ddt.crazytimecard.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.st_ddt.crazyplugin.events.CrazyPlayerRemoveEvent;
import de.st_ddt.crazytimecard.CrazyTimeCard;
import de.st_ddt.crazyutil.Named;

public final class CrazyTimeCardCrazyListener implements Listener
{

	protected final CrazyTimeCard plugin;

	public CrazyTimeCardCrazyListener(final CrazyTimeCard plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler
	public void CrazyPlayerRemoveEvent(final CrazyPlayerRemoveEvent event)
	{
		if (plugin.deletePlayerData(event.getPlayer()))
			event.markDeletion((Named) plugin);
	}
}
