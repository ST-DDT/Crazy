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
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.ItemStackParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.PlayerItemStackParamitrisable;
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
		super(params);
		final ItemStackParamitrisable bootsParam = (ItemStackParamitrisable) params.get("boots");
		this.boots = bootsParam.getValue();
		final DoubleParamitrisable bootsDropChanceParam = (DoubleParamitrisable) params.get("bootsdropchance");
		this.bootsDropChance = bootsDropChanceParam.getValue().floatValue();
		final ItemStackParamitrisable leggingsParam = (ItemStackParamitrisable) params.get("leggings");
		this.leggings = leggingsParam.getValue();
		final DoubleParamitrisable leggingsDropChanceParam = (DoubleParamitrisable) params.get("leggingsdropchance");
		this.leggingsDropChance = leggingsDropChanceParam.getValue().floatValue();
		final ItemStackParamitrisable chestplateParam = (ItemStackParamitrisable) params.get("chestplate");
		this.chestplate = chestplateParam.getValue();
		final DoubleParamitrisable chestplateDropChanceParam = (DoubleParamitrisable) params.get("chestplatedropchance");
		this.chestplateDropChance = chestplateDropChanceParam.getValue().floatValue();
		final ItemStackParamitrisable helmetParam = (ItemStackParamitrisable) params.get("helmet");
		this.helmet = helmetParam.getValue();
		final DoubleParamitrisable helmetDropChanceParam = (DoubleParamitrisable) params.get("helmetdropchance");
		this.helmetDropChance = helmetDropChanceParam.getValue().floatValue();
		final ItemStackParamitrisable itemInHandParam = (ItemStackParamitrisable) params.get("iteminhand");
		this.itemInHand = itemInHandParam.getValue();
		final DoubleParamitrisable itemInHandDropChanceParam = (DoubleParamitrisable) params.get("iteminhanddropchance");
		this.itemInHandDropChance = itemInHandDropChanceParam.getValue().floatValue();
		this.drops = new ArrayList<Drop>();
		int count = 0;
		while (true)
		{
			count++;
			final ItemStackParamitrisable dropParam = (ItemStackParamitrisable) params.get("drop" + count);
			final DoubleParamitrisable dropDropChanceParam = (DoubleParamitrisable) params.get("drop" + count + "dropchance");
			if (dropParam == null)
				break;
			if (dropParam.getValue() == null)
				continue;
			drops.add(new Drop(dropParam.getValue(), dropDropChanceParam.getValue().floatValue()));
		}
		final BooleanParamitrisable allowItemPickUpParam = (BooleanParamitrisable) params.get("allowitempickup");
		this.allowItemPickUp = allowItemPickUpParam.getValue();
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
		final ItemStackParamitrisable bootsParam = PlayerItemStackParamitrisable.getParamitrisableFor(boots, sender);
		params.put("boots", bootsParam);
		params.put("armorboots", bootsParam);
		final DoubleParamitrisable bootsDropChanceParam = new DoubleParamitrisable(bootsDropChance);
		params.put("bootsdc", bootsDropChanceParam);
		params.put("armorbootsdc", bootsDropChanceParam);
		params.put("bootsdropchance", bootsDropChanceParam);
		params.put("armorbootsdropchances", bootsDropChanceParam);
		final ItemStackParamitrisable leggingsParam = PlayerItemStackParamitrisable.getParamitrisableFor(leggings, sender);
		params.put("leggings", leggingsParam);
		params.put("armorleggings", leggingsParam);
		final DoubleParamitrisable leggingsDropChanceParam = new DoubleParamitrisable(leggingsDropChance);
		params.put("leggingsdc", leggingsDropChanceParam);
		params.put("armorleggingsdc", leggingsDropChanceParam);
		params.put("leggingsdropchance", leggingsDropChanceParam);
		params.put("armorleggingsdropchances", leggingsDropChanceParam);
		final ItemStackParamitrisable chestplateParam = PlayerItemStackParamitrisable.getParamitrisableFor(chestplate, sender);
		params.put("helmet", chestplateParam);
		params.put("armorhelmet", chestplateParam);
		final DoubleParamitrisable chestplateDropChanceParam = new DoubleParamitrisable(chestplateDropChance);
		params.put("helmetdc", chestplateDropChanceParam);
		params.put("armorhelmetdc", chestplateDropChanceParam);
		params.put("helmetdropchance", chestplateDropChanceParam);
		params.put("armorhelmetdropchances", chestplateDropChanceParam);
		final ItemStackParamitrisable helmetParam = PlayerItemStackParamitrisable.getParamitrisableFor(helmet, sender);
		params.put("helmet", helmetParam);
		params.put("armorhelmet", helmetParam);
		final DoubleParamitrisable helmetDropChanceParam = new DoubleParamitrisable(helmetDropChance);
		params.put("helmetdc", helmetDropChanceParam);
		params.put("armorhelmetdc", helmetDropChanceParam);
		params.put("helmetdropchance", helmetDropChanceParam);
		params.put("armorhelmetdropchances", helmetDropChanceParam);
		final ItemStackParamitrisable iteminhandParam = PlayerItemStackParamitrisable.getParamitrisableFor(itemInHand, sender);
		params.put("hand", iteminhandParam);
		params.put("iteminhand", iteminhandParam);
		final DoubleParamitrisable iteminhandDropChanceParam = new DoubleParamitrisable(itemInHandDropChance);
		params.put("handdc", iteminhandDropChanceParam);
		params.put("iteminhanddc", iteminhandDropChanceParam);
		params.put("handdropchance", iteminhandDropChanceParam);
		params.put("iteminhanddropchance", iteminhandDropChanceParam);
		int count = 0;
		for (final Drop drop : drops)
		{
			count++;
			final ItemStackParamitrisable dropParam = PlayerItemStackParamitrisable.getParamitrisableFor(drop.getItem(), sender);
			params.put("d" + count, dropParam);
			params.put("drop" + count, dropParam);
			final DoubleParamitrisable dropDropChanceParam = new DoubleParamitrisable(drop.getChance());
			params.put("d" + count + "dc", dropDropChanceParam);
			params.put("drop" + count + "dc", dropDropChanceParam);
			params.put("d" + count + "dropchance", dropDropChanceParam);
			params.put("drop" + count + "dropchance", dropDropChanceParam);
		}
		count++;
		final ItemStackParamitrisable dropParam = PlayerItemStackParamitrisable.getParamitrisableFor(null, sender);
		params.put("d" + count, dropParam);
		params.put("drop" + count, dropParam);
		final DoubleParamitrisable dropDropChanceParam = new DoubleParamitrisable(1);
		params.put("d" + count + "dc", dropDropChanceParam);
		params.put("drop" + count + "dc", dropDropChanceParam);
		params.put("d" + count + "dropchance", dropDropChanceParam);
		params.put("drop" + count + "dropchance", dropDropChanceParam);
		final BooleanParamitrisable allowItemPickUpParam = new BooleanParamitrisable(allowItemPickUp);
		params.put("pickup", allowItemPickUpParam);
		params.put("itempickup", allowItemPickUpParam);
		params.put("allowitempickup", allowItemPickUpParam);
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
