package de.st_ddt.crazyspawner.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.tasks.TimerSpawnTask;
import de.st_ddt.crazyspawner.tasks.comparator.SpawnTaskRangeComparator;
import de.st_ddt.crazyspawner.tasks.comparator.SpawnTaskTypeComparator;
import de.st_ddt.crazyspawner.tasks.options.SpawnTaskListOptionsModder;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.Tabbed;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;

public class CommandSpawnList extends CommandExecutor
{

	private final Map<String, Comparator<TimerSpawnTask>> sorters = new TreeMap<String, Comparator<TimerSpawnTask>>();
	final SpawnTaskRangeComparator rangeSort = new SpawnTaskRangeComparator();
	private final ListFormat listFormat = new ListFormat()
	{

		@Override
		@Localized("CRAZYSPAWNER.COMMAND.LIST.LISTFORMAT $Index$ $Entry$ $ChatHeader$")
		public String listFormat(final CommandSender target)
		{
			return plugin.getLocale().getLocaleMessage(target, "COMMAND.LIST.LISTFORMAT");
		}

		@Override
		@Localized("CRAZYSPAWNER.COMMAND.LIST.HEADFORMAT $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$")
		public String headFormat(final CommandSender target)
		{
			return plugin.getLocale().getLocaleMessage(target, "COMMAND.LIST.HEADFORMAT");
		}

		@Override
		@Localized("CRAZYSPAWNER.COMMAND.LIST.ENTRYFORMAT $Type$ $World$ $X$ $Y$ $Z$ $Distance$ $IntervalTicks$ $IntervalText$ $Repeat$ $Amount$")
		public String entryFormat(final CommandSender target)
		{
			return plugin.getLocale().getLocaleMessage(target, "COMMAND.LIST.ENTRYFORMAT");
		}
	};

	public CommandSpawnList(final CrazySpawner plugin)
	{
		super(plugin);
		final Comparator<TimerSpawnTask> typeSort = new SpawnTaskTypeComparator();
		sorters.put("t", typeSort);
		sorters.put("type", typeSort);
		sorters.put("c", typeSort);
		sorters.put("creature", typeSort);
		sorters.put("r", rangeSort);
		sorters.put("range", rangeSort);
		sorters.put("d", rangeSort);
		sorters.put("dist", rangeSort);
		sorters.put("distance", rangeSort);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final SpawnTaskListOptionsModder modder = new SpawnTaskListOptionsModder(rangeSort, sender instanceof Player ? 100D : null, sender);
		ChatHelperExtended.processFullListCommand(sender, args, plugin.getChatHeader(), listFormat, modder.getFilters(), sorters, rangeSort, modder, new ArrayList<TimerSpawnTask>(plugin.getTasks()));
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final Map<String, Tabbed> params = new TreeMap<String, Tabbed>();
		final SpawnTaskListOptionsModder modder = new SpawnTaskListOptionsModder(rangeSort, sender instanceof Player ? 100D : null, sender);
		final Tabbed page = ChatHelperExtended.listTabHelp(params, sender, null, sorters);
		modder.tabHelp(params);
		return ChatHelperExtended.tabHelpWithPipe(sender, args, params, page);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyspawner.list");
	}
}
