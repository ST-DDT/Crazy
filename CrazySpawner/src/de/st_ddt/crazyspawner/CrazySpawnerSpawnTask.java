package de.st_ddt.crazyspawner;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;

@SuppressWarnings("deprecation")
public class CrazySpawnerSpawnTask implements Runnable
{

	protected Location location;
	protected CreatureType creature;
	protected int amount;

	public CrazySpawnerSpawnTask(final Location location, final CreatureType creature, final int amount)
	{
		super();
		this.location = location;
		this.creature = creature;
		this.amount = amount;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < amount; i++)
			location.getWorld().spawnCreature(location, creature);
	}
}
