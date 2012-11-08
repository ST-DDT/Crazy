package de.st_ddt.crazyspawner.listener;

import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazySpawnerPlayerListener implements Listener
{

	private final CrazySpawner plugin;
	private final Map<String, EntityType> creatureSelection;

	public CrazySpawnerPlayerListener(final CrazySpawner plugin, final Map<String, EntityType> creatureSelection)
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
		final Block block = event.getClickedBlock();
		if (!(block instanceof CreatureSpawner))
			return;
		final Player player = event.getPlayer();
		final CreatureSpawner spawner = (CreatureSpawner) block;
		final EntityType creature = creatureSelection.remove(player.getName());
		spawner.setSpawnedType(creature);
		spawner.update();
		plugin.sendLocaleMessage("COMMAND.CREATURESPAWNER.APPLIED", player, creature.toString());
	}
}
