package de.st_ddt.crazyenchanter.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyenchanter.CrazyEnchanter;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandErrorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandExecutorException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.MapParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;

public class CrazyEnchanterCommandEnchant extends CrazyEnchanterCommandExecutor
{

	public CrazyEnchanterCommandEnchant(final CrazyEnchanter plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYENCHANTER.COMMAND.ENCHANT.SUCCESS $Item$ $Enchantment$ $Level$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!(sender instanceof Player))
			throw new CrazyCommandExecutorException(false);
		final Player player = (Player) sender;
		final ItemStack item = player.getItemInHand();
		if (item == null)
			throw new CrazyCommandCircumstanceException("when helding an enchantable item in hand!");
		if (item.getMaxStackSize() > 1)
			throw new CrazyCommandCircumstanceException("when helding an enchantable item in hand!");
		final Map<String, Enchantment> applicableEnchantments = new TreeMap<String, Enchantment>();
		for (final Enchantment enchantment : Enchantment.values())
			if (enchantment.canEnchantItem(item))
				applicableEnchantments.put(enchantment.getName(), enchantment);
		final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
		final MapParamitrisable<Enchantment> enchantment = new MapParamitrisable<Enchantment>("Enchantment", applicableEnchantments, null);
		params.put("e", enchantment);
		params.put("enchantment", enchantment);
		params.put("enchantments", enchantment);
		final IntegerParamitrisable level = new IntegerParamitrisable(1);
		params.put("l", level);
		params.put("level", level);
		final BooleanParamitrisable unsafe = new BooleanParamitrisable(false);
		params.put("u", unsafe);
		params.put("unsafe", unsafe);
		ChatHelperExtended.readParameters(args, params);
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
		plugin.sendLocaleMessage("COMMAND.ENCHANT.SUCCESS", sender, item.getType().toString(), enchantment.getValue().getName(), level.getValue());
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length != 1)
			return null;
		if (!(sender instanceof Player))
			return null;
		final Player player = (Player) sender;
		final ItemStack item = player.getItemInHand();
		final List<String> res = new LinkedList<String>();
		final String arg = args[0].toLowerCase();
		for (final Enchantment enchantment : Enchantment.values())
			if (enchantment.canEnchantItem(item))
				if (enchantment.getName().toLowerCase().startsWith(arg))
					res.add(enchantment.getName());
		return res;
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyenchanter.enchant");
	}
}
