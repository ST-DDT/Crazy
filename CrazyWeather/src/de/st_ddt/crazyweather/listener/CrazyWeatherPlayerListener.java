package de.st_ddt.crazyweather.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.st_ddt.crazyweather.CrazyWeather;
import de.st_ddt.crazyweather.events.CrazyWeatherPreThunderToolCastEvent;

public class CrazyWeatherPlayerListener implements Listener
{

	protected final CrazyWeather plugin;

	public CrazyWeatherPlayerListener(final CrazyWeather plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler
	public void PlayerInteract(final PlayerInteractEvent event)
	{
		if ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.isCancelled()) || event.getAction().equals(Action.RIGHT_CLICK_AIR))
		{
			final int tool = plugin.getThunderTool();
			if (tool == -1)
				return;
			final Player player = event.getPlayer();
			if (tool != player.getItemInHand().getTypeId())
				return;
			if (!player.hasPermission("crazyweather.thunder.tool") && !player.hasPermission("crazyweather." + player.getWorld().getName() + ".thunder.tool"))
				return;
			final Location location = player.getTargetBlock(null, 1024).getLocation();
			final CrazyWeatherPreThunderToolCastEvent cast = new CrazyWeatherPreThunderToolCastEvent(player, location);
			cast.callEvent();
			if (cast.isCancelled())
				return;
			plugin.strikeTarget(player, location);
			plugin.getCrazyLogger().log("ThunderStrike", player.getName() + " send a thunderstrike to " + location.getWorld().getName() + " (Thundertool)");
			event.setCancelled(true);
		}
	}
}
