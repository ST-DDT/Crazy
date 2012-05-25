package de.st_ddt.crazyloginfilter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class CrazyLoginFilterPlayerListener implements Listener
{

	private final CrazyLoginFilter plugin;

	public CrazyLoginFilterPlayerListener(final CrazyLoginFilter plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler
	public void PlayerLogin(final PlayerLoginEvent event)
	{
		final Player player = event.getPlayer();
		if (!plugin.checkIP(player) || !plugin.checkConnection(player))
		{
			event.setResult(Result.KICK_OTHER);
			event.setKickMessage(plugin.getLocale().getLocaleMessage(player, "ACCESS.DENIED"));
			return;
		}
	}
}
