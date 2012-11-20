package de.st_ddt.crazyplugin.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPlayerDataPluginInterface;
import de.st_ddt.crazyplugin.comparator.PlayerDataComparator;
import de.st_ddt.crazyplugin.data.PlayerDataFilterInterface;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.Filter;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.Tabbed;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyPlayerDataPluginCommandPlayerList<T extends PlayerDataInterface> extends CrazyPlayerDataCommandExecutor<T, CrazyPlayerDataPluginInterface<T, ? extends T>>
{

	private final Collection<? extends PlayerDataFilterInterface<T>> availableFilters;
	private final Map<String, PlayerDataComparator<T>> availableSorters;
	private final PlayerDataComparator<T> defaultSort;
	private final ListFormat format;

	public CrazyPlayerDataPluginCommandPlayerList(final CrazyPlayerDataPluginInterface<T, ? extends T> plugin)
	{
		super(plugin);
		this.availableFilters = plugin.getPlayerDataFilters();
		this.availableSorters = plugin.getPlayerDataComparators();
		this.defaultSort = plugin.getPlayerDataDefaultComparator();
		this.format = plugin.getPlayerDataListFormat();
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final List<T> list;
		synchronized (plugin.getPlayerDataLock())
		{
			list = new ArrayList<T>(plugin.getPlayerData());
		}
		ChatHelperExtended.processFullListCommand(sender, args, plugin.getChatHeader(), format, Filter.getFilterInstances(availableFilters), availableSorters, defaultSort, plugin.getPlayerDataListModder(), list);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final Map<String, Tabbed> params = new HashMap<String, Tabbed>();
		Tabbed pageTab = ChatHelperExtended.listTabHelp(params, sender, availableFilters, availableSorters);
		return ChatHelperExtended.tabHelp(args, params, pageTab);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".player.list");
	}
}
