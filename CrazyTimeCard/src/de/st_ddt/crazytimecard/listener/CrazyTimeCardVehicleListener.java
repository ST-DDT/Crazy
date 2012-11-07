package de.st_ddt.crazytimecard.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import de.st_ddt.crazytimecard.CrazyTimeCard;

public final class CrazyTimeCardVehicleListener implements Listener
{

	private final CrazyTimeCard plugin;

	public CrazyTimeCardVehicleListener(final CrazyTimeCard plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler
	public void VehicleEnter(final VehicleEnterEvent event)
	{
		if (!(event.getVehicle().getPassenger() instanceof Player))
			return;
		final Player player = (Player) event.getVehicle().getPassenger();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
		plugin.requestActivation(player);
	}

	@EventHandler
	public void VehicleDamage(final VehicleDamageEvent event)
	{
		if (!(event.getAttacker() instanceof Player))
			return;
		final Player player = (Player) event.getAttacker();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
		plugin.requestActivation(player);
	}

	@EventHandler
	public void VehicleDestroy(final VehicleDestroyEvent event)
	{
		if (!(event.getAttacker() instanceof Player))
			return;
		final Player player = (Player) event.getAttacker();
		if (plugin.isActive(player))
			return;
		event.setCancelled(true);
		plugin.requestActivation(player);
	}
}
