package de.st_ddt.crazycore.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazycore.CrazyCore;
import de.st_ddt.crazyplugin.CrazyLightPlugin;
import de.st_ddt.crazyplugin.comparator.DepenciesComparator;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.Tabbed;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

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
		ChatHelperExtended.processFullListCommand(sender, args, plugin.getChatHeader(), plugin.getLocale().getLanguageEntry("COMMAND.PLUGINLIST.HEADER").getLanguageText(sender), plugin.getLocale().getLanguageEntry("COMMAND.PLUGINLIST.LISTFORMAT").getLanguageText(sender), plugin.getLocale().getLanguageEntry("COMMAND.PLUGINLIST.ENTRYFORMAT").getLanguageText(sender), null, null, new DepenciesComparator<CrazyLightPlugin>(), null, new ArrayList<CrazyLightPlugin>(CrazyLightPlugin.getCrazyLightPlugins()));
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final Map<String, Tabbed> params = new HashMap<String, Tabbed>();
		final Tabbed pageTab = ChatHelperExtended.listTabHelp(params, sender, null, null);
		return ChatHelperExtended.tabHelpWithPipe(sender, args, params, pageTab);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazycore.list");
	}
}
