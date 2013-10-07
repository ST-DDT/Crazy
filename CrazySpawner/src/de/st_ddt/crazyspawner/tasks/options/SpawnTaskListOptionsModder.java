package de.st_ddt.crazyspawner.tasks.options;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.tasks.TimerSpawnTask;
import de.st_ddt.crazyspawner.tasks.comparator.SpawnTaskRangeComparator;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.Filter;
import de.st_ddt.crazyutil.FilterInterface;
import de.st_ddt.crazyutil.FilterInterface.FilterInstanceInterface;
import de.st_ddt.crazyutil.ListOptionsModder;
import de.st_ddt.crazyutil.Tabbed;
import de.st_ddt.crazyutil.paramitrisable.CreatureParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.LocationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.NamedEntitySpawnerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;

public class SpawnTaskListOptionsModder implements ListOptionsModder<TimerSpawnTask>
{

	private final CreatureParamitrisable creature = new CreatureParamitrisable(null);
	private final NamedEntitySpawnerParamitrisable type = new NamedEntitySpawnerParamitrisable();
	private final LocationParamitrisable location;
	private final DoubleParamitrisable range;
	private final SpawnTaskRangeComparator rangeSort;

	public SpawnTaskListOptionsModder(final SpawnTaskRangeComparator rangeSort, final Double defaultRange, final CommandSender sender)
	{
		super();
		this.rangeSort = rangeSort;
		location = new LocationParamitrisable(sender);
		range = new DoubleParamitrisable(defaultRange)
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

	public Collection<FilterInstanceInterface<TimerSpawnTask>> getFilters()
	{
		final List<FilterInstanceInterface<TimerSpawnTask>> res = new ArrayList<FilterInterface.FilterInstanceInterface<TimerSpawnTask>>();
		res.add(new Filter.DeafFilterInstance<TimerSpawnTask>()
		{

			@Override
			public boolean isActive()
			{
				return creature.getValue() != null;
			}

			@Override
			public boolean filter(final TimerSpawnTask data)
			{
				return creature.getValue() == data.getType().getType();
			}
		});
		res.add(new Filter.DeafFilterInstance<TimerSpawnTask>()
		{

			@Override
			public boolean isActive()
			{
				return type.getValue() != null;
			}

			@Override
			public boolean filter(final TimerSpawnTask data)
			{
				return type.getValue() == data.getType();
			}
		});
		res.add(new Filter.DeafFilterInstance<TimerSpawnTask>()
		{

			@Override
			public boolean isActive()
			{
				return location.getValue() != null && location.getValue().getWorld() != null;
			}

			@Override
			public boolean filter(final TimerSpawnTask data)
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
	public void modListPreOptions(final Map<String, Paramitrisable> params, final List<TimerSpawnTask> datas)
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
	public String[] modListPostOptions(final List<TimerSpawnTask> datas, final String[] pipeArgs) throws CrazyException
	{
		rangeSort.setLocation(location.getValue());
		if (pipeArgs == null)
			return null;
		if (pipeArgs.length == 1)
			if (pipeArgs[0].equalsIgnoreCase("thunder") || pipeArgs[0].equalsIgnoreCase("strike"))
				return ChatHelperExtended.appendArray(pipeArgs, "x:$2$", "y:$3$", "z:$4$", "w:$1$");
			else if (pipeArgs[0].equalsIgnoreCase("blink"))
				return ChatHelperExtended.appendArray(pipeArgs, Material.MOB_SPAWNER.name(), "x:$2$", "y:$3$", "z:$4$", "w:$1$", "r:5", "i:1s");
		return pipeArgs;
	}
}
