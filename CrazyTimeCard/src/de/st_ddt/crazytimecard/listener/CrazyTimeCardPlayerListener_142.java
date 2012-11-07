package de.st_ddt.crazytimecard.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;

import de.st_ddt.crazytimecard.CrazyTimeCard;

public class CrazyTimeCardPlayerListener_142 extends CrazyTimeCardPlayerListener
{

	public CrazyTimeCardPlayerListener_142(final CrazyTimeCard plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PaintingPlace(final HangingPlaceEvent event)
	{
		final Player player = event.getPlayer();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
		plugin.requestActivation(player);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PaintingBreak(final HangingBreakByEntityEvent event)
	{
		if (!(event.getRemover() instanceof Player))
			return;
		final Player player = (Player) event.getRemover();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
		plugin.requestActivation(player);
	}
}
