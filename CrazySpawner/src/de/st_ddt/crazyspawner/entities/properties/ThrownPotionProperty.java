package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.paramitrisable.ItemStackParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.PlayerItemStackParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class ThrownPotionProperty extends BasicProperty
{

	protected final ItemStack item;

	public ThrownPotionProperty()
	{
		super();
		this.item = null;
	}

	public ThrownPotionProperty(final ConfigurationSection config)
	{
		super(config);
		this.item = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("potion"));
		if (item != null && item.getType() != Material.POTION)
			throw new IllegalArgumentException("The item must be a potion! But has been a " + item.getType().name() + ".");
	}

	public ThrownPotionProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final ItemStackParamitrisable itemParam = (ItemStackParamitrisable) params.get("potion");
		this.item = itemParam.getValue();
		if (item != null && item.getType() != Material.POTION)
			throw new IllegalArgumentException("The item must be a potion! But has been a " + item.getType().name() + ".");
	}

	@Override
	public void apply(final Entity entity)
	{
		final ThrownPotion potion = (ThrownPotion) entity;
		if (item != null)
			potion.setItem(item);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final ItemStackParamitrisable itemParam = PlayerItemStackParamitrisable.getParamitrisableFor(item, sender);
		params.put("potion", itemParam);
		params.put("potionitem", itemParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		if (item == null)
			config.set(path + "potion", null);
		else
			config.set(path + "potion", item.serialize());
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "potion", "Item (Potions only!)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.THROWNPOTION $Potion$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.THROWNPOTION", target, item == null ? "None" : item);
	}

	@Override
	public boolean equalsDefault()
	{
		return item == null;
	}
}
