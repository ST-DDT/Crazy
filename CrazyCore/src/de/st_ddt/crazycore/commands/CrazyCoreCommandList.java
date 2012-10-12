package de.st_ddt.crazycore.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.CrazyLightPlugin;
import de.st_ddt.crazyplugin.comparator.DepenciesComparator;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyCoreCommandList extends CrazyCoreCommandExecutor
{

	public CrazyCoreCommandList(final CrazyCore plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYCORE.COMMAND.PLUGINLIST.HEADER $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$", "CRAZYCORE.COMMAND.PLUGINLIST.LISTFORMAT $Index$ $Entry$ $ChatHeader$", "CRAZYCORE.COMMAND.PLUGINLIST.ENTRYFORMAT $Name$ $ChatHeader$ $Version$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!sender.hasPermission("crazycore.list"))
			throw new CrazyCommandPermissionException();
		ChatHelperExtended.processFullListCommand(sender, args, plugin.getChatHeader(), plugin.getLocale().getLanguageEntry("COMMAND.PLUGINLIST.HEADER").getLanguageText(sender), plugin.getLocale().getLanguageEntry("COMMAND.PLUGINLIST.LISTFORMAT").getLanguageText(sender), plugin.getLocale().getLanguageEntry("COMMAND.PLUGINLIST.ENTRYFORMAT").getLanguageText(sender), null, null, new DepenciesComparator<CrazyLightPlugin>(), null, new ArrayList<CrazyLightPlugin>(CrazyLightPlugin.getCrazyLightPlugins()));
	}
}
