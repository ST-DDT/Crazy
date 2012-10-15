package de.st_ddt.crazyonline.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyonline.CrazyOnline;
import de.st_ddt.crazyonline.data.OnlineData;
import de.st_ddt.crazyonline.data.comparator.OnlineDataLastLogoutComperator;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.Filter;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyOnlineCommandOnlines extends CrazyOnlineCommandExecutor
{

	public CrazyOnlineCommandOnlines(CrazyOnline plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYONLINE.COMMAND.ONLINES.HEADER $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$", "CRAZYONLINE.COMMAND.ONLINES.LISTFORMAT $Index$ $Entry$ $ChatHeader$", "CRAZYONLINE.COMMAND.ONLINES.ENTRYFORMAT $Name$ $FirstLogin$ $LastLogin$ $LastLogout$ $TimeTotalValue$ $IP$ $TimeTotalText$" })
	public void command(CommandSender sender, String[] args) throws CrazyException
	{
		if (!sender.hasPermission("crazyonline.list"))
			throw new CrazyCommandPermissionException();
		ChatHelperExtended.processFullListCommand(sender, args, plugin.getChatHeader(), plugin.getLocale().getLanguageEntry("COMMAND.ONLINES.HEADER").getLanguageText(sender), plugin.getLocale().getLanguageEntry("COMMAND.ONLINES.LISTFORMAT").getLanguageText(sender), plugin.getLocale().getLanguageEntry("COMMAND.ONLINES.ENTRYFORMAT").getLanguageText(sender), Filter.getFilterInstances(plugin.getPlayerDataFilters()), plugin.getPlayerDataComparators(), new OnlineDataLastLogoutComperator(), null, new ArrayList<OnlineData>(plugin.getOnlinePlayerDatas()));
	}
}
