package de.st_ddt.crazycore.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyCoreCommandLanguageLink extends CrazyCoreCommandExecutor
{

	public CrazyCoreCommandLanguageLink(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYCORE.COMMAND.LANGUAGE.LINK")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!sender.hasPermission("crazylanguage.advanced"))
			throw new CrazyCommandPermissionException();
		if (args.length != 1)
			throw new CrazyCommandUsageException("<Path>");
		plugin.sendLocaleMessage("COMMAND.LANGUAGE.LINK", sender);
		final CrazyLocale locale = CrazyLocale.getLocaleHead().getLanguageEntry(args[0]);
		if (locale.getAlternative() == null)
			sender.sendMessage(locale.getPath() + " <= (null)");
		else
			sender.sendMessage(locale.getPath() + " <= " + locale.getAlternative().getPath());
	}
}
