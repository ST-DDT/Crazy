package de.st_ddt.crazypunisher;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CrazyPunisherPlayerListener implements Listener
{

	private final CrazyPunisher plugin;

	public CrazyPunisherPlayerListener(CrazyPunisher plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler
	public void PlayerChat(PlayerChatEvent event)
	{
		if (!plugin.isJailed(event.getPlayer()))
			return;
		event.setCancelled(true);
		plugin.sendLocaleMessage("MESSAGE.JAILED", event.getPlayer());
	}

	@EventHandler
	public void PlayerDropItem(PlayerDropItemEvent event)
	{
		if (!plugin.isJailed(event.getPlayer()))
			return;
		event.setCancelled(true);
		plugin.sendLocaleMessage("MESSAGE.JAILED", event.getPlayer());
	}

	@EventHandler
	public void PlayerInteract(PlayerInteractEvent event)
	{
		if (!plugin.isJailed(event.getPlayer()))
			return;
		event.setCancelled(true);
		plugin.sendLocaleMessage("MESSAGE.JAILED", event.getPlayer());
	}

	@EventHandler
	public void PlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if (!plugin.isJailed(event.getPlayer()))
			return;
		event.setCancelled(true);
		plugin.sendLocaleMessage("MESSAGE.JAILED", event.getPlayer());
	}

	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		boolean hidden = plugin.isHidden(player);
		for (Player plr : plugin.getServer().getOnlinePlayers())
			if ((plr.hasPermission("crazypunisher.showall") || plugin.isHidden(plr)) && hidden)
			{
				player.showPlayer(plr);
				plr.showPlayer(player);
			}
			else if (hidden)
			{
				player.showPlayer(plr);
				plr.hidePlayer(player);
			}
			else
			{
				plr.showPlayer(player);
			}
		if (!plugin.isJailed(player))
		{
			if (plugin.isInsideJail(player.getLocation()))
			{
				Location target = player.getBedSpawnLocation();
				if (target == null)
					target = player.getWorld().getSpawnLocation();
				if (target != null)
					player.teleport(target, TeleportCause.PLUGIN);
			}
			return;
		}
		plugin.sendLocaleMessage("MESSAGE.JAILEDUNTIL", event.getPlayer(), plugin.getJailTime(event.getPlayer()));
	}

	@EventHandler
	public void PlayerMove(PlayerMoveEvent event)
	{
		if (!plugin.isJailed(event.getPlayer()))
			return;
		if (!plugin.isInsideJail(event.getTo()))
			plugin.keepJailed(event.getPlayer());
	}

	@EventHandler
	public void PlayerTeleport(PlayerTeleportEvent event)
	{
		if (!plugin.isJailed(event.getPlayer()))
			return;
		if (!plugin.isInsideJail(event.getTo()))
			event.setCancelled(true);
	}

	@EventHandler
	public void PlayerRespawn(PlayerRespawnEvent event)
	{
		if (!plugin.isJailed(event.getPlayer()))
			return;
		plugin.keepJailed(event.getPlayer());
	}

	@EventHandler
	public void PlayerLogin(PlayerLoginEvent event)
	{
		Player player = event.getPlayer();
		if (!plugin.isBanned(player))
			return;
		if (plugin.isAutoBanIPEnabled())
			plugin.getServer().banIP(player.getAddress().getAddress().getHostAddress());
		player.setBanned(true);
		event.setKickMessage("You are banned!");
		event.setResult(Result.KICK_BANNED);
	}

	@EventHandler
	public void PlayerDamage(EntityDamageByEntityEvent event)
	{
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		if (plugin.isJailed(player))
		{
			event.setCancelled(true);
			return;
		}
		if (!(event.getDamager() instanceof Player))
			return;
		player = (Player) event.getDamager();
		if (plugin.isJailed(player))
		{
			event.setCancelled(true);
			plugin.sendLocaleMessage("MESSAGE.JAILED", player);
			return;
		}
	}

	@EventHandler
	public void PlayerDamage(EntityDamageByBlockEvent event)
	{
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		if (!plugin.isJailed(player))
			return;
		Location location = player.getBedSpawnLocation();
		if (location == null)
			location = player.getWorld().getSpawnLocation();
		player.teleport(location, TeleportCause.PLUGIN);
		event.setCancelled(true);
	}
}
