package de.st_ddt.crazyutil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * PlayerSaver shall be used to temporarily backup player datas and restore them later.
 */
public class PlayerSaver implements Named
{

	protected final String name;
	protected Location compass;
	protected Location bed;
	protected int experience;
	protected int foodlevel;
	protected GameMode gamemode;
	protected int health;
	protected int air;
	protected final HashMap<Integer, ItemStack> inventory = new HashMap<Integer, ItemStack>();
	// private int slot;
	protected float exhaustion;
	protected float saturation;
	protected boolean backup = false;
	protected File file;
	protected final YamlConfiguration config = new YamlConfiguration();

	/**
	 * Create PlayerSaver, you have to create a backup first to use it.
	 * 
	 * @param name
	 *            the name of the player is backup belongs to.
	 */
	public PlayerSaver(final String name)
	{
		super();
		this.name = name;
	}

	/**
	 * Create PlayerSaver, you have to create a backup first to use it.
	 * 
	 * @param player
	 *            the Player who is used to create the backup.
	 */
	public PlayerSaver(final Player player)
	{
		this(player.getName());
	}

	/**
	 * Create PlayerSaver, it automatically generates a backup.
	 * 
	 * @param player
	 *            the Player who is used to create the backup.
	 * @param clearAfterBackup
	 *            when true the player is resetted to default values.
	 */
	public PlayerSaver(final Player player, final boolean clearAfterBackup)
	{
		this(player);
		backup(player);
		if (clearAfterBackup)
			clear(player);
	}

	/**
	 * Create PlayerSaver, it loads the backup from file.
	 * 
	 * @param name
	 *            the name of the player is backup belongs to.
	 * @param path
	 *            the path used to load the backup from a file
	 */
	public PlayerSaver(final String name, final String path)
	{
		this(name);
		setFilePath(path);
		loadFromFile();
	}

	/**
	 * Create PlayerSaver, it loads the backup from file.
	 * 
	 * @param name
	 *            the name of the player is backup belongs to.
	 * @param file
	 *            the file used to load the backup
	 */
	public PlayerSaver(final String name, final File file)
	{
		this(name);
		setFile(file);
		loadFromFile();
	}

	/**
	 * @return the name of the player this backup belongs to.
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * @return the player entity this backup belongs to. May be null, if player is offline.
	 */
	public Player getPlayer()
	{
		return Bukkit.getPlayerExact(name);
	}

	/**
	 * Create a backup of the assigned player.
	 */
	public void backup(final Player player)
	{
		this.compass = player.getCompassTarget();
		this.bed = player.getBedSpawnLocation();
		this.experience = player.getTotalExperience();
		this.foodlevel = player.getFoodLevel();
		this.gamemode = player.getGameMode();
		this.health = player.getHealth();
		this.air = player.getRemainingAir();
		this.exhaustion = player.getExhaustion();
		this.saturation = player.getSaturation();
		this.inventory.clear();
		final PlayerInventory inv = player.getInventory();
		final int size = inv.getSize();
		for (int i = 0; i < size; i++)
			if (inv.getItem(i) != null)
				inventory.put(i, inv.getItem(i).clone());
		if (inv.getBoots() != null)
			inventory.put(-1, inv.getBoots().clone());
		if (inv.getLeggings() != null)
			inventory.put(-2, inv.getLeggings().clone());
		if (inv.getChestplate() != null)
			inventory.put(-3, inv.getChestplate().clone());
		if (inv.getHelmet() != null)
			inventory.put(-4, inv.getHelmet().clone());
		backup = true;
	}

	/**
	 * @return true if a backup has been created.
	 */
	public boolean hasBackup()
	{
		return backup;
	}

	/**
	 * Restore player's data from the backup. This method does nothing if no backup is created. This method does nothing if player is null.
	 * 
	 * @param player
	 *            The player this backup should apply to.
	 */
	public void restore(final Player player)
	{
		if (!backup)
			return;
		if (player == null)
			return;
		player.setCompassTarget(compass);
		if (bed != null)
			player.setBedSpawnLocation(bed);
		player.setTotalExperience(experience);
		player.setFoodLevel(foodlevel);
		player.setGameMode(gamemode);
		player.setHealth(Math.min(Math.max(0, health), player.getMaxHealth()));
		player.setRemainingAir(Math.min(Math.max(0, air), player.getMaximumAir()));
		player.setExhaustion(exhaustion);
		player.setSaturation(saturation);
		player.setFireTicks(0);
		final PlayerInventory inv = player.getInventory();
		inv.clear();
		final int size = inv.getSize();
		for (int i = 0; i < size; i++)
			if (inventory.get(i) != null)
				inv.setItem(i, inventory.get(i).clone());
		if (inventory.containsKey(-1))
			inv.setBoots(inventory.get(-1).clone());
		else
			inv.setBoots(null);
		if (inventory.containsKey(-2))
			inv.setLeggings(inventory.get(-2).clone());
		else
			inv.setLeggings(null);
		if (inventory.containsKey(-3))
			inv.setChestplate(inventory.get(-3).clone());
		else
			inv.setChestplate(null);
		if (inventory.containsKey(-4))
			inv.setHelmet(inventory.get(-4).clone());
		else
			inv.setHelmet(null);
	}

	/**
	 * Wipe every restorable player data. Should be used after creating a backup.
	 */
	public void clear(final Player player)
	{
		player.setCompassTarget(player.getWorld().getSpawnLocation());
		player.setBedSpawnLocation(player.getWorld().getSpawnLocation());
		player.setTotalExperience(0);
		player.setFoodLevel(20);
		player.setGameMode(Bukkit.getDefaultGameMode());
		player.setHealth(player.getMaxHealth());
		player.setRemainingAir(player.getMaximumAir());
		player.setExhaustion(20);
		player.setSaturation(20);
		player.setFireTicks(0);
		player.getInventory().clear();
		player.setItemInHand(null);
	}

	/**
	 * Drops all stored information from backup. Cannot be reverted.
	 */
	public void reset()
	{
		backup = false;
		compass = null;
		bed = null;
		experience = 0;
		foodlevel = 20;
		gamemode = Bukkit.getDefaultGameMode();
		health = 20;
		air = 20;
		exhaustion = 20;
		saturation = 20;
		inventory.clear();
	}

	public File getFile()
	{
		return file;
	}

	public void setFile(final File file)
	{
		this.file = file;
	}

	public void setFilePath(final String path)
	{
		this.file = new File(path);
	}

	public final void loadFromFile()
	{
		try
		{
			config.load(file);
			load();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	public void load()
	{
		this.compass = ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection("compass"), null);
		this.bed = ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection("bed"), null);
		this.experience = config.getInt("xp", 0);
		this.foodlevel = config.getInt("food", 20);
		this.gamemode = GameMode.getByValue(config.getInt("gamemode"));
		if (gamemode == null)
			gamemode = Bukkit.getDefaultGameMode();
		this.health = config.getInt("hp", 10);
		this.air = config.getInt("air", 10);
		this.exhaustion = config.getInt("exhaustion", 10);
		this.saturation = config.getInt("saturation", 10);
		this.inventory.clear();
		final ConfigurationSection inventoryConfig = config.getConfigurationSection("inventory");
		if (config != null)
			this.inventory.putAll(ObjectSaveLoadHelper.loadInventory(inventoryConfig));
		this.backup = config.getBoolean("backup", false);
	}

	public final void saveToFile()
	{
		try
		{
			save();
			config.save(file);
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	public void save()
	{
		config.set("compass", null);
		if (compass == null)
			ObjectSaveLoadHelper.saveLocation(config, "compass.", compass, true, true);
		config.set("bed", null);
		if (bed != null)
			ObjectSaveLoadHelper.saveLocation(config, "bed.", bed, true, true);
		config.set("xp", experience);
		config.set("foodlevel", foodlevel);
		config.set("gamemode", gamemode.getValue());
		config.set("hp", health);
		config.set("air", air);
		config.set("exhaustion", exhaustion);
		config.set("saturation", saturation);
		config.set("inventory", null);
		ObjectSaveLoadHelper.saveInventory(config, "inventory.", inventory);
		config.set("backup", backup);
	}

	public final boolean wipeFile()
	{
		if (file == null)
			return true;
		if (!file.exists())
			return true;
		try
		{
			return file.delete();
		}
		catch (final Exception e)
		{
			return false;
		}
	}
}
