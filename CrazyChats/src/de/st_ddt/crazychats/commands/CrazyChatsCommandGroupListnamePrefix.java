package de.st_ddt.crazychats.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyChatsCommandGroupListnamePrefix extends CrazyChatsCommandExecutor
{

	public CrazyChatsCommandGroupListnamePrefix(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCHATS.COMMAND.GROUP.LISTNAMEPREFIX.DELETED $Group$", "CRAZYCHATS.COMMAND.GROUP.LISTNAMEPREFIX.SET $Group$ $ListnamePrefix$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length == 1)
		{
			plugin.getGroupListnamePrefixes().remove(args[0]);
			plugin.sendLocaleMessage("COMMAND.GROUP.LISTNAMEPREFIX.DELETED", sender, args[0]);
			plugin.saveConfiguration();
		}
		else if (args.length == 2)
		{
			plugin.getGroupListnamePrefixes().put(args[0], ChatHelper.colorise(args[1]));
			plugin.sendLocaleMessage("COMMAND.GROUP.LISTNAMEPREFIX.SET", sender, args[0], ChatHelper.colorise(args[1]) + sender.getName());
			plugin.saveConfiguration();
		}
		else
			throw new CrazyCommandUsageException("<Group> [Prefix]");
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length != 1)
			return null;
		final String name = args[0].toLowerCase();
		final List<String> res = new LinkedList<String>();
		for (final String group : plugin.getGroupListnamePrefixes().keySet())
			if (group.toLowerCase().startsWith(name))
				res.add(group);
		return res;
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazychats.group.listnameprefix");
	}
}
