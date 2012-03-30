package de.st_ddt.crazyannouncer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CrazyAnnouncerPlayerListener implements Listener
{

	private final CrazyAnnouncer plugin;

	public CrazyAnnouncerPlayerListener(CrazyAnnouncer plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event)
	{
		plugin.login(event.getPlayer());
	}
}
