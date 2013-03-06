package de.st_ddt.crazyspawner.listener;

import java.util.Map;

import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.source.Localized;

public class PlayerListener implements Listener
{

	private final CrazySpawner plugin;
	private final Map<Player, EntityType> creatureSelection;

	public PlayerListener(final CrazySpawner plugin, final Map<Player, EntityType> creatureSelection)
	{
		super();
		this.plugin = plugin;
		this.creatureSelection = creatureSelection;
	}

	@EventHandler(ignoreCancelled = true)
	@Localized("CRAZYSPAWNER.COMMAND.CREATURESPAWNER.APPLIED $Creature$")
	public void PlayerInteract(final PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		final BlockState block = event.getClickedBlock().getState();
		if (!(block instanceof CreatureSpawner))
			return;
		final Player player = event.getPlayer();
		final EntityType creature = creatureSelection.remove(player);
		if (creature == null)
			return;
		final CreatureSpawner spawner = (CreatureSpawner) block;
		spawner.setSpawnedType(creature);
		spawner.update();
		event.setCancelled(true);
		plugin.sendLocaleMessage("COMMAND.CREATURESPAWNER.APPLIED", player, creature.toString());
	}

	@EventHandler
	public void PlayerQuit(final PlayerQuitEvent event)
	{
		creatureSelection.remove(event.getPlayer());
	}
}
