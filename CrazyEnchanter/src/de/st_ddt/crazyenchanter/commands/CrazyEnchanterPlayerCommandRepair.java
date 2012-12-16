package de.st_ddt.crazyenchanter.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyenchanter.CrazyEnchanter;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyEnchanterPlayerCommandRepair extends CrazyEnchanterPlayerCommandExecutor
{

	public CrazyEnchanterPlayerCommandRepair(final CrazyEnchanter plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYENCHANTER.COMMAND.REPAIR.SUCCESS $Item$ $Level$")
	public void command(final Player player, final String[] args) throws CrazyException
	{
		if (args.length > 1)
			throw new CrazyCommandUsageException("[DurabilityQuota]");
		final ItemStack item = player.getItemInHand();
		if (item == null)
			throw new CrazyCommandCircumstanceException("when helding an repairable item in hand!");
		final short max = item.getType().getMaxDurability();
		if (max == 0)
			throw new CrazyCommandCircumstanceException("when helding an repairable item in hand!");
		final double level;
		if (args.length == 1)
			try
			{
				level = Double.parseDouble(args[0]);
			}
			catch (final NumberFormatException e)
			{
				throw new CrazyCommandParameterException(0, "Numeric (Double)");
			}
		else
			level = 1;
		if (level <= 0 || level > 1)
			throw new CrazyCommandParameterException(0, "Numeric (Double)", "0 < x <= 1");
		short quota = (short) Math.round(max * (1 - level));
		item.setDurability(quota);
		plugin.sendLocaleMessage("COMMAND.REPAIR.SUCCESS", player, item.getType().toString(), level * 100);
	}

	@Override
	public boolean hasAccessPermission(final Player player)
	{
		return PermissionModule.hasPermission(player, "crazyenchanter.repair");
	}
}
