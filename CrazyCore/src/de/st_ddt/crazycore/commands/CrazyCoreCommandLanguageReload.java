package de.st_ddt.crazycore.commands;

import java.util.LinkedHashSet;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyCoreCommandLanguageReload extends CrazyCoreCommandExecutor
{

	public CrazyCoreCommandLanguageReload(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCORE.COMMAND.LANGUAGE.RELOADED $Language$", "CRAZYCORE.COMMAND.LANGUAGE.RELOADED.PLUGIN $Language$ $Plugin$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!sender.hasPermission("crazylanguage.advanced"))
			throw new CrazyCommandPermissionException();
		if (args.length == 0)
			throw new CrazyCommandUsageException("<Plugin/Language/*>");
		final String name = args[0].toLowerCase();
		if (name.equals("*"))
		{
			for (final String language : CrazyLocale.getLoadedLanguages())
			{
				for (final CrazyPlugin plugin : CrazyPlugin.getCrazyPlugins())
				{
					plugin.loadLanguage(language, sender);
					plugin.checkLocale();
				}
				plugin.sendLocaleMessage("COMMAND.LANGUAGE.RELOADED", sender, language);
			}
			return;
		}
		if (name.matches("[a-z][a-z]_[a-z][a-z]"))
		{
			for (final CrazyPlugin plugin : CrazyPlugin.getCrazyPlugins())
			{
				plugin.loadLanguage(name, sender);
				plugin.checkLocale();
			}
			plugin.sendLocaleMessage("COMMAND.LANGUAGE.RELOADED", sender, name);
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
		for (final String language : CrazyLocale.getLoadedLanguages())
		{
			plugin.loadLanguage(language, sender);
			plugin.checkLocale();
			plugin.sendLocaleMessage("COMMAND.LANGUAGE.RELOADED.PLUGIN", sender, language, plugin.getName());
		}
		return;
	}
}
