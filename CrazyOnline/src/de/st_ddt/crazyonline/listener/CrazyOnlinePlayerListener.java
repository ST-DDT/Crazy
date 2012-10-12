package de.st_ddt.crazyonline.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.st_ddt.crazyonline.CrazyOnline;
import de.st_ddt.crazyonline.commands.CrazyOnlineCommandExecutor;
import de.st_ddt.crazyonline.data.OnlinePlayerData;
import de.st_ddt.crazyonline.tasks.PlayerDataShortTimeCheckTask;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class CrazyOnlinePlayerListener implements Listener
{

	protected final CrazyOnline plugin;
	private final CrazyOnlineCommandExecutor sinceCommand;

	public CrazyOnlinePlayerListener(final CrazyOnline plugin, final CrazyOnlineCommandExecutor sinceCommand)
	{
		super();
		this.plugin = plugin;
		this.sinceCommand = sinceCommand;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void PlayerLoginDataUpdate(final PlayerLoginEvent event)
	{
		plugin.getCrazyDatabase().updateEntry(event.getPlayer().getName());
	}

	@EventHandler(ignoreCancelled = true)
	public void PlayerJoin(final PlayerJoinEvent event)
	{
		PlayerJoin(event.getPlayer());
	}

	public void PlayerJoin(final Player player)
	{
		OnlinePlayerData data = plugin.getPlayerData(player);
		if (data == null)
			data = new OnlinePlayerData(player);
		data.join(player.getAddress().getAddress().getHostAddress());
		plugin.getCrazyDatabase().save(data);
		if (plugin.isShowOnlineInfoEnabled())
			if (player.hasPermission("crazyonline.since.auto"))
				try
				{
					sinceCommand.command(player, new String[0]);
				}
				catch (final CrazyException e)
				{}
		plugin.getCrazyLogger().log("Join", player.getName() + " @ " + player.getAddress().getAddress().getHostAddress() + " joined the server");
	}

	@EventHandler(priority = EventPriority.LOW)
	public void PlayerQuit(final PlayerQuitEvent event)
	{
		PlayerQuit(event.getPlayer());
	}

	public void PlayerQuit(final Player player)
	{
		plugin.getCrazyLogger().log("Quit", player.getName() + " @ " + player.getAddress().getAddress().getHostAddress() + " left the server");
		final OnlinePlayerData data = plugin.getPlayerData(player);
		if (data == null)
			return;
		data.quit();
		plugin.getCrazyDatabase().save(data);
		if (!plugin.getCrazyDatabase().isCachedDatabase())
			Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable()
			{

				@Override
				public void run()
				{
					if (!data.isOnline())
						plugin.getCrazyDatabase().unloadEntry(player.getName());
				}
			}, 20 * 60 * (plugin.isDeletingShortVisitorsEnabled() ? 15 : 1));
	}

	public void PlayerQuit2(final Player player)
	{
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
		Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new PlayerDataShortTimeCheckTask(plugin, player.getName()), 20 * 60 * 10);
	}
}
