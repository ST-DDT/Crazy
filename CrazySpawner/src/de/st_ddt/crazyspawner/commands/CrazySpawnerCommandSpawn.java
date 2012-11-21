package de.st_ddt.crazyspawner.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.tasks.SpawnTask;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.CreatureParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.DurationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.LocationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public class CrazySpawnerCommandSpawn extends CrazySpawnerCommandExecutor
{

	public CrazySpawnerCommandSpawn(final CrazySpawner plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYSPAWNER.COMMAND.SPAWNED $Type$ $Amount$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final Map<String, TabbedParamitrisable> params = new TreeMap<String, TabbedParamitrisable>();
		final CreatureParamitrisable creature = new CreatureParamitrisable(null);
		params.put("", creature);
		params.put("c", creature);
		params.put("creature", creature);
		final LocationParamitrisable location = new LocationParamitrisable(sender);
		location.addFullParams(params, "l", "loc", "location");
		location.addAdvancedParams(params, "");
		final IntegerParamitrisable amount = new IntegerParamitrisable(1)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				if (value < 0)
					throw new CrazyCommandParameterException(0, "positive Number (Integer)");
			}
		};
		params.put("a", amount);
		params.put("amount", amount);
		final DurationParamitrisable delay = new DurationParamitrisable(0L)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				if (value < 0)
					throw new CrazyCommandParameterException(0, "positive amount of time");
			}
		};
		params.put("d", delay);
		params.put("delay", delay);
		final DurationParamitrisable interval = new DurationParamitrisable(1000L)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				if (value <= 0)
					throw new CrazyCommandParameterException(0, "positive amount of time");
			}
		};
		params.put("i", interval);
		params.put("interval", interval);
		final IntegerParamitrisable repeat = new IntegerParamitrisable(0)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				if (value < -1)
					throw new CrazyCommandParameterException(0, "positive Number (Integer)", "-1=infinite");
			}
		};
		params.put("r", repeat);
		params.put("repeat", repeat);
		final IntegerParamitrisable creatureMaxCount = new IntegerParamitrisable(0)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				if (value < -1)
					throw new CrazyCommandParameterException(0, "positive Number (Integer)", "0=unlimited");
			}
		};
		params.put("m", creatureMaxCount);
		params.put("max", creatureMaxCount);
		params.put("creaturecount", creatureMaxCount);
		params.put("creaturemaxcount", creatureMaxCount);
		final DoubleParamitrisable creatureRange = new DoubleParamitrisable(16D)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				if (value <= 0)
					throw new CrazyCommandParameterException(0, "positive Number (Double)");
			}
		};
		params.put("creaturerange", creatureRange);
		final IntegerParamitrisable playerCount = new IntegerParamitrisable(0)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				if (value < 0)
					throw new CrazyCommandParameterException(0, "positive Number (Integer)");
			}
		};
		params.put("min", playerCount);
		params.put("playercount", playerCount);
		params.put("playermincount", playerCount);
		final DoubleParamitrisable playerRange = new DoubleParamitrisable(16D)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				if (value <= 0)
					throw new CrazyCommandParameterException(0, "positive Number (Double)");
			}
		};
		params.put("playerrange", playerRange);
		ChatHelperExtended.readParameters(args, params);
		final SpawnTask task = new SpawnTask(plugin, creature.getValue(), location.getValue(), amount.getValue(), interval.getValue() / 50, repeat.getValue(), creatureMaxCount.getValue(), creatureRange.getValue(), playerCount.getValue(), playerRange.getValue());
		plugin.addSpawnTask(task);
		Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, task, delay.getValue() / 50);
		plugin.sendLocaleMessage("COMMAND.SPAWNED", sender, creature.getValue().getName(), amount);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final List<String> res = new LinkedList<String>();
		final String arg = args[args.length - 1].toLowerCase();
		for (final String name : CreatureParamitrisable.CREATURE_NAMES)
			if (name.toLowerCase().startsWith(arg))
				res.add(name);
		if ("location:".startsWith(arg))
			res.add("location:");
		if ("amount:".startsWith(arg))
			res.add("amount:");
		if ("delay:".startsWith(arg))
			res.add("delay:");
		if ("interval:".startsWith(arg))
			res.add("interval:");
		if ("repeat:".startsWith(arg))
			res.add("repeat:");
		if ("creaturemaxcount:".startsWith(arg))
			res.add("creaturemaxcount:");
		if ("creaturerange:".startsWith(arg))
			res.add("creaturerange:");
		if ("playermincount:".startsWith(arg))
			res.add("playermincount:");
		if ("playerrange:".startsWith(arg))
			res.add("playerrange:");
		return res;
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyspawner.spawn");
	}
}
