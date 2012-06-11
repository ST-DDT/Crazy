package de.st_ddt.crazycompass;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CrazyCompassPlayerListener implements Listener
{

	String chatHeader = ChatColor.RED + "[" + ChatColor.GREEN + "CrazyCompass" + ChatColor.RED + "] " + ChatColor.WHITE;

	@EventHandler
	public void PlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		if (event.getMaterial() != Material.COMPASS)
			return;
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR))
		{
			Player nearest = null;
			double distance = Double.MAX_VALUE;
			for (Player player2 : Bukkit.getServer().getOnlinePlayers())
				if (player2 != player)
				{
					double dist = player.getLocation().distance(player2.getLocation());
					if (dist < distance)
					{
						distance = dist;
						nearest = player2;
					}
				}
			if (nearest == null)
			{
				player.sendMessage(chatHeader + "Noone to target at");
				return;
			}
			player.setCompassTarget(nearest.getLocation());
			player.sendMessage(chatHeader + "Compass targets at " + nearest.getDisplayName() + " (" + Math.round(distance) + " m)");
		}
		else
		{
			player.setCompassTarget(player.getWorld().getSpawnLocation());
			player.sendMessage(chatHeader + "Compass targets at " + player.getWorld().getName() + "'s Spawn (" + Math.round(player.getLocation().distance(player.getWorld().getSpawnLocation())) + " m)");
		}
	}
}
