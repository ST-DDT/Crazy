package de.st_ddt.crazycore.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyCoreCommandLanguageSetDefault extends CrazyCoreCommandExecutor
{

	public CrazyCoreCommandLanguageSetDefault(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYCORE.COMMAND.LANGUAGE.DEFAULT.SET $LanguageName$ $Language$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!PermissionModule.hasPermission(sender, "crazylanguage.advanced"))
			throw new CrazyCommandPermissionException();
		if (args.length != 1)
			throw new CrazyCommandUsageException("<Language>");
		final String language = args[0].toLowerCase();
		if (!CrazyLocale.PATTERN_LANGUAGE.matcher(language).matches())
			throw new CrazyCommandNoSuchException("Language", args[0], CrazyLocale.getActiveLanguagesNames(true));
		if (!CrazyLocale.getDefaultLanguage().equals(language))
		{
			CrazyLocale.setDefaultLanguage(language);
			plugin.loadLanguageFiles(language, true);
		}
		plugin.save();
		plugin.sendLocaleMessage("COMMAND.LANGUAGE.DEFAULT.SET", sender, CrazyLocale.getSaveLanguageName(language), language);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length == 0)
			return new ArrayList<String>(plugin.getPreloadedLanguages());
		if (args.length > 1)
			return null;
		final List<String> res = new ArrayList<String>();
		final String arg = args[0];
		final Pattern pattern = Pattern.compile(arg, Pattern.CASE_INSENSITIVE);
		for (final String subCommand : plugin.getPreloadedLanguages())
			if (pattern.matcher(subCommand).find())
				res.add(subCommand);
		return res;
	}
}
