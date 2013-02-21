package de.st_ddt.crazyspawner.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.ExtendedCreatureType;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.paramitrisable.ExtendedCreatureParamitrisable;

public class SpawnTask implements Runnable, ConfigurationSaveable
{

	private final CrazySpawner plugin;
	private final ExtendedCreatureType type;
	private final Location location;
	private final int amount;
	private final long interval;
	private int repeat;
	private final int creatureMaxCount;
	private final double creatureRange;
	private final int playerMinCount;
	private final double playerRange;

	public SpawnTask(final CrazySpawner plugin, final ExtendedCreatureType type, final Location location, final int amount, final long interval, final int repeat, final int creatureMaxCount, final double creatureRange, final int playerMinCount, final double playerRange)
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

	public SpawnTask(final CrazySpawner plugin, final ConfigurationSection config)
	{
		super();
		this.plugin = plugin;
		this.type = ExtendedCreatureParamitrisable.CREATURE_TYPES.get(config.getString("type").toUpperCase());
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
			for (int i = 0; i < amount; i++)
				type.spawn(location);
			if (amount > 0)
				if (repeat > 0)
					repeat--;
				else if (repeat == 0)
				{
					plugin.removeSpawnTask(this);
					return;
				}
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, interval);
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
		for (final Entity entity : type.getEntities(location.getWorld()))
			if (location.distance(entity.getLocation()) < creatureRange)
				count--;
		return count;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
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
