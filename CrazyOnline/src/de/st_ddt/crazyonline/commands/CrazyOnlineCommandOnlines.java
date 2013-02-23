package de.st_ddt.crazyonline.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyonline.CrazyOnline;
import de.st_ddt.crazyonline.data.OnlineData;
import de.st_ddt.crazyonline.data.comparator.OnlineDataLastLogoutComperator;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.Filter;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyOnlineCommandOnlines extends CrazyOnlineCommandExecutor
{

	public CrazyOnlineCommandOnlines(final CrazyOnline plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYONLINE.COMMAND.ONLINES.HEADER $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$", "CRAZYONLINE.COMMAND.ONLINES.LISTFORMAT $Index$ $Entry$ $ChatHeader$", "CRAZYONLINE.COMMAND.ONLINES.ENTRYFORMAT $Name$ $FirstLogin$ $LastLogin$ $LastLogout$ $TimeTotalValue$ $IP$ $TimeTotalText$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		ChatHelperExtended.processFullListCommand(sender, args, plugin.getChatHeader(), plugin.getLocale().getLanguageEntry("COMMAND.ONLINES.HEADER").getLanguageText(sender), plugin.getLocale().getLanguageEntry("COMMAND.ONLINES.LISTFORMAT").getLanguageText(sender), plugin.getLocale().getLanguageEntry("COMMAND.ONLINES.ENTRYFORMAT").getLanguageText(sender), Filter.getFilterInstances(plugin.getPlayerDataFilters()), plugin.getPlayerDataComparators(), new OnlineDataLastLogoutComperator(), null, new ArrayList<OnlineData>(plugin.getOnlinePlayerDatas()));
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyonline.list");
	}
}
