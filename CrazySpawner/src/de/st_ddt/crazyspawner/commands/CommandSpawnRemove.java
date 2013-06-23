package de.st_ddt.crazyspawner.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.tasks.SpawnTask;
import de.st_ddt.crazyspawner.tasks.comparator.SpawnTaskRangeComparator;
import de.st_ddt.crazyspawner.tasks.comparator.SpawnTaskTypeComparator;
import de.st_ddt.crazyspawner.tasks.options.SpawnTaskListOptionsModder;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.Tabbed;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;

public class CommandSpawnRemove extends CommandExecutor
{

	private final Map<String, Comparator<SpawnTask>> sorters = new TreeMap<String, Comparator<SpawnTask>>();
	final SpawnTaskRangeComparator rangeSort = new SpawnTaskRangeComparator();
	private final ListFormat listFormat = new ListFormat()
	{

		@Override
		@Localized("CRAZYSPAWNER.COMMAND.REMOVE.LISTFORMAT $Index$ $Entry$ $ChatHeader$")
		public String listFormat(final CommandSender target)
		{
			return plugin.getLocale().getLocaleMessage(target, "COMMAND.REMOVE.LISTFORMAT");
		}

		@Override
		@Localized("CRAZYSPAWNER.COMMAND.REMOVE.HEADFORMAT $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$")
		public String headFormat(final CommandSender target)
		{
			return plugin.getLocale().getLocaleMessage(target, "COMMAND.REMOVE.HEADFORMAT");
		}

		@Override
		@Localized("CRAZYSPAWNER.COMMAND.REMOVE.ENTRYFORMAT $Type$ $World$ $X$ $Y$ $Z$ $Distance$ $IntervalTicks$ $IntervalText$ $Repeat$ $Amount$")
		public String entryFormat(final CommandSender target)
		{
			return plugin.getLocale().getLocaleMessage(target, "COMMAND.REMOVE.ENTRYFORMAT");
		}
	};

	public CommandSpawnRemove(final CrazySpawner plugin)
	{
		super(plugin);
		final Comparator<SpawnTask> typeSort = new SpawnTaskTypeComparator();
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
		if (sender instanceof ConsoleCommandSender)
			if (args.length == 0)
				throw new CrazyCommandUsageException("[creature:EntityType] [type:ExtendedCreatureType] [location:Location [range:Double]]");
		final SpawnTaskListOptionsModder modder = new SpawnTaskListOptionsModder(rangeSort, 2D, sender);
		final List<SpawnTask> list = new ArrayList<SpawnTask>(plugin.getTasks());
		ChatHelperExtended.processListCommand(sender, args, plugin.getChatHeader(), listFormat, modder.getFilters(), sorters, rangeSort, modder, list);
		plugin.getTasks().removeAll(list);
		for (final SpawnTask task : list)
			task.cancel();
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final Map<String, Tabbed> params = new TreeMap<String, Tabbed>();
		final SpawnTaskListOptionsModder modder = new SpawnTaskListOptionsModder(rangeSort, 2D, sender);
		final Tabbed page = ChatHelperExtended.listTabHelp(params, sender, null, sorters);
		modder.tabHelp(params);
		return ChatHelperExtended.tabHelp(args, params, page);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyspawner.remove");
	}
}
