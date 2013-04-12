package de.st_ddt.crazyspawner.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.tasks.SpawnTask;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.Filter;
import de.st_ddt.crazyutil.FilterInterface;
import de.st_ddt.crazyutil.FilterInterface.FilterInstanceInterface;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.ListOptionsModder;
import de.st_ddt.crazyutil.Tabbed;
import de.st_ddt.crazyutil.paramitrisable.CreatureParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.ExtendedCreatureParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.LocationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class CommandSpawnList extends CommandExecutor
{

	private final Map<String, Comparator<SpawnTask>> sorters = new TreeMap<String, Comparator<SpawnTask>>();
	private final SpawnTaskRangeComparator rangeSort = new SpawnTaskRangeComparator();
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
		final SpawnTaskListOptionsModder modder = new SpawnTaskListOptionsModder(sender);
		ChatHelperExtended.processFullListCommand(sender, args, plugin.getChatHeader(), listFormat, modder.getFilters(), sorters, rangeSort, modder, new ArrayList<SpawnTask>(plugin.getTasks()));
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final Map<String, Tabbed> params = new TreeMap<String, Tabbed>();
		final SpawnTaskListOptionsModder modder = new SpawnTaskListOptionsModder(sender);
		final Tabbed page = ChatHelperExtended.listTabHelp(params, sender, null, sorters);
		modder.tabHelp(params);
		return ChatHelperExtended.tabHelp(args, params, page);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		// EDIT Implementiere CommandSpawnList.hasAccessPermission()
		return super.hasAccessPermission(sender);
	}

	private final class SpawnTaskRangeComparator implements Comparator<SpawnTask>
	{

		private Location location;

		public void setLocation(final Location location)
		{
			this.location = location;
		}

		@Override
		public int compare(final SpawnTask o1, final SpawnTask o2)
		{
			if (location == null)
				return o1.compareTo(o2);
			if (o1.getLocation().getWorld() == location.getWorld())
				if (o2.getLocation().getWorld() == location.getWorld())
				{
					final int res = Double.compare(location.distance(o1.getLocation()), location.distance(o2.getLocation()));
					if (res == 0)
						return o1.compareTo(o2);
					else
						return res;
				}
				else
					return -1;
			else if (o2.getLocation().getWorld() == location.getWorld())
				return 1;
			else
				return o1.compareTo(o2);
		}
	}

	private final class SpawnTaskTypeComparator implements Comparator<SpawnTask>
	{

		@Override
		public int compare(final SpawnTask o1, final SpawnTask o2)
		{
			final int res = o1.getType().getName().compareTo(o2.getType().getName());
			if (res == 0)
				return o1.compareTo(o2);
			else
				return res;
		}
	}

	private class SpawnTaskListOptionsModder implements ListOptionsModder<SpawnTask>
	{

		private final CreatureParamitrisable creature = new CreatureParamitrisable(null);
		private final ExtendedCreatureParamitrisable type = new ExtendedCreatureParamitrisable();
		private final LocationParamitrisable location;
		private final DoubleParamitrisable range;

		public SpawnTaskListOptionsModder(final CommandSender sender)
		{
			super();
			location = new LocationParamitrisable(sender);
			range = new DoubleParamitrisable(sender instanceof Player ? 100D : null)
			{

				@Override
				public void setParameter(final String parameter) throws CrazyException
				{
					super.setParameter(parameter);
					if (value <= 0)
						throw new CrazyCommandParameterException(0, "positive Number (Double)");
				}
			};
		}

		public Collection<FilterInstanceInterface<SpawnTask>> getFilters()
		{
			final List<FilterInstanceInterface<SpawnTask>> res = new ArrayList<FilterInterface.FilterInstanceInterface<SpawnTask>>();
			res.add(new Filter.DeafFilterInstance<SpawnTask>()
			{

				@Override
				public boolean isActive()
				{
					return creature.getValue() != null;
				}

				@Override
				public boolean filter(final SpawnTask data)
				{
					return creature.getValue() == data.getType().getType();
				}
			});
			res.add(new Filter.DeafFilterInstance<SpawnTask>()
			{

				@Override
				public boolean isActive()
				{
					return type.getValue() != null;
				}

				@Override
				public boolean filter(final SpawnTask data)
				{
					return type.getValue() == data.getType();
				}
			});
			res.add(new Filter.DeafFilterInstance<SpawnTask>()
			{

				@Override
				public boolean isActive()
				{
					return location.getValue() != null && location.getValue().getWorld() == null;
				}

				@Override
				public boolean filter(final SpawnTask data)
				{
					if (location.getValue().getWorld() == data.getLocation().getWorld())
						return location.getValue().distance(data.getLocation()) <= range.getValue();
					else
						return false;
				}
			});
			return res;
		}

		@Override
		public void modListPreOptions(final Map<String, Paramitrisable> params, final List<SpawnTask> datas)
		{
			params.put("c", creature);
			params.put("creature", creature);
			params.put("ct", creature);
			params.put("ctype", creature);
			params.put("creaturetype", creature);
			params.put("t", type);
			params.put("type", type);
			params.put("et", type);
			params.put("etype", type);
			params.put("extendedtype", type);
			location.addFullParams(params, "l", "loc", "location");
			location.addAdvancedParams(params, "");
			params.put("r", range);
			params.put("range", range);
			params.put("searchrange", range);
		}

		public void tabHelp(final Map<String, Tabbed> params)
		{
			params.put("c", creature);
			params.put("creature", creature);
			params.put("ct", creature);
			params.put("ctype", creature);
			params.put("creaturetype", creature);
			params.put("t", type);
			params.put("type", type);
			params.put("et", type);
			params.put("etype", type);
			params.put("extendedtype", type);
			location.addFullParams(params, "l", "loc", "location");
			location.addAdvancedParams(params, "");
			params.put("r", range);
			params.put("range", range);
			params.put("searchrange", range);
		}

		@Override
		public String[] modListPostOptions(final List<SpawnTask> datas, final String[] pipeArgs) throws CrazyException
		{
			rangeSort.setLocation(location.getValue());
			if (pipeArgs == null)
				return null;
			if (pipeArgs.length == 1)
				if (pipeArgs[0].equalsIgnoreCase("thunder") || pipeArgs[0].equalsIgnoreCase("strike"))
					return ChatHelperExtended.appendArray(pipeArgs, "x:$2$", "y:$3$", "z:$4$", "w:$1$");
				else if (pipeArgs[0].equalsIgnoreCase("blink"))
					return ChatHelperExtended.appendArray(pipeArgs, Integer.toString(Material.MOB_SPAWNER.getId()), "x:$2$", "y:$3$", "z:$4$", "w:$1$", "r:5", "i:1s");
			return pipeArgs;
		}
	}
}
