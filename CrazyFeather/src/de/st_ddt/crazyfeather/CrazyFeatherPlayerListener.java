package de.st_ddt.crazyfeather;

import java.util.Date;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class CrazyFeatherPlayerListener implements Listener
{

	protected final CrazyFeather plugin;
	protected HashMap<Entity, Date> cooldown = new HashMap<Entity, Date>();

	public CrazyFeatherPlayerListener(CrazyFeather plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void PlayerInteract(PlayerInteractEntityEvent event)
	{
		Entity entity = event.getRightClicked();
		Player player = event.getPlayer();
		if (entity.getType() != EntityType.CHICKEN)
			return;
		if (player.getItemInHand().getType() != Material.SHEARS)
			return;
		for (Entity alive : cooldown.keySet())
			if (alive.isDead())
				cooldown.remove(alive);
		Date date = cooldown.get(entity);
		if (date != null)
			if (date.after(new Date()))
				return;
		player.getWorld().dropItem(entity.getLocation(), new ItemStack(Material.FEATHER));
		cooldown.put(entity, new Date(new Date().getTime() + 3 * 60 * 1000));
	}
}
