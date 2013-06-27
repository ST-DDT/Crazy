package de.st_ddt.crazycore.commands;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.CrazyPipe;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.source.Localized;

public class CommandLanguageList extends CommandExecutor
{

	public CommandLanguageList(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCORE.COMMAND.LANGUAGE.CURRENT $LanguageName$ $LanguageSelection$", "CRAZYCORE.COMMAND.LANGUAGE.DEFAULT $DefaultLanguageName$ $DefaultLanguageSelection$", "CRAZYCORE.COMMAND.LANGUAGE.LIST.HEADER $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$", "CRAZYCORE.COMMAND.LANGUAGE.LIST.LISTFORMAT $Index$ $Entry$ $ChatHeader$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length > 0)
			if (args[0].equals(">"))
			{
				final String[] newArgs = ChatHelperExtended.shiftArray(args, 1);
				for (final String name : CrazyLocale.getActiveLanguagesNames(false))
					CrazyPipe.pipe(sender, name, newArgs);
				return;
			}
			else
				throw new CrazyCommandUsageException("[> CrazyPipe]");
		plugin.sendLocaleMessage("COMMAND.LANGUAGE.CURRENT", sender, CrazyLocale.getLanguageName(), CrazyLocale.getUserLanguage(sender));
		plugin.sendLocaleMessage("COMMAND.LANGUAGE.DEFAULT", sender, CrazyLocale.getLanguageName().getDefaultLanguageText(), CrazyLocale.getDefaultLanguage());
		plugin.sendLocaleList(sender, "COMMAND.LANGUAGE.LIST.HEADER", "COMMAND.LANGUAGE.LIST.LISTFORMAT", null, -1, 1, CrazyLocale.getActiveLanguagesNames(true));
	}
}
