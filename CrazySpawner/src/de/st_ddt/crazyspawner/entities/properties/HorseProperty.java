package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.ItemStackParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public class HorseProperty extends BasicProperty
{

	protected final boolean chest;
	protected final Color color;
	protected final int domestication;
	protected final int maxDomestication;
	protected final double jumpStrength;
	protected final Style style;
	protected final Variant variant;
	// HorseInventory
	protected final ItemStack armor;
	protected final ItemStack saddle;

	public HorseProperty()
	{
		super();
		this.chest = false;
		this.color = null;
		this.domestication = -1;
		this.maxDomestication = -1;
		this.jumpStrength = -1;
		this.style = null;
		this.variant = null;
		this.saddle = null;
		this.armor = null;
	}

	public HorseProperty(final ConfigurationSection config)
	{
		super(config);
		this.chest = config.getBoolean("chest", false);
		final String colorName = config.getString("color");
		if (colorName == null)
			this.color = null;
		else
		{
			Color color = null;
			try
			{
				color = Color.valueOf(colorName);
			}
			catch (final Exception e)
			{
				System.err.println(config.getName() + "'s color " + colorName + " was corrupted/invalid and has been removed!");
			}
			this.color = color;
		}
		this.maxDomestication = config.getInt("maxDomestication", -1);
		this.domestication = Math.min(config.getInt("domestication", -1), maxDomestication);
		final double jumpStrength = Math.min(config.getInt("jumpStrength", -1), 2);
		if (jumpStrength < 0)
			this.jumpStrength = -1;
		else
			this.jumpStrength = jumpStrength;
		final String styleName = config.getString("style");
		if (styleName == null)
			this.style = null;
		else
		{
			Style style = null;
			try
			{
				style = Style.valueOf(styleName);
			}
			catch (final Exception e)
			{
				System.err.println(config.getName() + "'s style " + styleName + " was corrupted/invalid and has been removed!");
			}
			this.style = style;
		}
		final String variantName = config.getString("style");
		if (variantName == null)
			this.variant = null;
		else
		{
			Variant variant = null;
			try
			{
				variant = Variant.valueOf(styleName);
			}
			catch (final Exception e)
			{
				System.err.println(config.getName() + "'s variant " + styleName + " was corrupted/invalid and has been removed!");
			}
			this.variant = variant;
		}
		// HorseInventoy
		this.armor = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("armor"));
		this.saddle = ObjectSaveLoadHelper.loadItemStack(config.getConfigurationSection("saddle"));
	}

	@SuppressWarnings("unchecked")
	public HorseProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable chestParam = (BooleanParamitrisable) params.get("chest");
		this.chest = chestParam.getValue();
		final EnumParamitrisable<Color> colorParam = (EnumParamitrisable<Color>) params.get("color");
		this.color = colorParam.getValue();
		final IntegerParamitrisable maxDomesticationParam = (IntegerParamitrisable) params.get("maxdomestication");
		this.maxDomestication = maxDomesticationParam.getValue();
		final IntegerParamitrisable domesticationParam = (IntegerParamitrisable) params.get("domestication");
		this.domestication = domesticationParam.getValue();
		final DoubleParamitrisable jumpStrengthParam = (DoubleParamitrisable) params.get("jumpstrength");
		final double jumpStrength = jumpStrengthParam.getValue();
		if (jumpStrength < 0)
			this.jumpStrength = -1;
		else
			this.jumpStrength = jumpStrength;
		final EnumParamitrisable<Style> styleParam = (EnumParamitrisable<Style>) params.get("style");
		this.style = styleParam.getValue();
		final EnumParamitrisable<Variant> variantParam = (EnumParamitrisable<Variant>) params.get("variant");
		this.variant = variantParam.getValue();
		// HorseInventoy
		final ItemStackParamitrisable armorParam = (ItemStackParamitrisable) params.get("armor");
		this.armor = armorParam.getValue();
		final ItemStackParamitrisable saddleParam = (ItemStackParamitrisable) params.get("saddle");
		this.saddle = saddleParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Horse horse = (Horse) entity;
		horse.setCarryingChest(chest);
		if (color != null)
			horse.setColor(color);
		if (maxDomestication != -1)
			horse.setMaxDomestication(maxDomestication);
		if (domestication != -1)
			horse.setDomestication(domestication);
		if (jumpStrength != -1)
			horse.setJumpStrength(jumpStrength);
		if (style != null)
			horse.setStyle(style);
		if (variant != null)
			horse.setVariant(variant);
		final HorseInventory inventory = horse.getInventory();
		if (saddle != null)
			inventory.setSaddle(saveClone(saddle));
		if (armor != null)
			inventory.setArmor(saveClone(armor));
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final BooleanParamitrisable chestParam = new BooleanParamitrisable(chest);
		params.put("chest", chestParam);
		final EnumParamitrisable<Color> colorParam = new EnumParamitrisable<Horse.Color>("HorseColor", color, Color.values());
		params.put("c", colorParam);
		params.put("color", colorParam);
		params.put("horsecolor", colorParam);
		final IntegerParamitrisable maxDomesticationParam = new IntegerParamitrisable(maxDomestication);
		params.put("md", maxDomesticationParam);
		params.put("maxdo", maxDomesticationParam);
		params.put("maxdomestication", maxDomesticationParam);
		final IntegerParamitrisable domesticationParam = new IntegerParamitrisable(domestication);
		params.put("do", domesticationParam);
		params.put("domestication", domesticationParam);
		final DoubleParamitrisable jumpStrengthParam = new DoubleParamitrisable(jumpStrength);
		params.put("js", jumpStrengthParam);
		params.put("jumps", jumpStrengthParam);
		params.put("jstrength", jumpStrengthParam);
		params.put("jumpstrength", jumpStrengthParam);
		final EnumParamitrisable<Style> styleParam = new EnumParamitrisable<Style>("HorseStyle", style, Style.values());
		params.put("s", styleParam);
		params.put("style", styleParam);
		final EnumParamitrisable<Variant> variantParam = new EnumParamitrisable<Variant>("HorseVariant", variant, Variant.values());
		params.put("v", variantParam);
		params.put("variant", variantParam);
		// HorseInventoy
		final ItemStackParamitrisable armorParam = new ItemStackParamitrisable(armor);
		params.put("armor", armorParam);
		final ItemStackParamitrisable saddleParam = new ItemStackParamitrisable(saddle);
		params.put("saddle", saddleParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "chest", chest);
		if (color == null)
			config.set(path + "color", null);
		else
			config.set(path + "color", color.name());
		config.set(path + "domestication", domestication);
		config.set(path + "maxDomestication", maxDomestication);
		config.set(path + "jumpStrength", jumpStrength);
		if (style == null)
			config.set(path + "style", null);
		else
			config.set(path + "style", style.name());
		if (variant == null)
			config.set(path + "variant", null);
		else
			config.set(path + "variant", variant.name());
		// HorseInventoy
		if (armor == null)
			config.set(path + "armor", null);
		else
			config.set(path + "armor", armor.serialize());
		if (saddle == null)
			config.set(path + "saddle", null);
		else
			config.set(path + "saddle", saddle.serialize());
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "chest", "boolean (true/false)");
		config.set(path + "color", "HorseColor");
		config.set(path + "domestication", "int (-1 = default)");
		config.set(path + "maxDomestication", "int (-1 = default)");
		config.set(path + "jumpStrength", "double (0.0 - 2.0; -1 = default)");
		config.set(path + "style", "HorseStyle");
		config.set(path + "variant", "HorseVariant");
		// HorseInventoy
		config.set(path + "armor", "Item");
		config.set(path + "saddle", "Item");
	}

	@Override
	public void show(final CommandSender target)
	{
		// EDIT Implementiere HorseProperty.show()
	}

	@Override
	public boolean equalsDefault()
	{
		return chest == false && color == null && domestication == -1 && maxDomestication == -1 && jumpStrength == -1 && style == null && variant == null && armor == null && saddle == null;
	}
}
