package de.st_ddt.crazyutil.paramitrisable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;

public class ItemStackParamitrisable extends TypedParamitrisable<ItemStack>
{

	protected final static Pattern PATTERN_SPACE = Pattern.compile(" ");

	public ItemStackParamitrisable(final ItemStack defaultValue)
	{
		super(defaultValue);
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		final Map<String, TabbedParamitrisable> params = new HashMap<String, TabbedParamitrisable>();
		final Map<Enchantment, IntegerParamitrisable> enchantments = new LinkedHashMap<Enchantment, IntegerParamitrisable>();
		final MaterialParamitriable material;
		final IntegerParamitrisable amount;
		final IntegerParamitrisable damage;
		if (value == null)
		{
			material = new MaterialParamitriable(null);
			amount = new IntegerParamitrisable(1);
			damage = new IntegerParamitrisable(0);
			for (final Enchantment enchantment : Enchantment.values())
			{
				final IntegerParamitrisable level = new IntegerParamitrisable(0);
				enchantments.put(enchantment, level);
				params.put(enchantment.getName(), level);
			}
		}
		else
		{
			material = new MaterialParamitriable(value.getType());
			amount = new IntegerParamitrisable(value.getAmount());
			damage = new IntegerParamitrisable(value.getDurability());
			for (final Enchantment enchantment : Enchantment.values())
			{
				final IntegerParamitrisable level = new IntegerParamitrisable(value.getEnchantmentLevel(enchantment));
				enchantments.put(enchantment, level);
				params.put(enchantment.getName(), level);
			}
		}
		ChatHelperExtended.readParameters(PATTERN_SPACE.split(parameter), params, material, amount, damage);
		if (material.getValue() == null || material.getValue() == Material.AIR)
			value = null;
		else
		{
			value = new ItemStack(material.getValue(), amount.getValue(), damage.getValue().shortValue());
			for (final Entry<Enchantment, IntegerParamitrisable> entry : enchantments.entrySet())
				value.addUnsafeEnchantment(entry.getKey(), entry.getValue().getValue());
		}
	}

	@Override
	public List<String> tab(final String parameter)
	{
		final Map<String, TabbedParamitrisable> params = new HashMap<String, TabbedParamitrisable>();
		final MaterialParamitriable material;
		final IntegerParamitrisable amount;
		final IntegerParamitrisable damage;
		if (value == null)
		{
			material = new MaterialParamitriable(null);
			amount = new IntegerParamitrisable(1);
			damage = new IntegerParamitrisable(0);
		}
		else
		{
			material = new MaterialParamitriable(value.getType());
			amount = new IntegerParamitrisable(value.getAmount());
			damage = new IntegerParamitrisable(value.getDurability());
		}
		final Map<Enchantment, IntegerParamitrisable> enchantments = new LinkedHashMap<Enchantment, IntegerParamitrisable>();
		for (final Enchantment enchantment : Enchantment.values())
		{
			final IntegerParamitrisable level = new IntegerParamitrisable(value.getEnchantmentLevel(enchantment));
			enchantments.put(enchantment, level);
			params.put(enchantment.getName(), level);
		}
		return ChatHelperExtended.tabHelp(PATTERN_SPACE.split(parameter), params, material, amount, damage);
	}
}
