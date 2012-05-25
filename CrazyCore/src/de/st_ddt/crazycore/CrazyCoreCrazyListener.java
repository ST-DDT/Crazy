package de.st_ddt.crazycore;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.st_ddt.crazyplugin.events.CrazyPlayerRemoveEvent;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class CrazyCoreCrazyListener implements Listener
{

	protected final CrazyCore plugin;

	public CrazyCoreCrazyListener(CrazyCore plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler
	public void CrazyPlayerRemoveEvent(CrazyPlayerRemoveEvent event)
	{
		if (CrazyLocale.removeUserLanguage(event.getPlayer()))
			event.markDeletion(plugin);
	}
}
