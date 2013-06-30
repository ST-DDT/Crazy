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
import de.st_ddt.crazyutil.source.Permission;

public class CommandGroupHeadnamePrefix extends CommandExecutor
{

	public CommandGroupHeadnamePrefix(final CrazyChats plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCHATS.COMMAND.GROUP.HEADNAMEPREFIX.DELETED $Group$", "CRAZYCHATS.COMMAND.GROUP.HEADNAMEPREFIX.SET $Group$ $HeadnamePrefix$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length == 1)
		{
			plugin.getGroupHeadnamePrefixes().remove(args[0]);
			plugin.sendLocaleMessage("COMMAND.GROUP.HEADNAMEPREFIX.DELETED", sender, args[0]);
			plugin.saveConfiguration();
		}
		else if (args.length == 2)
		{
			plugin.getGroupHeadnamePrefixes().put(args[0], ChatHelper.colorise(args[1]));
			plugin.sendLocaleMessage("COMMAND.GROUP.HEADNAMEPREFIX.SET", sender, args[0], ChatHelper.colorise(args[1]) + sender.getName());
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
		for (final String group : plugin.getGroupHeadnamePrefixes().keySet())
			if (group.toLowerCase().startsWith(name))
				res.add(group);
		return res;
	}

	@Override
	@Permission("crazychats.group.headnameprefix")
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazychats.group.headnameprefix");
	}
}
