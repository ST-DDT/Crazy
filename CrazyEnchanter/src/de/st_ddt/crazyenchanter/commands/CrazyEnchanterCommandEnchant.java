package de.st_ddt.crazyenchanter.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyenchanter.CrazyEnchanter;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandErrorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

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
			return;
		final Player player = (Player) sender;
		final ItemStack item = player.getItemInHand();
		if (item == null)
			throw new CrazyCommandCircumstanceException("when helding an enchantable item in hand!");
		if (item.getMaxStackSize() > 1)
			throw new CrazyCommandCircumstanceException("when helding an enchantable item in hand!");
		Enchantment enchantment = null;
		int level = 1;
		boolean unsafe = false;
		switch (args.length)
		{
			case 3:
				if (args[2].equals("1") || args[2].equalsIgnoreCase("true"))
					unsafe = true;
			case 2:
				try
				{
					level = Integer.parseInt(args[1]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(2, "Integer");
				}
				if (level < 0)
					throw new CrazyCommandParameterException(2, "positive Integer");
			case 1:
				enchantment = Enchantment.getByName(args[0]);
				if (enchantment == null)
					throw new CrazyCommandParameterException(1, "Enchantment", ChatHelper.listingString(plugin.getEnchantmentNames()));
				break;
			default:
				throw new CrazyCommandUsageException("<Enchantment> [Level [Unsafe]]");
		}
		if (unsafe)
			item.addUnsafeEnchantment(enchantment, level);
		else
			try
			{
				item.addEnchantment(enchantment, level);
			}
			catch (final IllegalArgumentException e)
			{
				throw new CrazyCommandErrorException(e);
			}
		plugin.sendLocaleMessage("COMMAND.ENCHANT.SUCCESS", sender, item.getType().toString(), enchantment.getName(), level);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length != 1)
			return null;
		final List<String> res = new LinkedList<String>();
		final String arg = args[0].toLowerCase();
		for (final Enchantment enchantment : Enchantment.values())
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
