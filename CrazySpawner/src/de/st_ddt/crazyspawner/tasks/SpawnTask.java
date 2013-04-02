package de.st_ddt.crazyspawner.tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.data.CreatureMeta;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.ExtendedCreatureType;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.VersionComparator;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.paramitrisable.ExtendedCreatureParamitrisable;

public class SpawnTask implements Runnable, ConfigurationSaveable, Comparable<SpawnTask>
{

	protected final static boolean v146OrLater = VersionComparator.compareVersions(ChatHelper.getMinecraftVersion(), "1.4.6") >= 0;
	protected final static boolean v15OrLater = VersionComparator.compareVersions(ChatHelper.getMinecraftVersion(), "1.5") >= 0;
	protected final static Random RANDOM = new Random();
	protected final List<BukkitTask> tasks = new LinkedList<BukkitTask>();
	protected final CrazySpawner plugin;
	protected final ExtendedCreatureType type;
	protected final Location location;
	protected final double spawnRange;
	protected final int amount;
	protected final long interval;
	protected final boolean synced;
	protected int repeat;
	protected final int chunkLoadRange;
	protected final int creatureMaxCount;
	protected final double creatureRange;
	protected final int playerMinCount;
	protected final double playerRange;
	protected final double blockingRange;
	protected final List<Long> countDownTimes;
	protected final String countDownMessage;
	protected final boolean countDownBroadcast;
	protected final boolean allowDespawn;
	protected final int health;
	protected final boolean showHealth;

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
	public SpawnTask(final CrazySpawner plugin, final ExtendedCreatureType type, final Location location, final double spawnRange, final int amount, final long interval, final int repeat, final boolean synced, final int chunkLoadRange, final int creatureMaxCount, final double creatureRange, final int playerMinCount, final double playerRange, final double blockingRange, final List<Long> countDownTimes, final String countDownMessage, final boolean countDownBroadcast, final boolean allowDespawn, final int health, final boolean showHealth)
	{
		super();
		this.plugin = plugin;
		this.type = type;
		this.location = location;
		Validate.notNull(this.type, "Type cannot be null!");
		Validate.notNull(this.location, "Location cannot be null!");
		Validate.notNull(this.location.getWorld(), "Location.world cannot be null!");
		this.spawnRange = Math.max(spawnRange, 0);
		this.amount = creatureMaxCount > 0 ? Math.min(amount, creatureMaxCount) : amount;
		this.interval = Math.max(interval, 1);
		this.repeat = repeat;
		this.synced = synced;
		this.chunkLoadRange = chunkLoadRange;
		this.creatureMaxCount = creatureMaxCount;
		this.creatureRange = creatureRange;
		this.playerMinCount = playerMinCount;
		this.playerRange = playerRange;
		this.blockingRange = blockingRange;
		if (countDownTimes == null)
			this.countDownTimes = new ArrayList<Long>(0);
		else
			this.countDownTimes = countDownTimes;
		clearCountDownTimes();
		this.countDownMessage = countDownMessage;
		this.countDownBroadcast = countDownMessage != null && countDownBroadcast;
		this.allowDespawn = allowDespawn;
		this.health = v146OrLater ? health : -1;
		this.showHealth = v15OrLater ? showHealth : false;
	}

	public SpawnTask(final CrazySpawner plugin, final ExtendedCreatureType type, final Location location, final double spawnRange, final int amount, final long interval, final int repeat, final boolean synced, final int chunkLoadRange, final int creatureMaxCount, final double creatureRange, final int playerMinCount, final double playerRange, final double blockingRange, final Long[] countDownTimes, final String countDownMessage, final boolean countDownBroadcast, final boolean allowDespawn, final int health, final boolean showHealth)
	{
		this(plugin, type, location, spawnRange, amount, interval, repeat, synced, chunkLoadRange, creatureMaxCount, creatureRange, playerMinCount, playerRange, blockingRange, new ArrayList<Long>(countDownTimes == null ? 0 : countDownTimes.length), countDownMessage, countDownBroadcast, allowDespawn, health, showHealth);
		if (countDownTimes != null)
			for (final Long time : countDownTimes)
				this.countDownTimes.add(time);
		clearCountDownTimes();
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
	public SpawnTask(final CrazySpawner plugin, final ExtendedCreatureType type, final Location location, final long interval, final int chunkLoadRange, final Long[] countDownTimes, final String countDownMessage, final double creatureRange)
	{
		this(plugin, type, location, 0, 1, interval, -1, true, chunkLoadRange, 1, creatureRange, 0, 0, 0, countDownTimes, countDownMessage, countDownMessage != null, false, -1, false);
	}

	public SpawnTask(final CrazySpawner plugin, final ConfigurationSection config)
	{
		super();
		this.plugin = plugin;
		this.type = ExtendedCreatureParamitrisable.CREATURE_TYPES.get(config.getString("type").toUpperCase());
		this.location = ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection("location"), null);
		Validate.notNull(this.type, "Type cannot be null!");
		Validate.notNull(this.location, "Location cannot be null!");
		Validate.notNull(this.location.getWorld(), "Location.world cannot be null!");
		this.spawnRange = Math.max(0, config.getDouble("spawnRange", 0));
		this.amount = Math.max(config.getInt("amount", 1), 1);
		this.interval = Math.max(config.getLong("interval", 20), 1);
		this.repeat = Math.max(config.getInt("repeat", 0), -1);
		this.synced = config.getBoolean("synced", false);
		this.chunkLoadRange = config.getInt("chunkLoadRange", -1);
		this.creatureMaxCount = Math.max(config.getInt("creatureMaxCount", 0), 0);
		this.creatureRange = Math.max(config.getDouble("creatureRange", 16), 0);
		this.playerMinCount = Math.max(config.getInt("playerMinCount", 0), 0);
		this.playerRange = Math.max(config.getDouble("playerRange", 16), 0);
		this.blockingRange = Math.max(config.getDouble("blockingRange", 0), 0);
		final List<Long> countDownTimes = config.getLongList("countDownTimes");
		if (countDownTimes == null)
			this.countDownTimes = new ArrayList<Long>(0);
		else
			this.countDownTimes = countDownTimes;
		clearCountDownTimes();
		final String message = config.getString("countDownMessage");
		if (message == null)
			this.countDownMessage = null;
		else
			this.countDownMessage = ChatHelper.colorise(message);
		this.countDownBroadcast = countDownMessage != null && this.countDownTimes.size() > 0 ? config.getBoolean("countDownBroadcast", false) : false;
		this.allowDespawn = config.getBoolean("allowDespawn", false);
		this.health = v146OrLater ? config.getInt("health", -1) : -1;
		this.showHealth = v15OrLater ? config.getBoolean("showHealth", false) : false;
	}

	private void clearCountDownTimes()
	{
		final Iterator<Long> it = countDownTimes.iterator();
		while (it.hasNext())
			if (it.next() > interval)
				it.remove();
	}

	private String timeConverter(long time)
	{
		final StringBuilder res = new StringBuilder();
		if (time >= 3600)
		{
			final long unit = time / 3600;
			time %= 3600;
			res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.HOURS", (CommandSender) null));
		}
		if (time >= 60)
		{
			final long unit = time / 60;
			time %= 60;
			res.append(" " + unit + " " + CrazyLocale.getUnitText("TIME.MINUTES", (CommandSender) null));
		}
		if (time > 0 || res.length() == 0)
			res.append(" " + time + " " + CrazyLocale.getUnitText("TIME.SECONDS", (CommandSender) null));
		return res.substring(1);
	}

	public final void start()
	{
		if (synced)
			start(Math.max(interval - System.currentTimeMillis() / 50 % interval, 1));
		else
			start(Math.max(Math.round(Math.random() * interval), 1));
	}

	public void start(final long delay)
	{
		if (tasks.size() != 0)
			return;
		final BukkitScheduler scheduler = Bukkit.getScheduler();
		for (final Long time : countDownTimes)
			if (time > delay)
				tasks.add(scheduler.runTaskTimer(plugin, new Messager(ChatHelper.putArgs(countDownMessage, timeConverter(time / 20))), delay - time + interval, interval));
			else
				tasks.add(scheduler.runTaskTimer(plugin, new Messager(ChatHelper.putArgs(countDownMessage, timeConverter(time / 20))), delay - time, interval));
		tasks.add(scheduler.runTaskTimer(plugin, this, delay, interval));
	}

	public void cancel()
	{
		for (final BukkitTask task : tasks)
			task.cancel();
		tasks.clear();
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
				postSpawnProcessing(type.spawn(randomizedLocation(location, spawnRange)));
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

	protected void postSpawnProcessing(final Entity entity)
	{
		if (v146OrLater)
		{
			if (entity instanceof LivingEntity)
			{
				postSpawnProcessing((Damageable) entity);
				postSpawnProcessing((LivingEntity) entity);
			}
			else if (entity instanceof Damageable)
				postSpawnProcessing((Damageable) entity);
		}
		else if (entity instanceof LivingEntity)
			postSpawnProcessing((LivingEntity) entity);
	}

	protected void postSpawnProcessing(final LivingEntity entity)
	{
		entity.setRemoveWhenFarAway(allowDespawn);
		if (showHealth)
		{
			final String name = entity.getCustomName() == null ? entity.getType().getName() : entity.getCustomName();
			entity.setMetadata("CreatureMeta", new CreatureMeta(name));
			entity.setCustomName(name + " (" + entity.getHealth() + ")");
			entity.setCustomNameVisible(true);
		}
	}

	protected void postSpawnProcessing(final Damageable entity)
	{
		if (health != -1)
		{
			entity.setMaxHealth(health);
			entity.setHealth(health);
		}
	}

	protected Location randomizedLocation(final Location location, final double offset)
	{
		if (offset == 0)
			return location;
		final Location res = location.clone();
		res.setX(res.getX() + (RANDOM.nextBoolean() ? 1 : -1) * RANDOM.nextDouble() * offset);
		res.setZ(res.getZ() + (RANDOM.nextBoolean() ? 1 : -1) * RANDOM.nextDouble() * offset);
		return res;
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
		config.set(path + "spawnRange", spawnRange);
		config.set(path + "amount", amount);
		config.set(path + "interval", interval);
		config.set(path + "repeat", repeat);
		config.set(path + "synced", synced);
		config.set(path + "chunkLoadRange", chunkLoadRange);
		config.set(path + "creatureMaxCount", creatureMaxCount);
		config.set(path + "creatureRange", creatureRange);
		config.set(path + "playerMinCount", playerMinCount);
		config.set(path + "playerRange", playerRange);
		config.set(path + "blockingRange", blockingRange);
		config.set(path + "countDownTimes", countDownTimes);
		if (countDownMessage != null)
			config.set(path + "countDownMessage", ChatHelper.decolorise(countDownMessage));
		config.set(path + "countDownBroadcast", countDownBroadcast);
		config.set(path + "allowDespawn", allowDespawn);
		config.set(path + "health", health);
		config.set(path + "showHealth", showHealth);
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
			res = Integer.valueOf(hashCode()).compareTo(o.hashCode());
		return res;
	}

	private class Messager implements Runnable
	{

		private final String message;

		public Messager(final String message)
		{
			super();
			this.message = message;
		}

		@Override
		public void run()
		{
			if (countDownBroadcast)
				for (final Player player : Bukkit.getOnlinePlayers())
					player.sendMessage(message);
			else
				for (final Player player : Bukkit.getOnlinePlayers())
					if (player.getWorld().equals(location.getWorld()))
						if (location.distance(player.getLocation()) < playerRange)
							player.sendMessage(message);
		}
	}
}
