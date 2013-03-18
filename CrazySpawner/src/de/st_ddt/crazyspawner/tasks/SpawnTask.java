package de.st_ddt.crazyspawner.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.ExtendedCreatureType;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.paramitrisable.ExtendedCreatureParamitrisable;

public class SpawnTask implements Runnable, ConfigurationSaveable, Comparable<SpawnTask>
{

	protected final CrazySpawner plugin;
	protected final ExtendedCreatureType type;
	protected final Location location;
	protected final int amount;
	protected final long interval;
	protected int repeat;
	protected final int chunkLoadRange;
	protected final int creatureMaxCount;
	protected final double creatureRange;
	protected final int playerMinCount;
	protected final double playerRange;
	protected final double blockingRange;
	protected final boolean allowDespawn;
	protected int taskID = -1;

	/**
	 * @param plugin
	 *            CrazySpawner
	 * @param type
	 *            The type to be spawned.
	 * @param location
	 *            The Location where to spawn the creature.
	 * @param amount
	 *            The maximum amount of creatures spawned at once per execution.
	 * @param interval
	 *            Repeat interval in ticks
	 * @param repeat
	 *            Reapeat it repeat times. (-1 = infinite)
	 * @param chunkLoadRange
	 *            Load chunks before executing this task. Monsters may not spawn otherwise.
	 * @param creatureMaxCount
	 *            Maximum allowed creatures of the given type. (Cuts amount).
	 * @param creatureRange
	 *            Search range for the given type.
	 * @param playerMinCount
	 *            If there aren't at least playerMinCount near this spawner it won't be executed.
	 * @param playerRange
	 *            The range where to search for nearby players for playerMinCount.
	 * @param blockingRange
	 *            This task won't be executed if a player is within this range.
	 */
	public SpawnTask(final CrazySpawner plugin, final ExtendedCreatureType type, final Location location, final int amount, final long interval, final int repeat, final int chunkLoadRange, final int creatureMaxCount, final double creatureRange, final int playerMinCount, final double playerRange, final double blockingRange, final boolean allowDespawn)
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
		this.chunkLoadRange = chunkLoadRange;
		this.creatureMaxCount = creatureMaxCount;
		this.creatureRange = creatureRange;
		this.playerMinCount = playerMinCount;
		this.playerRange = playerRange;
		this.blockingRange = blockingRange;
		this.allowDespawn = allowDespawn;
	}

	/**
	 * Constructor for tear respawn tasks
	 * 
	 * @param plugin
	 *            CrazySpawner
	 * @param type
	 *            The type to be spawned.
	 * @param location
	 *            The Location where to spawn the creature.
	 * @param interval
	 *            Repeat interval in ticks
	 * @param chunkLoadRange
	 *            Load chunks before executing this task. Monsters may not spawn otherwise.
	 * @param creatureRange
	 *            Search range for the given type.
	 */
	public SpawnTask(final CrazySpawner plugin, final ExtendedCreatureType type, final Location location, final long interval, final int chunkLoadRange, final double creatureRange)
	{
		this(plugin, type, location, 1, interval, -1, chunkLoadRange, 1, creatureRange, 0, 0, 0, false);
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
		this.chunkLoadRange = config.getInt("chunkLoadRange", -1);
		this.creatureMaxCount = Math.max(config.getInt("creatureMaxCount", 0), 0);
		this.creatureRange = Math.max(config.getDouble("creatureRange", 16), 0);
		this.playerMinCount = Math.max(config.getInt("playerMinCount", 0), 0);
		this.playerRange = Math.max(config.getDouble("playerRange", 16), 0);
		this.blockingRange = Math.max(config.getDouble("blockingRange", 0), 0);
		this.allowDespawn = config.getBoolean("allowDespawn", false);
	}

	public final void start()
	{
		start(1);
	}

	public void start(final long delay)
	{
		if (taskID == -1)
			taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, delay, interval);
	}

	public void cancel()
	{
		if (taskID != -1)
		{
			Bukkit.getScheduler().cancelTask(taskID);
			taskID = -1;
		}
	}

	@Override
	public void run()
	{
		if (checkPlayers())
		{
			final World world = location.getWorld();
			final Chunk chunk = world.getChunkAt(location);
			final int cX = chunk.getX();
			final int cZ = chunk.getZ();
			if (chunkLoadRange >= 0)
				for (int x = -chunkLoadRange; x <= chunkLoadRange; x++)
					for (int z = -chunkLoadRange; z <= chunkLoadRange; z++)
						world.loadChunk(cX + x, cZ + z, false);
			final int amount = checkCreatures();
			for (int i = 0; i < amount; i++)
			{
				final Entity entity = type.spawn(location);
				if (entity instanceof LivingEntity)
					((LivingEntity) entity).setRemoveWhenFarAway(allowDespawn);
			}
			if (amount > 0)
				if (repeat > 0)
					repeat--;
				else if (repeat == 0)
				{
					plugin.removeSpawnTask(this);
					cancel();
				}
			if (chunkLoadRange >= 0)
				for (int x = -chunkLoadRange; x <= chunkLoadRange; x++)
					for (int z = -chunkLoadRange; z <= chunkLoadRange; z++)
						world.unloadChunkRequest(x, z, true);
		}
	}

	protected boolean checkPlayers()
	{
		int count = 0;
		for (final Player player : Bukkit.getOnlinePlayers())
			if (player.getWorld().equals(location.getWorld()))
			{
				final double distance = location.distance(player.getLocation());
				if (distance < blockingRange)
					return false;
				if (distance < playerRange)
					count++;
			}
		return count >= playerMinCount;
	}

	protected int checkCreatures()
	{
		if (creatureMaxCount == 0)
			return amount;
		int count = creatureMaxCount;
		for (final Entity entity : type.getEntities(location.getWorld()))
			if (location.distance(entity.getLocation()) < creatureRange)
				count--;
		return Math.min(count, amount);
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
		config.set(path + "chunkLoadRange", chunkLoadRange);
		config.set(path + "creatureMaxCount", creatureMaxCount);
		config.set(path + "creatureRange", creatureRange);
		config.set(path + "playerMinCount", playerMinCount);
		config.set(path + "playerRange", playerRange);
		config.set(path + "blockingRange", blockingRange);
		config.set(path + "allowDespawn", allowDespawn);
	}

	@Override
	public String toString()
	{
		return "SpawnTask {Type=" + type + "; " + location + "}";
	}

	@Override
	public int compareTo(final SpawnTask o)
	{
		int res = location.getWorld().getName().compareToIgnoreCase(o.location.getWorld().getName());
		if (res == 0)
			res = type.getName().compareTo(o.type.getName());
		if (res == 0)
			res = Integer.valueOf(super.hashCode()).compareTo(o.hashCode());
		return res;
	}
}
