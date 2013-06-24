package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.ItemStackParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.PlayerItemStackParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class DroppedItemProperty extends BasicProperty
{

	protected final ItemStack item;
	protected final int delay;

	public DroppedItemProperty()
	{
		super();
		this.item = new ItemStack(1);
		this.delay = 0;
	}

	public DroppedItemProperty(final ConfigurationSection config)
	{
		super(config);
		final ItemStack item = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("item"));
		this.item = item == null ? new ItemStack(1) : item;
		this.delay = Math.max(config.getInt("pickUpDelay", 0), 0);
	}

	public DroppedItemProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final ItemStackParamitrisable itemParam = (ItemStackParamitrisable) params.get("item");
		this.item = itemParam.getValue();
		final IntegerParamitrisable delayParam = (IntegerParamitrisable) params.get("pickupdelay");
		this.delay = Math.max(delayParam.getValue(), 0);
	}

	@Override
	public void apply(final Entity entity)
	{
		final Item drop = (Item) entity;
		drop.setItemStack(item);
		drop.setPickupDelay(delay);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final ItemStackParamitrisable itemParam = PlayerItemStackParamitrisable.getParamitrisableFor(item, sender);
		params.put("i", itemParam);
		params.put("item", itemParam);
		final IntegerParamitrisable delayParam = new IntegerParamitrisable(delay);
		params.put("pd", delayParam);
		params.put("pdelay", delayParam);
		params.put("pickupd", delayParam);
		params.put("pickupdelay", delayParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "item", item.serialize());
		config.set(path + "pickUpDelay", delay);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "item", "Item");
		config.set(path + "pickUpDelay", "int (0-x)");
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.ITEM $Item$", "CRAZYSPAWNER.ENTITY.PROPERTY.PICKUPDELAY $Delay$" })
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.ITEM", target, item.toString());
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.PICKUPDELAY", target, delay);
	}
}
