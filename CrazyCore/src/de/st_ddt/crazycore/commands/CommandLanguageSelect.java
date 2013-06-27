package de.st_ddt.crazycore.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.source.Localized;

public class CommandLanguageSelect extends CommandExecutor
{

	public CommandLanguageSelect(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYCORE.COMMAND.LANGUAGE.CHANGED $LanguageName$ $Language$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("[Language]");
		final String language = args[0].toLowerCase();
		if (!CrazyLocale.PATTERN_LANGUAGE.matcher(language).matches())
			throw new CrazyCommandNoSuchException("Language", args[0], CrazyLocale.getActiveLanguagesNames(true));
		if (plugin.isLoadingUserLanguagesEnabled())
			plugin.loadLanguageFiles(language, false);
		CrazyLocale.setUserLanguage(sender, language);
		plugin.save();
		plugin.sendLocaleMessage("COMMAND.LANGUAGE.CHANGED", sender, CrazyLocale.getSaveLanguageName(language), language);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length != 1)
			return null;
		final List<String> res = new ArrayList<String>();
		final String arg = args[0];
		final Pattern pattern = Pattern.compile(arg, Pattern.CASE_INSENSITIVE);
		for (final String language : plugin.getPreloadedLanguages())
			if (pattern.matcher(language).find() || pattern.matcher(CrazyLocale.getSaveLanguageName(language)).find())
				res.add(language);
		return res;
	}
}
