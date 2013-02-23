package de.st_ddt.crazytimecard.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazytimecard.CrazyTimeCard;
import de.st_ddt.crazytimecard.data.PlayerTimeData;
import de.st_ddt.crazytimecard.tasks.KickTask;
import de.st_ddt.crazyutil.ChatConverter;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyTimeCardPlayerListener implements Listener
{

	protected final CrazyTimeCard plugin;
	private final Map<String, Location> movementBlocker = new HashMap<String, Location>();

	public CrazyTimeCardPlayerListener(final CrazyTimeCard plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	@Localized("CRAZYTIMECARD.KICKED.NAME.INVALIDCHARS")
	public void PlayerLoginNameCharCheck(final PlayerLoginEvent event)
	{
		final Player player = event.getPlayer();
		if (plugin.checkNameChars(player.getName()))
			return;
		event.setResult(Result.KICK_OTHER);
		event.setKickMessage(plugin.getLocale().getLocaleMessage(player, "KICKED.NAME.INVALIDCHARS"));
		plugin.getCrazyLogger().log("AccessDenied", "Denied access for player " + player.getName() + " @ " + event.getAddress().getHostAddress() + " because of invalid chars");
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	@Localized("CRAZYTIMECARD.KICKED.NAME.INVALIDCASE")
	public void PlayerLoginNameCaseCheck(final PlayerLoginEvent event)
	{
		final Player player = event.getPlayer();
		if (plugin.checkNameCase(player.getName()))
			return;
		event.setResult(Result.KICK_OTHER);
		event.setKickMessage(plugin.getLocale().getLocaleMessage(player, "KICKED.NAME.INVALIDCASE"));
		plugin.getCrazyLogger().log("AccessDenied", "Denied access for player " + player.getName() + " @ " + event.getAddress().getHostAddress() + " because of invalid name case");
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	@Localized("CRAZYTIMECARD.KICKED.NAME.INVALIDLENGTH $MinLength$ $MaxLength$")
	public void PlayerLoginNameLengthCheck(final PlayerLoginEvent event)
	{
		final Player player = event.getPlayer();
		if (plugin.checkNameLength(event.getPlayer().getName()))
			return;
		event.setResult(Result.KICK_OTHER);
		event.setKickMessage(plugin.getLocale().getLocaleMessage(player, "KICKED.NAME.INVALIDLENGTH", plugin.getMinNameLength(), plugin.getMaxNameLength()));
		plugin.getCrazyLogger().log("AccessDenied", "Denied access for player " + player.getName() + " @ " + event.getAddress().getHostAddress() + " because of invalid name length");
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void PlayerJoin(final PlayerJoinEvent event)
	{
		PlayerJoin(event.getPlayer());
	}

	@Localized({ "CRAZYTIMECARD.TIME.REMAINING.JOIN $Limit$ $Remaining$", "CRAZYTIMECARD.TIME.EXCEEDED.JOIN $Limit$" })
	public void PlayerJoin(final Player player)
	{
		PlayerTimeData data = plugin.getPlayerData(player);
		if (data == null)
		{
			data = new PlayerTimeData(player.getName(), plugin.getStartDuration());
			plugin.getCrazyDatabase().save(data);
		}
		final Date exceed = data.getLimit();
		if (data.isActive())
		{
			final long remaining = exceed.getTime() - new Date().getTime();
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new KickTask(player.getName()), remaining / 50 + 10);
			plugin.sendLocaleMessage("TIME.REMAINING.JOIN", player, CrazyLightPluginInterface.DATETIMEFORMAT.format(exceed), ChatConverter.timeConverter(remaining / 1000, 2, player, 3, true));
		}
		else
		{
			plugin.sendLocaleMessage("TIME.EXCEEDED.JOIN", player, CrazyLightPluginInterface.DATETIMEFORMAT.format(exceed));
			addToMovementBlocker(player);
			final int autoKick = plugin.getAutoKick();
			if (autoKick != -1)
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new KickTask(player), autoKick * 20);
		}
	}

	@EventHandler
	public void PlayerQuit(final PlayerQuitEvent event)
	{
		PlayerQuit(event.getPlayer());
	}

	public void PlayerQuit(final Player player)
	{
		final PlayerTimeData playerdata = plugin.getPlayerData(player);
		if (playerdata != null)
			plugin.getCrazyDatabase().save(playerdata);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PlayerInventoryOpen(final InventoryOpenEvent event)
	{
		if (!(event.getPlayer() instanceof Player))
			return;
		final Player player = (Player) event.getPlayer();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
		player.closeInventory();
		plugin.requestActivation(player);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PlayerInventoryClick(final InventoryClickEvent event)
	{
		if (!(event.getWhoClicked() instanceof Player))
			return;
		final Player player = (Player) event.getWhoClicked();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
		player.closeInventory();
		plugin.requestActivation(player);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PlayerPickupItem(final PlayerPickupItemEvent event)
	{
		final Player player = event.getPlayer();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
		plugin.requestActivation(player);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PlayerDropItem(final PlayerDropItemEvent event)
	{
		final Player player = event.getPlayer();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
		plugin.requestActivation(player);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PlayerInteract(final PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
		plugin.requestActivation(player);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PlayerInteractEntity(final PlayerInteractEntityEvent event)
	{
		final Player player = event.getPlayer();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
		plugin.requestActivation(player);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void PlayerMove(final PlayerMoveEvent event)
	{
		final Player player = event.getPlayer();
		if (plugin.isActive(player))
			return;
		final Location current = movementBlocker.get(player.getName().toLowerCase());
		if (current == null)
			return;
		double dist = Double.MAX_VALUE;
		if (current.getWorld() == event.getTo().getWorld())
			dist = current.distance(event.getTo());
		if (dist >= 0.1)
		{
			event.setCancelled(true);
			plugin.requestActivation(event.getPlayer());
			if (dist >= 2)
				player.teleport(current, TeleportCause.PLUGIN);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void PlayerTeleport(final PlayerTeleportEvent event)
	{
		final Player player = event.getPlayer();
		if (plugin.isActive(player))
			return;
		if (movementBlocker.get(player.getName().toLowerCase()) == null)
			return;
		if (event.getCause() == TeleportCause.PLUGIN || event.getCause() == TeleportCause.UNKNOWN)
		{
			movementBlocker.put(player.getName().toLowerCase(), event.getTo().clone());
			return;
		}
		event.setCancelled(true);
		plugin.requestActivation(event.getPlayer());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PlayerHeal(final EntityRegainHealthEvent event)
	{
		if (!(event.getEntity() instanceof Player))
			return;
		final Player player = (Player) event.getEntity();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PlayerFood(final FoodLevelChangeEvent event)
	{
		if (!(event.getEntity() instanceof Player))
			return;
		final Player player = (Player) event.getEntity();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PlayerShear(final PlayerShearEntityEvent event)
	{
		if (!(event.getEntity() instanceof Player))
			return;
		final Player player = (Player) event.getEntity();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PlayerBedEnter(final PlayerBedEnterEvent event)
	{
		if (!(event.getPlayer() instanceof Player))
			return;
		final Player player = event.getPlayer();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PlayerFish(final PlayerFishEvent event)
	{
		if (!(event.getPlayer() instanceof Player))
			return;
		final Player player = event.getPlayer();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PlayerDamage(final EntityDamageEvent event)
	{
		if (!(event.getEntity() instanceof Player))
			return;
		final Player player = (Player) event.getEntity();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PlayerDamage(final EntityDamageByBlockEvent event)
	{
		if (!(event.getEntity() instanceof Player))
			return;
		final Player player = (Player) event.getEntity();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PlayerDamage(final EntityDamageByEntityEvent event)
	{
		if (!(event.getEntity() instanceof Player))
			return;
		final Player player = (Player) event.getEntity();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PlayerDamageDeal(final EntityDamageByEntityEvent event)
	{
		if (!(event.getDamager() instanceof Player))
			return;
		final Player player = (Player) event.getDamager();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	@Localized("CRAZYTIMECARD.COMMAND.EXPLOITWARN $Name$ $IP$ $Command$")
	public void PlayerPreCommand(final PlayerCommandPreprocessEvent event)
	{
		final Player player = event.getPlayer();
		if (plugin.isActive(player))
			return;
		final String message = event.getMessage().toLowerCase();
		if (message.startsWith("/"))
		{
			for (final String command : plugin.getCommandWhiteList())
				if (message.matches(command))
					return;
			event.setCancelled(true);
			final String IP = player.getAddress().getAddress().getHostAddress();
			plugin.requestActivation(player);
			// plugin.getCrazyLogger().log("CommandBlocked", player.getName() + " @ " + IP + " tried to execute", event.getMessage());
			plugin.broadcastLocaleMessage(true, "crazytimecard.warncommandexploits", "COMMAND.EXPLOITWARN", player.getName(), IP, event.getMessage().replaceAll("\\$", "_"));
		}
	}

	public void addToMovementBlocker(final Player player)
	{
		addToMovementBlocker(player.getName(), player.getLocation());
	}

	public void addToMovementBlocker(final String player, final Location location)
	{
		movementBlocker.put(player.toLowerCase(), location);
	}

	public boolean removeFromMovementBlocker(final OfflinePlayer player)
	{
		return removeFromMovementBlocker(player.getName());
	}

	public boolean removeFromMovementBlocker(final String player)
	{
		return movementBlocker.remove(player.toLowerCase()) != null;
	}

	public void clearMovementBlocker(final boolean guestsOnly)
	{
		if (guestsOnly)
		{
			for (final String name : movementBlocker.keySet())
				if (!plugin.hasPlayerData(name))
					movementBlocker.remove(name);
		}
		else
			movementBlocker.clear();
	}
}
