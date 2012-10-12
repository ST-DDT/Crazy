package de.st_ddt.crazyonline.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyonline.CrazyOnline;
import de.st_ddt.crazyonline.data.OnlineData;
import de.st_ddt.crazyonline.data.comparator.OnlineDataTimeTotalOnlineComparator;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.Filter;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyOnlineCommandTop10 extends CrazyOnlineCommandExecutor
{

	public CrazyOnlineCommandTop10(final CrazyOnline plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYONLINE.COMMAND.TOP10.HEADER $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$", "CRAZYONLINE.COMMAND.TOP10.LISTFORMAT $Index$ $Entry$ $ChatHeader$", "CRAZYONLINE.COMMAND.TOP10.ENTRYFORMAT $Name$ $FirstLogin$ $LastLogin$ $LastLogout$ $TimeTotalValue$ $IP$ $TimeTotalText$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!sender.hasPermission("crazyonline.top10"))
			throw new CrazyCommandPermissionException();
		ChatHelperExtended.processFullListCommand(sender, args, plugin.getChatHeader(), plugin.getLocale().getLanguageEntry("COMMAND.TOP10.HEADER").getLanguageText(sender), plugin.getLocale().getLanguageEntry("COMMAND.TOP10.LISTFORMAT").getLanguageText(sender), plugin.getLocale().getLanguageEntry("COMMAND.TOP10.ENTRYFORMAT").getLanguageText(sender), Filter.getFilterInstances(plugin.getPlayerDataFilters()), plugin.getPlayerDataComparators(), new OnlineDataTimeTotalOnlineComparator(), null, new ArrayList<OnlineData>(plugin.getPlayerData()));
	}
}
