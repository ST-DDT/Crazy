package de.st_ddt.crazycommandkey.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycommandkey.CrazyCommandKey;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyCommandKeyCommandGenKey extends CrazyCommandKeyCommandExecutor
{

	public CrazyCommandKeyCommandGenKey(final CrazyCommandKey plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYCOMMANDKEY.COMMAND.KEYGEN $Keys$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!sender.hasPermission("crazycommandkey.keygen"))
			throw new CrazyCommandPermissionException();
		if (args.length < 1)
			throw new CrazyCommandUsageException("<Command>");
		int amount = 1;
		int startIndex = 0;
		if (args[0].toLowerCase().startsWith("amount:"))
		{
			startIndex++;
			try
			{
				amount = Integer.parseInt(args[0].substring(7));
			}
			catch (final NumberFormatException e)
			{
				throw new CrazyCommandParameterException(0, "Integer");
			}
		}
		final ArrayList<String> genKeys = new ArrayList<String>();
		final String command = ChatHelper.listingString(" ", ChatHelperExtended.shiftArray(args, startIndex));
		for (int i = 0; i < amount; i++)
		{
			String key = Long.toHexString(Math.round(Math.random() * Long.MAX_VALUE)) + Long.toHexString(Math.round(Math.random() * Long.MAX_VALUE));
			while (!plugin.getConfig().getString("keys." + key, "").equals(""))
				key = Long.toHexString(Math.round(Math.random() * Long.MAX_VALUE)) + Long.toHexString(Math.round(Math.random() * Long.MAX_VALUE));
			key = key.substring(0, 13);
			plugin.getKeys().put(key, command);
			genKeys.add(key);
		}
		plugin.sendLocaleMessage("COMMAND.KEYGEN", sender, ChatHelper.listingString(genKeys));
		plugin.getCrazyLogger().log("KeyGen", sender.getName() + " created " + amount + " keys for Command: " + command, "Keys: " + ChatHelper.listingString(genKeys));
		plugin.saveConfiguration();
	}
}
