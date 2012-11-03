package de.st_ddt.crazyspawner.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public class SpawnTask implements Runnable, ConfigurationSaveable
{

	private final CrazySpawner plugin;
	private final EntityType type;
	private final Location location;
	private final int amount;
	private final long interval;
	private int repeat;
	private final int creatureMaxCount;
	private final double creatureRange;
	private final int playerMinCount;
	private final double playerRange;

	public SpawnTask(final CrazySpawner plugin, final EntityType type, final Location location, final int amount, final long interval, final int repeat, final int creatureMaxCount, final double creatureRange, final int playerMinCount, final double playerRange)
	{
		super();
		this.plugin = plugin;
		this.type = type;
		this.location = location;
		this.amount = creatureMaxCount > 0 ? Math.min(amount, creatureMaxCount) : amount;
		this.interval = interval;
		this.repeat = repeat;
		this.creatureMaxCount = creatureMaxCount;
		this.creatureRange = creatureRange;
		this.playerMinCount = playerMinCount;
		this.playerRange = playerRange;
	}

	public SpawnTask(CrazySpawner plugin, ConfigurationSection config)
	{
		super();
		this.plugin = plugin;
		this.type = EntityType.valueOf(config.getString("type"));
		this.location = ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection("location"), null);
		this.amount = config.getInt("amount", 1);
		this.interval = config.getLong("interval", 20);
		this.repeat = config.getInt("repeat", 0);
		this.creatureMaxCount = config.getInt("creatureMaxCount", 0);
		this.creatureRange = config.getDouble("creatureMaxCount", 16);
		this.playerMinCount = config.getInt("playerMinCount", 0);
		this.playerRange = config.getDouble("playerRange", 16);
	}

	@Override
	public void run()
	{
		if (checkPlayers())
		{
			final int amount = checkCreatures();
			final World world = location.getWorld();
			final Class<? extends Entity> typeClass = type.getEntityClass();
			for (int i = 0; i < amount; i++)
				world.spawn(location, typeClass);
			if (repeat > 0)
				repeat--;
			else if (repeat == 0)
			{
				plugin.removeSpawnTask(this);
				return;
			}
		}
		Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, this, interval);
	}

	public boolean checkPlayers()
	{
		int count = 0;
		for (final Player player : Bukkit.getOnlinePlayers())
			if (player.getWorld().equals(location.getWorld()))
				if (location.distance(player.getLocation()) < playerRange)
					count++;
		return count >= playerMinCount;
	}

	public int checkCreatures()
	{
		if (creatureMaxCount == 0)
			return amount;
		int count = amount;
		for (final Entity entity : location.getWorld().getEntitiesByClass(type.getEntityClass()))
			if (location.distance(entity.getLocation()) < creatureRange)
				count--;
		return count;
	}

	@Override
	public void save(ConfigurationSection config, String path)
	{
		if (repeat == 0)
			return;
		config.set(path + "type", type.toString());
		ObjectSaveLoadHelper.saveLocation(config, path + "location.", location);
		config.set(path + "amount", amount);
		config.set(path + "interval", interval);
		config.set(path + "repeat", repeat);
		config.set(path + "creatureMaxCount", creatureMaxCount);
		config.set(path + "creatureRange", creatureRange);
		config.set(path + "playerMinCount", playerMinCount);
		config.set(path + "playerRange", playerRange);
	}
}
