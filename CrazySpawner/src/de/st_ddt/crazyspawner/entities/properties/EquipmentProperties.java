package de.st_ddt.crazyspawner.entities.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyspawner.entities.meta.CustomDrops;
import de.st_ddt.crazyutil.Drop;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public final class EquipmentProperties extends MetadataProperty implements CustomDrops
{

	protected final ItemStack boots;
	protected final float bootsDropChance;
	protected final ItemStack leggings;
	protected final float leggingsDropChance;
	protected final ItemStack chestplate;
	protected final float chestplateDropChance;
	protected final ItemStack helmet;
	protected final float helmetDropChance;
	protected final ItemStack itemInHand;
	protected final float itemInHandDropChance;
	protected final List<Drop> drops;
	protected final Boolean allowItemPickUp;

	public EquipmentProperties()
	{
		super();
		this.boots = null;
		this.bootsDropChance = 0;
		this.leggings = null;
		this.leggingsDropChance = 0;
		this.chestplate = null;
		this.chestplateDropChance = 0;
		this.helmet = null;
		this.helmetDropChance = 0;
		this.itemInHand = null;
		this.itemInHandDropChance = 0;
		this.drops = null;
		this.allowItemPickUp = null;
	}

	public EquipmentProperties(final ConfigurationSection config)
	{
		super(config);
		final ConfigurationSection dropsConfig = config.getConfigurationSection("equipment.drops");
		if (dropsConfig == null)
			this.drops = null;
		else
		{
			this.drops = new ArrayList<Drop>();
			for (final String key : dropsConfig.getKeys(false))
				try
				{
					this.drops.add(new Drop(dropsConfig.getConfigurationSection(key)));
				}
				catch (final Exception e)
				{
					System.err.println(config.getName() + "'s costum drop " + key + " was corrupted/invalid and has been removed!");
				}
		}
		this.boots = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("equipment.boots"));
		this.bootsDropChance = drops == null ? ((float) config.getDouble("equipment.bootsDropChance")) : 0;
		this.leggings = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("equipment.leggings"));
		this.leggingsDropChance = drops == null ? ((float) config.getDouble("equipment.leggingsDropChance")) : 0;
		this.chestplate = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("equipment.chestplate"));
		this.chestplateDropChance = drops == null ? ((float) config.getDouble("equipment.chestplateDropChance")) : 0;
		this.helmet = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("equipment.helmet"));
		this.helmetDropChance = drops == null ? ((float) config.getDouble("equipment.helmetDropChance")) : 0;
		this.itemInHand = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("equipment.itemInHand"));
		this.itemInHandDropChance = drops == null ? ((float) config.getDouble("equipment.itemInHandDropChance")) : 0;
		if (config.getBoolean("equipment.allowItemPickUp", false))
			this.allowItemPickUp = true;
		else if (!config.getBoolean("equipment.allowItemPickUp", true))
			this.allowItemPickUp = false;
		else
			this.allowItemPickUp = null;
	}

	public EquipmentProperties(final Map<String, ? extends Paramitrisable> params)
	{
		// super(params);
		this();
		// EDIT Implementiere Konstruktor von EquipmentProperties
	}

	@Override
	public void apply(final Entity entity)
	{
		final LivingEntity living = (LivingEntity) entity;
		final EntityEquipment equipment = living.getEquipment();
		if (boots != null)
		{
			equipment.setBoots(saveClone(boots));
			equipment.setBootsDropChance(bootsDropChance);
		}
		if (leggings != null)
		{
			equipment.setLeggings(saveClone(leggings));
			equipment.setLeggingsDropChance(leggingsDropChance);
		}
		if (chestplate != null)
		{
			equipment.setChestplate(saveClone(chestplate));
			equipment.setChestplateDropChance(chestplateDropChance);
		}
		if (helmet != null)
		{
			equipment.setHelmet(saveClone(helmet));
			equipment.setHelmetDropChance(helmetDropChance);
		}
		if (itemInHand != null)
		{
			equipment.setItemInHand(saveClone(itemInHand));
			equipment.setItemInHandDropChance(itemInHandDropChance);
		}
		if (drops != null)
			entity.setMetadata(CustomDrops.METAHEADER, this);
		if (allowItemPickUp != null)
			living.setCanPickupItems(allowItemPickUp);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		// EDIT Implementiere EquipmentProperties.getCommandParams()
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		if (boots == null)
			config.set(path + "equipment.boots", null);
		else
			config.set(path + "equipment.boots", boots.serialize());
		config.set(path + "equipment.bootsDropChance", bootsDropChance);
		if (leggings == null)
			config.set(path + "equipment.leggings", null);
		else
			config.set(path + "equipment.leggings", leggings.serialize());
		config.set(path + "equipment.leggingsDropChance", leggingsDropChance);
		if (chestplate == null)
			config.set(path + "equipment.chestplate", null);
		else
			config.set(path + "equipment.chestplate", chestplate.serialize());
		config.set(path + "equipment.chestplateDropChance", chestplateDropChance);
		if (helmet == null)
			config.set(path + "equipment.helmet", null);
		else
			config.set(path + "equipment.helmet", helmet.serialize());
		config.set(path + "equipment.helmetDropChance", helmetDropChance);
		if (itemInHand == null)
			config.set(path + "equipment.itemInHand", null);
		else
			config.set(path + "equipment.itemInHand", itemInHand.serialize());
		config.set(path + "equipment.itemInHandDropChance", itemInHandDropChance);
		if (drops == null)
			config.set(path + "equipment.drops", null);
		else
		{
			int i = 0;
			for (final Drop drop : drops)
				drop.save(config, path + "equipment.drops.d" + (i++) + ".");
		}
		if (allowItemPickUp == null)
			config.set(path + "equipment.allowItemPickUp", "default");
		else
			config.set(path + "equipment.allowItemPickUp", allowItemPickUp);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "equipment.boots", "Item");
		config.set(path + "equipment.bootsDropChance", "float (0-1)");
		config.set(path + "equipment.leggings", "Item");
		config.set(path + "equipment.leggingsDropChance", "float (0-1)");
		config.set(path + "equipment.chestplate", "Item");
		config.set(path + "equipment.chestplateDropChance", "float (0-1)");
		config.set(path + "equipment.helmet", "Item");
		config.set(path + "equipment.helmetDropChance", "float (0-1)");
		config.set(path + "equipment.itemInHand", "Item");
		config.set(path + "equipment.itemInHandDropChance", "float (0-1)");
		config.set(path + "equipment.drops.d1.item", "Item");
		config.set(path + "equipment.drops.d1.chance", "float (0-1)");
		config.set(path + "equipment.drops.d2.item", "Item");
		config.set(path + "equipment.drops.d2.chance", "float (0-1)");
		config.set(path + "equipment.drops.dX.item", "Item");
		config.set(path + "equipment.drops.dX.chance", "float (0-1)");
		config.set(path + "equipment.allowItemPickUp", "Boolean (true/false/default)");
	}

	@Override
	public List<ItemStack> getDrops()
	{
		return updateDrops(new ArrayList<ItemStack>());
	}

	@Override
	public <S extends Collection<ItemStack>> S updateDrops(final S collection)
	{
		if (drops.contains(null))
			return collection;
		try
		{
			collection.clear();
			for (final Drop drop : drops)
				if (drop.checkChance())
					collection.add(drop.getItemClone());
		}
		catch (final UnsupportedOperationException e)
		{}
		return collection;
	}

	@Override
	public void show(final CommandSender target)
	{
		// TODO Automatisch generierter Methodenstub
	}
}
