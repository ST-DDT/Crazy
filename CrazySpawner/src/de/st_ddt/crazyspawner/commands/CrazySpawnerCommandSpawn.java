package de.st_ddt.crazyspawner.commands;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.tasks.SpawnTask;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.DurationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.ExtendedCreatureParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.LocationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

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
		final ExtendedCreatureParamitrisable creature = new ExtendedCreatureParamitrisable();
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
		params.put("cmax", creatureMaxCount);
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
		params.put("cr", creatureRange);
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
		params.put("pmin", playerCount);
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
		params.put("pr", playerRange);
		params.put("playerrange", playerRange);
		ChatHelperExtended.readParameters(args, params, creature, amount, repeat, interval, delay);
		if (creature.getValue() == null)
			throw new CrazyCommandUsageException("<creature:Creature> [amount:Integer] [repeat:Integer] [interval:Duration] [delay:Duration] [creaturemaxcount:Integer [creaturerange:Double]] [playermincount:Integer [playerrange:Double]] [location:Location]");
		final SpawnTask task = new SpawnTask(plugin, creature.getValue(), location.getValue(), amount.getValue(), interval.getValue() / 50, repeat.getValue(), creatureMaxCount.getValue(), creatureRange.getValue(), playerCount.getValue(), playerRange.getValue());
		plugin.addSpawnTask(task);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, task, delay.getValue() / 50);
		plugin.sendLocaleMessage("COMMAND.SPAWNED", sender, creature.getValue().getName(), amount);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final Map<String, TabbedParamitrisable> params = new TreeMap<String, TabbedParamitrisable>();
		final ExtendedCreatureParamitrisable creature = new ExtendedCreatureParamitrisable();
		params.put("c", creature);
		params.put("creature", creature);
		final LocationParamitrisable location = new LocationParamitrisable(sender);
		location.addFullParams(params, "l", "loc", "location");
		location.addAdvancedParams(params, "");
		final IntegerParamitrisable amount = new IntegerParamitrisable(1);
		params.put("a", amount);
		params.put("amount", amount);
		final DurationParamitrisable delay = new DurationParamitrisable(0L);
		params.put("d", delay);
		params.put("delay", delay);
		final DurationParamitrisable interval = new DurationParamitrisable(1000L);
		params.put("i", interval);
		params.put("interval", interval);
		final IntegerParamitrisable repeat = new IntegerParamitrisable(0);
		params.put("r", repeat);
		params.put("repeat", repeat);
		final IntegerParamitrisable creatureMaxCount = new IntegerParamitrisable(0);
		params.put("m", creatureMaxCount);
		params.put("max", creatureMaxCount);
		params.put("creaturecount", creatureMaxCount);
		params.put("creaturemaxcount", creatureMaxCount);
		final DoubleParamitrisable creatureRange = new DoubleParamitrisable(16D);
		params.put("creaturerange", creatureRange);
		final IntegerParamitrisable playerCount = new IntegerParamitrisable(0);
		params.put("min", playerCount);
		params.put("playercount", playerCount);
		params.put("playermincount", playerCount);
		final DoubleParamitrisable playerRange = new DoubleParamitrisable(16D);
		params.put("playerrange", playerRange);
		return ChatHelperExtended.tabHelp(args, params, creature, amount, repeat, interval);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyspawner.spawn");
	}
}
