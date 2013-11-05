package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.Rotation;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.ItemStackParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.PlayerItemStackParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class ItemFrameProperty extends BasicProperty
{

	protected final ItemStack item;
	protected final Rotation rotation;

	public ItemFrameProperty()
	{
		super();
		this.item = null;
		this.rotation = null;
	}

	public ItemFrameProperty(final ItemStack item, final Rotation rotation)
	{
		super();
		this.item = item;
		this.rotation = rotation;
	}

	public ItemFrameProperty(final ConfigurationSection config)
	{
		super(config);
		this.item = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("item"));
		final String rotationName = config.getString("itemRotation", "default");
		if (rotationName.equals("default"))
			this.rotation = null;
		else
		{
			Rotation rotation = null;
			try
			{
				rotation = Rotation.valueOf(rotationName);
			}
			catch (final Exception e)
			{
				System.err.println(config.getName() + "'s rotation " + rotationName + " was corrupted/invalid and has been removed!");
				rotation = null;
			}
			this.rotation = rotation;
		}
	}

	public ItemFrameProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final ItemStackParamitrisable itemParam = (ItemStackParamitrisable) params.get("item");
		this.item = itemParam.getValue();
		@SuppressWarnings("unchecked")
		final EnumParamitrisable<Rotation> rotationParam = (EnumParamitrisable<Rotation>) params.get("itemrotation");
		this.rotation = rotationParam.getValue();
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return ItemFrame.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		final ItemFrame frame = (ItemFrame) entity;
		if (item != null)
			frame.setItem(item);
		if (rotation != null)
			frame.setRotation(rotation);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final ItemStackParamitrisable itemParam = PlayerItemStackParamitrisable.getParamitrisableFor(item, sender);
		params.put("i", itemParam);
		params.put("item", itemParam);
		final EnumParamitrisable<Rotation> rotationParam = new EnumParamitrisable<Rotation>("Rotation", rotation, Rotation.values());
		params.put("r", rotationParam);
		params.put("rotation", rotationParam);
		params.put("itemrotation", rotationParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		if (item != null)
			config.set(path + "item", item.serialize());
		if (rotation == null)
			config.set(path + "itemRotation", "default");
		else
			config.set(path + "itemRotation", rotation.name());
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "item", "Item");
		config.set(path + "itemRotation", "Rotation");
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.ITEMFRAME.ITEM $Item$", "CRAZYSPAWNER.ENTITY.PROPERTY.ITEMFRAME.ITEMROTATION $Rotation$" })
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.ITEMFRAME.ITEM", target, item == null ? "None" : item);
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.ITEMFRAME.ITEMROTATION", target, rotation == null ? "Default" : rotation.name());
	}

	@Override
	public boolean equalsDefault()
	{
		return item == null && rotation == null;
	}
}
