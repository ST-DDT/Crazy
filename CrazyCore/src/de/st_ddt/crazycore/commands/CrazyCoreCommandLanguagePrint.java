package de.st_ddt.crazycore.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class CrazyCoreCommandLanguagePrint extends CrazyCoreCommandExecutor
{

	public CrazyCoreCommandLanguagePrint(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!sender.hasPermission("crazylanguage.advanced"))
			throw new CrazyCommandPermissionException();
		if (args.length != 1)
			throw new CrazyCommandUsageException("<Path/*>");
		if (args[0].equalsIgnoreCase("*"))
			CrazyLocale.printAll(sender);
		else
			CrazyLocale.getLocaleHead().getLanguageEntry(args[0]).print(sender);
	}
}
