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

	protected final CrazySpawner plugin;
	protected final ExtendedCreatureType type;
	protected final Location location;
	protected final int amount;
	protected final long interval;
	protected int repeat;
	protected final int creatureMaxCount;
	protected final double creatureRange;
	protected final int playerMinCount;
	protected final double playerRange;
	protected int taskID = -1;

	public SpawnTask(final CrazySpawner plugin, final ExtendedCreatureType type, final Location location, final int amount, final long interval, final int repeat, final int creatureMaxCount, final double creatureRange, final int playerMinCount, final double playerRange)
	{
		super();
		this.plugin = plugin;
		this.type = type;
		if (type == null)
			throw new IllegalArgumentException("Type cannot be null!");
		this.location = location;
		if (location == null)
			throw new IllegalArgumentException("Location cannot be null!");
		if (location.getWorld() == null)
			throw new IllegalArgumentException("Location.world cannot be null!");
		this.amount = creatureMaxCount > 0 ? Math.min(amount, creatureMaxCount) : amount;
		this.interval = Math.max(interval, 1);
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
		if (type == null)
			throw new IllegalArgumentException("Type " + config.getString("type") + " wasn't found!");
		this.location = ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection("location"), null);
		if (location == null)
			throw new IllegalArgumentException("Location cannot be null!");
		if (location.getWorld() == null)
			throw new IllegalArgumentException("Location.world cannot be null!");
		this.amount = Math.max(config.getInt("amount", 1), 1);
		this.interval = Math.max(config.getLong("interval", 20), 1);
		this.repeat = Math.max(config.getInt("repeat", 0), -1);
		this.creatureMaxCount = Math.max(config.getInt("creatureMaxCount", 0), 0);
		this.creatureRange = Math.max(config.getDouble("creatureRange", 16), 0);
		this.playerMinCount = Math.max(config.getInt("playerMinCount", 0), 0);
		this.playerRange = Math.max(config.getDouble("playerRange", 16), 0);
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

	protected boolean checkPlayers()
	{
		int count = 0;
		for (final Player player : Bukkit.getOnlinePlayers())
			if (player.getWorld().equals(location.getWorld()))
				if (location.distance(player.getLocation()) < playerRange)
					count++;
		return count >= playerMinCount;
	}

	protected int checkCreatures()
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
		config.set(path + "type", type.getName());
		ObjectSaveLoadHelper.saveLocation(config, path + "location.", location, true, false);
		config.set(path + "amount", amount);
		config.set(path + "interval", interval);
		config.set(path + "repeat", repeat);
		config.set(path + "creatureMaxCount", creatureMaxCount);
		config.set(path + "creatureRange", creatureRange);
		config.set(path + "playerMinCount", playerMinCount);
		config.set(path + "playerRange", playerRange);
	}
}
