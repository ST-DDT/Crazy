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

public class CrazyChatsCommandGroupSuffix extends CrazyChatsCommandExecutor
{

	public CrazyChatsCommandGroupSuffix(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCHATS.COMMAND.GROUP.SUFFIX.DELETED $Group$", "CRAZYCHATS.COMMAND.GROUP.SUFFIX.SET $Group$ $Suffix$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length == 1)
		{
			plugin.getGroupSuffixes().remove(args[0]);
			plugin.sendLocaleMessage("COMMAND.GROUP.SUFFIX.DELETED", sender, args[0]);
			plugin.saveConfiguration();
		}
		else if (args.length == 2)
		{
			plugin.getGroupSuffixes().put(args[0], ChatHelper.colorise(args[1]));
			plugin.sendLocaleMessage("COMMAND.GROUP.SUFFIX.SET", sender, args[0], ChatHelper.colorise(sender.getName() + args[1]));
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
		for (final String group : plugin.getGroupSuffixes().keySet())
			if (group.toLowerCase().startsWith(name))
				res.add(group);
		return res;
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazychats.group.suffix");
	}
}
