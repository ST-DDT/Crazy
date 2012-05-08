package de.st_ddt.crazyarena.spleef;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;

import de.st_ddt.crazyarena.participants.Participant;
import de.st_ddt.crazyarena.participants.ParticipantType;

public class ArenaSpleefBlockListener implements Listener
{

	private final ArenaSpleef arena;

	public ArenaSpleefBlockListener(ArenaSpleef arena)
	{
		this.arena = arena;
	}

	public ArenaSpleef getArena()
	{
		return arena;
	}

	@EventHandler
	public void BlockBreakEvent(BlockBreakEvent event)
	{
		if (!arena.getRegion().isInside(event.getBlock().getLocation()) || arena.getEditMode())
			return;
		if (arena.getArena().isInside(event.getBlock().getLocation()) && arena.isRunning())
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void BlockBurnEvent(BlockBurnEvent event)
	{
		if (!arena.getRegion().isInside(event.getBlock().getLocation()) || arena.getEditMode())
			return;
		if (arena.getArena().isInside(event.getBlock().getLocation()) && arena.isRunning())
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void BlockCanBuildEvent(BlockCanBuildEvent event)
	{
		if (!arena.getRegion().isInside(event.getBlock().getLocation()) || arena.getEditMode())
			return;
		if (arena.getArena().isInside(event.getBlock().getLocation()) && arena.isRunning())
			return;
		event.setBuildable(false);
	}

	@EventHandler
	public void BlockDamageEvent(BlockDamageEvent event)
	{
		if (!arena.getRegion().isInside(event.getBlock().getLocation()) || arena.getEditMode())
			return;
		if (arena.getArena().isInside(event.getBlock().getLocation()) && arena.isRunning())
		{
			Player player = (Player) event.getPlayer();
			Participant participant = arena.getParticipant(player);
			if (participant != null)
				if (participant.getParticipantType() == ParticipantType.PARTICIPANT)
				{
					event.getBlock().setType(Material.AIR);
					return;
				}
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void BlockFadeEvent(BlockFadeEvent event)
	{
		if (!arena.getRegion().isInside(event.getBlock().getLocation()) || arena.getEditMode())
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void BlockFormEvent(BlockFormEvent event)
	{
		if (!arena.getRegion().isInside(event.getBlock().getLocation()) || arena.getEditMode())
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void BlockFromToEvent(BlockFromToEvent event)
	{
		if (!arena.getRegion().isInside(event.getBlock().getLocation()) || arena.getEditMode())
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void BlockGrowEvent(BlockGrowEvent event)
	{
		if (!arena.getRegion().isInside(event.getBlock().getLocation()) || arena.getEditMode())
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void BlockIgniteEvent(BlockIgniteEvent event)
	{
		if (!arena.getRegion().isInside(event.getBlock().getLocation()) || arena.getEditMode())
			return;
		if (arena.getArena().isInside(event.getBlock().getLocation()) && arena.isRunning())
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void BlockPlaceEvent(BlockPlaceEvent event)
	{
		if (!arena.getRegion().isInside(event.getBlock().getLocation()) || arena.getEditMode())
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void BlockSpreadEvent(BlockSpreadEvent event)
	{
		if (!arena.getRegion().isInside(event.getBlock().getLocation()) || arena.getEditMode())
			return;
		if (arena.getArena().isInside(event.getBlock().getLocation()) && arena.isRunning())
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void LeavesDecayEvent(LeavesDecayEvent event)
	{
		if (!arena.getRegion().isInside(event.getBlock().getLocation()) || arena.getEditMode())
			return;
		if (arena.getArena().isInside(event.getBlock().getLocation()) && arena.isRunning())
			return;
		event.setCancelled(true);
	}
}
