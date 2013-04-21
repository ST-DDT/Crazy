package de.st_ddt.crazyenchanter.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyenchanter.CrazyEnchanter;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandErrorException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.Tabbed;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.MapParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class PlayerCommandEnchant extends PlayerCommandExecutor
{

	public PlayerCommandEnchant(final CrazyEnchanter plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYENCHANTER.COMMAND.ENCHANT.SUCCESS $Item$ $Enchantment$ $Level$")
	public void command(final Player player, final String[] args) throws CrazyException
	{
		final ItemStack item = player.getItemInHand();
		if (item == null)
			throw new CrazyCommandCircumstanceException("when helding an enchantable item in hand!");
		final Map<String, Enchantment> applicableEnchantments = new TreeMap<String, Enchantment>();
		for (final Enchantment enchantment : Enchantment.values())
			if (enchantment.canEnchantItem(item))
				applicableEnchantments.put(enchantment.getName().toLowerCase(), enchantment);
		if (applicableEnchantments.size() == 0)
			throw new CrazyCommandCircumstanceException("when helding an enchantable item in hand!");
		final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
		final MapParamitrisable<Enchantment> enchantment = new MapParamitrisable<Enchantment>("Enchantment", applicableEnchantments, null, true);
		params.put("e", enchantment);
		params.put("enchantment", enchantment);
		params.put("enchantments", enchantment);
		final IntegerParamitrisable level = new IntegerParamitrisable(1);
		params.put("l", level);
		params.put("level", level);
		final BooleanParamitrisable unsafe = new BooleanParamitrisable(false);
		params.put("u", unsafe);
		params.put("unsafe", unsafe);
		ChatHelperExtended.readParameters(args, params, enchantment, level, unsafe);
		if (unsafe.getValue())
			item.addUnsafeEnchantment(enchantment.getValue(), level.getValue());
		else
			try
			{
				item.addEnchantment(enchantment.getValue(), level.getValue());
			}
			catch (final IllegalArgumentException e)
			{
				throw new CrazyCommandErrorException(e);
			}
		plugin.sendLocaleMessage("COMMAND.ENCHANT.SUCCESS", player, item.getType().toString(), enchantment.getValue().getName(), level.getValue());
	}

	@Override
	public List<String> tab(final Player player, final String[] args)
	{
		final ItemStack item = player.getItemInHand();
		final Map<String, Enchantment> applicableEnchantments = new TreeMap<String, Enchantment>();
		for (final Enchantment enchantment : Enchantment.values())
			if (enchantment.canEnchantItem(item))
				applicableEnchantments.put(enchantment.getName().toLowerCase(), enchantment);
		if (applicableEnchantments.size() == 0)
			return null;
		final Map<String, Tabbed> params = new HashMap<String, Tabbed>();
		final MapParamitrisable<Enchantment> enchantment = new MapParamitrisable<Enchantment>("Enchantment", applicableEnchantments, null, true);
		params.put("e", enchantment);
		params.put("enchantment", enchantment);
		params.put("enchantments", enchantment);
		final IntegerParamitrisable level = new IntegerParamitrisable(1);
		params.put("l", level);
		params.put("level", level);
		final BooleanParamitrisable unsafe = new BooleanParamitrisable(false);
		params.put("u", unsafe);
		params.put("unsafe", unsafe);
		return ChatHelperExtended.tabHelp(args, params, enchantment, level, unsafe);
	}

	@Override
	public boolean hasAccessPermission(final Player player)
	{
		return PermissionModule.hasPermission(player, "crazyenchanter.enchant");
	}
}
