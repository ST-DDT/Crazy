package de.st_ddt.crazycore.commands;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyCoreCommandLanguageExtract extends CrazyCoreCommandExecutor
{

	public CrazyCoreCommandLanguageExtract(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCORE.COMMAND.LANGUAGE.EXTRACTED $Language$", "CRAZYCORE.COMMAND.LANGUAGE.EXTRACTED.PLUGIN $Language$ $Plugin$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!PermissionModule.hasPermission(sender, "crazylanguage.advanced"))
			throw new CrazyCommandPermissionException();
		if (args.length == 0)
			throw new CrazyCommandUsageException("<Language>", "<Plugin>", "*");
		final String name = args[0].toLowerCase();
		if (name.equalsIgnoreCase("*"))
		{
			for (final String language : CrazyLocale.getLoadedLanguages())
			{
				for (final CrazyPlugin plugin : CrazyPlugin.getCrazyPlugins())
				{
					plugin.unpackLanguage(language);
					plugin.loadLanguage(language, sender);
					plugin.checkLocale();
				}
				plugin.sendLocaleMessage("COMMAND.LANGUAGE.EXTRACTED", sender, language);
			}
			return;
		}
		if (CrazyLocale.PATTERN_LANGUAGE.matcher(name).matches())
		{
			for (final CrazyPlugin plugin : CrazyPlugin.getCrazyPlugins())
			{
				plugin.unpackLanguage(name, sender);
				plugin.loadLanguage(name, sender);
				plugin.checkLocale();
			}
			plugin.sendLocaleMessage("COMMAND.LANGUAGE.EXTRACTED", sender, name);
			return;
		}
		final CrazyPlugin plugin = CrazyPlugin.getPlugin(name);
		if (plugin == null)
		{
			final LinkedHashSet<String> alternatives = new LinkedHashSet<String>();
			alternatives.addAll(CrazyLocale.getLoadedLanguages());
			for (final CrazyPlugin temp : CrazyPlugin.getCrazyPlugins())
				alternatives.add(temp.getName());
			throw new CrazyCommandNoSuchException("Languages/Plugins", name, alternatives);
		}
		else
			for (final String language : CrazyLocale.getLoadedLanguages())
			{
				plugin.unpackLanguage(language, sender);
				plugin.loadLanguage(language, sender);
				plugin.checkLocale();
				plugin.sendLocaleMessage("COMMAND.LANGUAGE.EXTRACTED.PLUGIN", sender, language, plugin.getName());
			}
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final List<String> res = new ArrayList<String>();
		String arg = "";
		if (args.length > 0)
			arg = args[args.length - 1];
		final Pattern pattern = Pattern.compile(arg, Pattern.CASE_INSENSITIVE);
		for (final String subCommand : CrazyLocale.getActiveLanguages())
			if (pattern.matcher(subCommand).find())
				res.add(subCommand);
		for (final CrazyPlugin plugin : CrazyPlugin.getCrazyPlugins())
			if (pattern.matcher(plugin.getName()).find())
				res.add(plugin.getName());
		return res;
	}
}
