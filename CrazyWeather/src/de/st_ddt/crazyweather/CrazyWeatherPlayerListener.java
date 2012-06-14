package de.st_ddt.crazyweather;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.st_ddt.crazyweather.events.CrazyWeatherPreThunderToolCastEvent;

public class CrazyWeatherPlayerListener implements Listener
{

	protected final CrazyWeather plugin;

	public CrazyWeatherPlayerListener(CrazyWeather plugin)
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
			Player player = event.getPlayer();
			if (tool != -1)
				if (tool != player.getItemInHand().getTypeId())
					return;
			if (!player.hasPermission("crazyweather.thunder.tool"))
				return;
			Location location = player.getTargetBlock(null, 1024).getLocation();
			CrazyWeatherPreThunderToolCastEvent cast = new CrazyWeatherPreThunderToolCastEvent(plugin, player, location);
			Bukkit.getPluginManager().callEvent(cast);
			if (cast.isCancelled())
				return;
			event.getPlayer().getWorld().strikeLightning(location);
			event.setCancelled(true);
		}
	}
}
