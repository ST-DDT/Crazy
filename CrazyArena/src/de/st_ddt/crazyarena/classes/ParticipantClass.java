package de.st_ddt.crazyarena.classes;

import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public class ParticipantClass implements ConfigurationSaveable
{

	protected final String name;
	protected final ArrayList<ItemStack> items;
	protected ItemStack helmet;
	protected ItemStack chestplate;
	protected ItemStack leggins;
	protected ItemStack boots;

	public ParticipantClass(final String name)
	{
		super();
		this.name = name;
		items = new ArrayList<ItemStack>();
		helmet = null;
		chestplate = null;
		leggins = null;
		boots = null;
	}

	public ParticipantClass(final ConfigurationSection config)
	{
		this(config.getString("name"));
		if (config.contains("helmet"))
			helmet = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("helmet"));
		if (config.contains("chestplate"))
			chestplate = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("chestplate"));
		if (config.contains("leggins"))
			leggins = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("leggins"));
		if (config.contains("helmet"))
			boots = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("boots"));
		ConfigurationSection config2 = config.getConfigurationSection("inventory");
		for (String name : config2.getKeys(false))
			items.add(ObjectSaveLoadHelper.loadItemStack(config2.getConfigurationSection(name)));
	}

	/**
	 * Creates a new ParticipantClass using player as template
	 * 
	 * @param name
	 *            Name of new ParticipantClass
	 * @param player
	 *            Player to use as template
	 */
	public ParticipantClass(final String name, final Player player)
	{
		this(name);
		PlayerInventory inventory = player.getInventory();
		for (ItemStack item : inventory)
			this.items.add(item.clone());
		if (inventory.getHelmet() != null)
			this.helmet = inventory.getHelmet().clone();
		if (inventory.getChestplate() != null)
			this.chestplate = inventory.getChestplate().clone();
		if (inventory.getLeggings() != null)
			this.leggins = inventory.getLeggings().clone();
		if (inventory.getBoots() != null)
			this.boots = inventory.getBoots().clone();
	}

	public ParticipantClass(final String name, final ArrayList<ItemStack> items, final ItemStack helmet, final ItemStack chestplate, final ItemStack leggins, final ItemStack boots)
	{
		this(name);
		this.items.addAll(items);
		if (helmet != null)
			this.helmet = helmet.clone();
		if (chestplate != null)
			this.chestplate = chestplate.clone();
		if (leggins != null)
			this.leggins = leggins.clone();
		if (boots != null)
			this.boots = boots.clone();
	}

	public void activate(final Player player)
	{
		PlayerInventory inventory = player.getInventory();
		inventory.clear();
		for (ItemStack item : items)
			inventory.addItem(item.clone());
		if (helmet != null)
			inventory.setHelmet(helmet.clone());
		if (chestplate != null)
			inventory.setChestplate(chestplate.clone());
		if (leggins != null)
			inventory.setLeggings(leggins.clone());
		if (boots != null)
			inventory.setBoots(boots.clone());
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path, null);
		config.set(path + "name", name);
		if (helmet == null)
			config.set(path + "helmet", null);
		else
			ObjectSaveLoadHelper.saveItemStack(config, path + "helmet", helmet);
		if (chestplate == null)
			config.set(path + "chestplate", null);
		else
			ObjectSaveLoadHelper.saveItemStack(config, path + "chestplate", chestplate);
		if (leggins == null)
			config.set(path + "leggins", null);
		else
			ObjectSaveLoadHelper.saveItemStack(config, path + "leggins", leggins);
		if (boots == null)
			config.set(path + "boots", null);
		else
			ObjectSaveLoadHelper.saveItemStack(config, path + "boots", boots);
		config.set(path + "inventory", null);
		for (int i = 0; i < items.size(); i++)
			ObjectSaveLoadHelper.saveItemStack(config, path + "inventory.item" + i+".", items.get(i));
	}
}
