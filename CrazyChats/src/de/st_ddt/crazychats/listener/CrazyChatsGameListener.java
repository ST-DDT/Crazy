package de.st_ddt.crazychats.listener;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazychats.channels.WorldChannel;

public class CrazyChatsGameListener implements Listener
{

	protected final CrazyChats plugin;

	public CrazyChatsGameListener(final CrazyChats plugin)
	{
		super();
		this.plugin = plugin;
	}

	@EventHandler
	public void WorldLoad(final WorldLoadEvent event)
	{
		final World world = event.getWorld();
		plugin.getWorldChannels().put(world.getName(), new WorldChannel(world));
	}

	@EventHandler
	public void WorldUnload(final WorldUnloadEvent event)
	{
		plugin.getWorldChannels().remove(event.getWorld().getName());
	}
}
