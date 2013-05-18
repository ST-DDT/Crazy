package de.st_ddt.crazyspawner.listener;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.MetadataValue;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.data.meta.AlarmMeta;
import de.st_ddt.crazyspawner.data.meta.PeacefulMeta;
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

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void ItemPickup(final PlayerPickupItemEvent event)
	{
		final Item item = event.getItem();
		final List<MetadataValue> metas = item.getMetadata(AlarmMeta.METAHEADER);
		for (final MetadataValue meta : metas)
			if (meta instanceof AlarmMeta)
			{
				final AlarmMeta alarm = (AlarmMeta) meta;
				final double alarmRange = alarm.asDouble();
				final Location location = item.getLocation();
				for (final LivingEntity nearby : item.getWorld().getEntitiesByClass(LivingEntity.class))
					if (location.distance(nearby.getLocation()) < alarmRange)
						nearby.removeMetadata(PeacefulMeta.METAHEADER, plugin);
				break;
			}
	}

	@EventHandler
	public void PlayerQuit(final PlayerQuitEvent event)
	{
		creatureSelection.remove(event.getPlayer());
	}
}
