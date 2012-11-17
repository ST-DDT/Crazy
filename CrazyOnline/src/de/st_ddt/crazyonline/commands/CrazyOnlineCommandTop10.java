package de.st_ddt.crazyonline.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyonline.CrazyOnline;
import de.st_ddt.crazyonline.data.OnlineData;
import de.st_ddt.crazyonline.data.comparator.OnlineDataTimeTotalOnlineComparator;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.Filter;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

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
		final List<OnlineData> list;
		synchronized (plugin.getPlayerDataLock())
		{
			list = new ArrayList<OnlineData>(plugin.getPlayerData());
		}
		ChatHelperExtended.processFullListCommand(sender, args, plugin.getChatHeader(), plugin.getLocale().getLanguageEntry("COMMAND.TOP10.HEADER").getLanguageText(sender), plugin.getLocale().getLanguageEntry("COMMAND.TOP10.LISTFORMAT").getLanguageText(sender), plugin.getLocale().getLanguageEntry("COMMAND.TOP10.ENTRYFORMAT").getLanguageText(sender), Filter.getFilterInstances(plugin.getPlayerDataFilters()), plugin.getPlayerDataComparators(), new OnlineDataTimeTotalOnlineComparator(), null, list);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyonline.top10");
	}
}
