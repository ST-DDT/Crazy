package de.st_ddt.crazyspawner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CrazySpawnerSpawnTask implements Runnable
{

	protected final JavaPlugin plugin;
	protected final Location location;
	protected final EntityType creature;
	protected final int amount;
	protected final long interval;
	protected int repeat;
	protected final double playerradius;
	protected final int playercount;

	public CrazySpawnerSpawnTask(final JavaPlugin plugin, final Location location, final EntityType creature, final int amount, final long interval, final int repeat, final double playerradius, final int playercount)
	{
		super();
		this.plugin = plugin;
		this.location = location;
		this.creature = creature;
		this.amount = amount;
		this.interval = interval;
		this.repeat = repeat;
		this.playerradius = playerradius;
		this.playercount = playercount;
	}

	@Override
	public void run()
	{
		if (playercount > 0)
		{
			int remaining = playercount;
			for (final Player player : Bukkit.getOnlinePlayers())
				if (player.getWorld().equals(location.getWorld()))
					if (location.distance(player.getLocation()) < playerradius)
						if (remaining > 0)
							remaining--;
						else
							break;
			if (remaining != 0)
				return;
		}
		if (repeat != 0)
		{
			if (repeat != -1)
				repeat--;
			Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, this, interval);
		}
		for (int i = 0; i < amount; i++)
			location.getWorld().spawnEntity(location, creature);
	}
}
