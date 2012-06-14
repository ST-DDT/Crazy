package de.st_ddt.crazyweather;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CrazyWeatherPlayerListener implements Listener
{

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		if ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.isCancelled()) || event.getAction().equals(Action.RIGHT_CLICK_AIR))
		{
			final int tool = CrazyWeather.getPlugin().getTool();
			if (tool != -1)
				if (tool != event.getPlayer().getItemInHand().getTypeId())
					return;
			if (!event.getPlayer().hasPermission("crazyweather.thunder.tool"))
				return;
			event.getPlayer().getWorld().strikeLightning(event.getPlayer().getTargetBlock(null, 1024).getLocation());
			event.setCancelled(true);
		}
	}
}
