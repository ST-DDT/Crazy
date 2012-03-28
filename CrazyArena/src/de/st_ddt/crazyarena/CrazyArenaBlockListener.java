package de.st_ddt.crazyarena;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import de.st_ddt.crazyarena.arenas.Arena;

public class CrazyArenaBlockListener implements Listener
{

	@EventHandler
	public void SignChange(SignChangeEvent e)
	{
		if (e.getLine(0).equalsIgnoreCase("[ca]"))
		{
			Player player=e.getPlayer();
			if (!player.hasPermission("crazyarena.arena.sign"))
			{
				e.setCancelled(true);
				CrazyArena.getPlugin().sendLocaleMessage("ARENA.SIGN.NOPERMISSION", player);
			}
			Arena arena = CrazyArena.getPlugin().getArenas().getArena(e.getLine(1));
			if (arena == null)
			{
				e.setLine(1, "not found");
				return;
			}
			arena.addSign(player, e.getBlock());
		}
	}
}
