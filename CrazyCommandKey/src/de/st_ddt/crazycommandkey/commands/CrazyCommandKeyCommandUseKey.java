package de.st_ddt.crazycommandkey.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazycommandkey.CrazyCommandKey;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyCommandKeyCommandUseKey extends CrazyCommandKeyCommandExecutor
{

	public CrazyCommandKeyCommandUseKey(final CrazyCommandKey plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYCOMMANDKEY.COMMAND.KEYUSE $Key$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!sender.hasPermission("crazycommandkey.keyuse"))
			throw new CrazyCommandPermissionException();
		if (args.length != 1)
			throw new CrazyCommandUsageException("/key <Key>");
		String command = plugin.getKeys().remove(args[0]);
		if (command == null)
			throw new CrazyCommandNoSuchException("Key", args[0]);
		command = ChatHelper.putArgs(command, sender.getName());
		plugin.sendLocaleMessage("COMMAND.KEYUSE", sender, args[0]);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
		plugin.getCrazyLogger().log("KeyUse", sender.getName() + " used key: " + args[0], "Command: " + command);
		plugin.saveConfiguration();
	}
}
