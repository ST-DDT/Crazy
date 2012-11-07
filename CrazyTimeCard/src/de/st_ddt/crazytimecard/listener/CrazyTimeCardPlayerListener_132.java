package de.st_ddt.crazytimecard.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;

import de.st_ddt.crazytimecard.CrazyTimeCard;

@SuppressWarnings("deprecation")
public class CrazyTimeCardPlayerListener_132 extends CrazyTimeCardPlayerListener
{

	public CrazyTimeCardPlayerListener_132(final CrazyTimeCard plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PaintingPlace(final PaintingPlaceEvent event)
	{
		final Player player = event.getPlayer();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
		plugin.requestActivation(player);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void PaintingBreak(final PaintingBreakByEntityEvent event)
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
