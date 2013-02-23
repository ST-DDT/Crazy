package de.st_ddt.crazychats.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyChatsCommandGroupPrefix extends CrazyChatsCommandExecutor
{

	public CrazyChatsCommandGroupPrefix(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCHATS.COMMAND.GROUP.PREFIX.DELETED $Group$", "CRAZYCHATS.COMMAND.GROUP.PREFIX.SET $Group$ $Prefix$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length == 1)
		{
			plugin.getGroupPrefixes().remove(args[0]);
			plugin.sendLocaleMessage("COMMAND.GROUP.PREFIX.DELETED", sender, args[0]);
			plugin.saveConfiguration();
		}
		else if (args.length == 2)
		{
			plugin.getGroupPrefixes().put(args[0], ChatHelper.colorise(args[1]));
			plugin.sendLocaleMessage("COMMAND.GROUP.PREFIX.SET", sender, args[0], ChatHelper.colorise(args[1]) + sender.getName());
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
		for (final String group : plugin.getGroupPrefixes().keySet())
			if (group.toLowerCase().startsWith(name))
				res.add(group);
		return res;
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazychats.group.prefix");
	}
}
