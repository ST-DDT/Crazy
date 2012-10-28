package de.st_ddt.crazycore.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyCoreCommandLanguagePrint extends CrazyCoreCommandExecutor
{

	public CrazyCoreCommandLanguagePrint(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("<Path/*>");
		if (args[0].equalsIgnoreCase("*"))
			CrazyLocale.printAll(sender);
		else
			CrazyLocale.getLocaleHead().getLanguageEntry(args[0]).print(sender);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length == 0)
			return new ArrayList<String>(CrazyLocale.getLocaleHead().keySet());
		if (args.length != 1)
			return null;
		final List<String> res = new ArrayList<String>();
		final String path = args[0].toUpperCase();
		final CrazyLocale pathLocale = CrazyLocale.getLocaleHead().getLanguageEntry(args[0]);
		if (pathLocale != null)
			for (final String sub : pathLocale.keySet())
				res.add(path + "." + sub);
		if (path.contains("."))
		{
			String part1 = path.substring(0, path.lastIndexOf('.'));
			final CrazyLocale subLocale = CrazyLocale.getLocaleHead().getLanguageEntry(part1);
			part1 += ".";
			final String part2 = path.substring(path.lastIndexOf('.') + 1);
			if (subLocale != null)
				for (final String sub : CrazyLocale.getLocaleHead().keySet())
					if (sub.startsWith(part2))
						res.add(part1 + sub);
		}
		else
			for (final String sub : CrazyLocale.getLocaleHead().keySet())
				if (sub.startsWith(path))
					res.add(sub);
		return res;
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazylanguage.advanced");
	}
}
