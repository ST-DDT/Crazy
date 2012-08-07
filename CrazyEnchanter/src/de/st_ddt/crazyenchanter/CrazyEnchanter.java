package de.st_ddt.crazyenchanter;

import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandErrorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;

public class CrazyEnchanter extends CrazyPlugin
{

	@Override
	public boolean command(CommandSender sender, String commandLabel, String[] args) throws CrazyException
	{
		if (commandLabel.equalsIgnoreCase("enchant"))
		{
			commandEnchant(sender, args);
			return true;
		}
		return false;
	}

	private void commandEnchant(CommandSender sender, String[] args) throws CrazyCommandException
	{
		if (!(sender instanceof Player))
			return;
		final Player player = (Player) sender;
		if (!player.hasPermission("crazyenchanter.enchant"))
			throw new CrazyCommandPermissionException();
		ItemStack item = player.getItemInHand();
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
				catch (NumberFormatException e)
				{
					throw new CrazyCommandParameterException(2, "Integer");
				}
				if (level < 0)
					throw new CrazyCommandParameterException(2, "positive Integer");
			case 1:
				enchantment = Enchantment.getByName(args[0]);
				System.out.println(ChatHelper.listingString(getEnchantmentNames()));
				if (enchantment == null)
					throw new CrazyCommandParameterException(1, "Enchantment", ChatHelper.listingString(getEnchantmentNames()));
				break;
			default:
				throw new CrazyCommandUsageException("/enchant <Enchantment> [Level [Unsafe]]");
		}
		if (unsafe)
			item.addUnsafeEnchantment(enchantment, level);
		else
			try
			{
				item.addEnchantment(enchantment, level);
			}
			catch (IllegalArgumentException e)
			{
				throw new CrazyCommandErrorException(e);
			}
		sendLocaleMessage("COMMAND.ENCHANT.DONE", sender, item.getType().toString(), enchantment.getName(), level);
	}

	private String[] getEnchantmentNames()
	{
		Enchantment[] enchantments = Enchantment.values();
		int length = enchantments.length;
		String[] res = new String[length];
		for (int i = 0; i < length; i++)
			res[i] = enchantments[i].getName();
		return res;
	}
}
