package de.st_ddt.crazycore.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazycore.tasks.PlayerWipeTask;
import de.st_ddt.crazyplugin.events.CrazyPlayerAssociatesEvent;
import de.st_ddt.crazyplugin.events.CrazyPlayerIPsConnectedToNameEvent;
import de.st_ddt.crazyplugin.events.CrazyPlayerNamesConnectedToIPEvent;
import de.st_ddt.crazyplugin.events.CrazyPlayerPreRemoveEvent;
import de.st_ddt.crazyplugin.events.CrazyPlayerRemoveEvent;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.Named;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyListener implements Listener
{

	protected final CrazyCore plugin;

	public CrazyListener(final CrazyCore plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler
	public void PlayerIPs(final CrazyPlayerIPsConnectedToNameEvent event)
	{
		final Player player = Bukkit.getPlayerExact(event.getSearchedName());
		if (player != null)
			event.getIPs().add(player.getAddress().getAddress().getHostAddress());
	}

	@EventHandler
	public void PlayerNames(final CrazyPlayerNamesConnectedToIPEvent event)
	{
		for (final Player player : Bukkit.getOnlinePlayers())
			if (event.getSearchedIP().startsWith(player.getAddress().getAddress().getHostAddress()))
				event.getNames().add(player.getName());
	}

	@EventHandler
	public void PlayerAssociates(final CrazyPlayerAssociatesEvent event)
	{
		final CrazyPlayerIPsConnectedToNameEvent nameevent = new CrazyPlayerIPsConnectedToNameEvent(event.getSearchedName());
		nameevent.callEvent();
		for (final String ip : nameevent.getIPs())
		{
			final CrazyPlayerNamesConnectedToIPEvent ipevent = new CrazyPlayerNamesConnectedToIPEvent(ip);
			ipevent.callEvent();
			event.addNames(ipevent.getNames());
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void CrazyPlayerPreRemoveEvent(final CrazyPlayerPreRemoveEvent event)
	{
		if (plugin.getProtectedPlayers().contains(event.getPlayer().toLowerCase()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void CrazyPlayerRemoveOfflinePlayerDataEvent(final CrazyPlayerRemoveEvent event)
	{
		final OfflinePlayer player = Bukkit.getOfflinePlayer(event.getPlayer());
		if (player != null)
		{
			if (plugin.isWipingPlayerBansEnabled())
				player.setBanned(false);
			player.setOp(false);
			player.setWhitelisted(false);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	@Localized("CRAZYCORE.COMMAND.PLAYER.DELETE.KICK")
	public void CrazyPlayerRemovePlayerDataEvent(final CrazyPlayerRemoveEvent event)
	{
		final Player player = Bukkit.getPlayerExact(event.getPlayer());
		if (player != null)
			if (player.isOnline())
			{
				player.setOp(false);
				player.setWhitelisted(false);
				player.leaveVehicle();
				player.getInventory().clear();
				player.setGameMode(Bukkit.getDefaultGameMode());
				player.setExp(0);
				player.setFoodLevel(20);
				player.setHealth(20);
				player.setFireTicks(0);
				player.resetPlayerTime();
				final Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
				player.setCompassTarget(spawn);
				player.teleport(spawn);
				player.setBedSpawnLocation(spawn);
				player.saveData();
				player.kickPlayer(plugin.getLocale().getLocaleMessage(player, "COMMAND.PLAYER.DELETE.KICK"));
			}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void CrazyPlayerRemoveLanguageEvent(final CrazyPlayerRemoveEvent event)
	{
		if (CrazyLocale.removeUserLanguage(event.getPlayer()))
			event.markDeletion((Named) plugin);
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void CrazyPlayerRemoveCommandEvent(final CrazyPlayerRemoveEvent event)
	{
		final String name = event.getPlayer();
		final ConsoleCommandSender console = Bukkit.getConsoleSender();
		Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable()
		{

			@Override
			public void run()
			{
				for (final String command : plugin.getWipePlayerCommands())
					Bukkit.dispatchCommand(console, ChatHelper.putArgs(command, name));
			}
		});
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void CrazyPlayerRemoveFileEvent(final CrazyPlayerRemoveEvent event)
	{
		new PlayerWipeTask(event.getPlayer(), true, plugin.getWipePlayerFilePaths(), plugin.isWipingPlayerWorldFilesEnabled()).execute();
		plugin.save();
	}
}
