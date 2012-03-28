package de.st_ddt.crazyarena.utils;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class SpawnList extends ArrayList<Location>
{

	protected final World world;
	private static final long serialVersionUID = -4995034097544178441L;

	public SpawnList(World world)
	{
		super();
		this.world = world;
	}

	public void teleport(LivingEntity entity)
	{
		entity.teleport(randomSpawn(), TeleportCause.PLUGIN);
	}

	public void spawn(EntityType type, int amount)
	{
		for (int i = 0; i < amount; i++)
			world.spawnCreature(randomSpawn(), type);
	}

	public Location randomSpawn()
	{
		return get((int) (Math.random() * size()));
	}
}
