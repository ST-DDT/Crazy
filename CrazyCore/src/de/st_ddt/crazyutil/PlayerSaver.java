package de.st_ddt.crazyutil;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * PlayerSaver shall be used to temporarily backup player datas and restore them later.
 * 
 */
/**
 * @author Daniel
 *
 */
/**
 * @author Daniel
 * 
 */
/**
 * @author Daniel
 * 
 */
public class PlayerSaver
{

	protected final String name;
	protected Player player;
	protected Location compass;
	protected Location bed;
	protected String displayname;
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

	/**
	 * Create PlayerSaver, you have to create a backup first to use it. You can reassign the player later;
	 * 
	 * @param player
	 *            the Player who is used to create the backup.
	 */
	public PlayerSaver(final Player player)
	{
		super();
		this.name = player.getName();
		this.player = player;
	}

	/**
	 * @return the name of the player this backup belongs to.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the player entity this backup belongs to.
	 */
	public Player getPlayer()
	{
		return player;
	}

	/**
	 * This Method should be used if the player reconnected and the player handles does not match anymore.
	 * 
	 * @param player
	 *            the new player this backup shall belong to
	 */
	public void reasignPlayer(final Player player)
	{
		this.player = player;
	}

	/**
	 * Create a backup of the assigned player.
	 */
	public void backup()
	{
		if (player == null)
			return;
		this.compass = player.getCompassTarget();
		this.bed = player.getBedSpawnLocation();
		this.displayname = player.getDisplayName();
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
		// slot = inv.getHeldItemSlot();
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
	 * Restore player's data from the backup. This Method does nothing if no backup is created.
	 */
	public void restore()
	{
		if (!backup)
			return;
		player.setCompassTarget(compass);
		if (bed != null)
			player.setBedSpawnLocation(bed);
		player.setDisplayName(displayname);
		player.setTotalExperience(experience);
		player.setFoodLevel(foodlevel);
		player.setGameMode(gamemode);
		player.setHealth(health);
		player.setRemainingAir(air);
		player.setExhaustion(exhaustion);
		player.setSaturation(saturation);
		player.setFireTicks(0);
		final PlayerInventory inv = player.getInventory();
		inv.clear();
		final int size = inv.getSize();
		for (int i = 0; i < size; i++)
			if (inventory.get(i) != null)
				inv.setItem(i, inventory.get(i).clone());
		if (inventory.get(-1) != null)
			inv.setBoots(inventory.get(-1).clone());
		if (inventory.get(-2) != null)
			inv.setLeggings(inventory.get(-2).clone());
		if (inventory.get(-3) != null)
			inv.setChestplate(inventory.get(-3).clone());
		if (inventory.get(-4) != null)
			inv.setHelmet(inventory.get(-4).clone());
	}

	/**
	 * Wipe every restorable player data. Should be used after creating a backup.
	 */
	public void clear()
	{
		player.setCompassTarget(player.getWorld().getSpawnLocation());
		player.setBedSpawnLocation(player.getWorld().getSpawnLocation());
		player.setDisplayName(player.getName());
		player.setTotalExperience(0);
		player.setFoodLevel(20);
		player.setGameMode(Bukkit.getDefaultGameMode());
		player.setHealth(20);
		player.setRemainingAir(20);
		player.setExhaustion(20);
		player.setSaturation(20);
		player.setFireTicks(0);
		player.getInventory().clear();
	}
}
