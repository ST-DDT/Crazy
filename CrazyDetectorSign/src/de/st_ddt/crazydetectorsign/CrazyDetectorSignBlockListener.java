package de.st_ddt.crazydetectorsign;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

public class CrazyDetectorSignBlockListener implements Listener
{

	protected final CrazyDetectorSign plugin;

	public CrazyDetectorSignBlockListener(final CrazyDetectorSign plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void SignChangeEvent(final SignChangeEvent event)
	{
		if (!event.getLine(0).equals("[CD]"))
			return;
		if (event.getBlock().getType() != Material.WALL_SIGN)
			return;
		plugin.registerSign(event.getBlock(), event.getLines());
	}

	@EventHandler
	public void BlockBreakEvent(final BlockBreakEvent event)
	{
		if (event.getBlock().getType() != Material.WALL_SIGN)
			return;
		plugin.unregisterSign(event.getBlock().getLocation());
	}
}
