package de.st_ddt.crazyonline.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.st_ddt.crazyonline.CrazyOnline;
import de.st_ddt.crazyonline.data.OnlinePlayerData;
import de.st_ddt.crazyonline.tasks.PlayerDataCheckTask;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;

public class CrazyOnlinePlayerListener implements Listener
{

	protected final CrazyOnline plugin;

	public CrazyOnlinePlayerListener(final CrazyOnline plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void PlayerJoin(final PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		OnlinePlayerData data = plugin.getPlayerData(player);
		if (data == null)
			data = new OnlinePlayerData(player);
		data.join(player.getAddress().getAddress().getHostAddress());
		plugin.getCrazyDatabase().save(data);
		if (plugin.isShowOnlineInfoEnabled())
			if (player.hasPermission("crazyonline.since.auto"))
				try
				{
					plugin.commandSince(player);
				}
				catch (final CrazyCommandException e)
				{}
		plugin.getCrazyLogger().log("Join", player.getName() + " @ " + player.getAddress().getAddress().getHostAddress() + " joined the server");
	}

	@EventHandler(priority = EventPriority.LOW)
	public void PlayerQuit(final PlayerQuitEvent event)
	{
		final Player player = event.getPlayer();
		plugin.getCrazyLogger().log("Quit", player.getName() + " @ " + player.getAddress().getAddress().getHostAddress() + " left the server");
		final OnlinePlayerData data = plugin.getPlayerData(player);
		if (data == null)
			return;
		data.quit();
		plugin.getCrazyDatabase().save(data);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void PlayerQuitCheck(final PlayerQuitEvent event)
	{
		if (!plugin.isDeletingShortVisitorsEnabled())
			return;
		final Player player = event.getPlayer();
		final OnlinePlayerData data = plugin.getPlayerData(player);
		if (data == null)
			return;
		if (data.getTimeTotal() > 5)
			return;
		Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new PlayerDataCheckTask(plugin, player.getName()), 20 * 60 * 10);
	}
}
