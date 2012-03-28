package de.st_ddt.crazyarena.utils;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import de.st_ddt.crazyutil.PairList;

public class PlayerSaver
{

	private final Player player;
	private Location compass;
	private String displayname;
	private int experience;
	private int foodlevel;
	private GameMode gamemode;
	private int health;
	private int air;
	private PairList<Integer, ItemStack> inventory;
	// private int slot;
	private float exhaustion;
	private float saturation;
	private boolean backup = false;

	public PlayerSaver(Player player)
	{
		super();
		this.player = player;
	}

	public void backup()
	{
		this.compass = player.getCompassTarget();
		this.displayname = player.getDisplayName();
		this.experience = player.getTotalExperience();
		this.foodlevel = player.getFoodLevel();
		this.gamemode = player.getGameMode();
		this.health = player.getHealth();
		this.air = player.getRemainingAir();
		this.exhaustion = player.getExhaustion();
		this.saturation = player.getSaturation();
		this.inventory = new PairList<Integer, ItemStack>();
		PlayerInventory inv = player.getInventory();
		int size = inv.getSize();
		for (int i = 0; i < size; i++)
			inventory.add(i, inv.getItem(i).clone());
		inventory.add(-1, inv.getBoots());
		inventory.add(-2, inv.getLeggings());
		inventory.add(-3, inv.getChestplate().clone());
		inventory.add(-4, inv.getHelmet().clone());
		// slot = inv.getHeldItemSlot();
		backup = true;
	}

	public boolean hasBackup()
	{
		return backup;
	}

	public void restore()
	{
		if (!backup)
			return;
		clear();
		player.setCompassTarget(compass);
		player.setDisplayName(displayname);
		player.setTotalExperience(experience);
		player.setFoodLevel(foodlevel);
		player.setGameMode(gamemode);
		player.setHealth(health);
		player.setRemainingAir(air);
		player.setExhaustion(exhaustion);
		player.setSaturation(saturation);
		PlayerInventory inv = player.getInventory();
		int size = inv.getSize();
		for (int i = 0; i < size; i++)
			inv.setItem(i, inventory.findDataVia1(i).clone());
		inv.setBoots(inventory.findDataVia1(-1).clone());
		inv.setLeggings(inventory.findDataVia1(-2).clone());
		inv.setChestplate(inventory.findDataVia1(-3).clone());
		inv.setHelmet(inventory.findDataVia1(-4).clone());
	}

	public void clear()
	{
		player.setTotalExperience(0);
		player.setFallDistance(20);
		player.setGameMode(GameMode.SURVIVAL);
		player.setHealth(20);
		player.setRemainingAir(20);
		player.setExhaustion(0);
		player.setSaturation(0);
		player.getInventory().clear();
	}
}
