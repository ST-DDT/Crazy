package de.st_ddt.crazyspawner.commands;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.data.options.Thunder;
import de.st_ddt.crazyspawner.tasks.SpawnTask;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.ColoredStringParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.DurationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.ExtendedCreatureParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.LocationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.MultiParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class CommandSpawn extends CommandExecutor
{

	public CommandSpawn(final CrazySpawner plugin)
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
		final BooleanParamitrisable synced = new BooleanParamitrisable(false);
		params.put("s", synced);
		params.put("synced", synced);
		final IntegerParamitrisable chunkLoadRange = new IntegerParamitrisable(-1)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				if (value < -1)
					throw new CrazyCommandParameterException(0, "positive Number (Integer)", "0=unlimited");
			}
		};
		params.put("clr", chunkLoadRange);
		params.put("chunkloadrange", chunkLoadRange);
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
		params.put("cm", creatureMaxCount);
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
		params.put("crange", creatureRange);
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
		params.put("pm", playerCount);
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
		params.put("prange", playerRange);
		params.put("playerrange", playerRange);
		final DoubleParamitrisable blockingRange = new DoubleParamitrisable(0D)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				if (value < 0)
					throw new CrazyCommandParameterException(0, "positive Number (Double)");
			}
		};
		params.put("br", blockingRange);
		params.put("brange", blockingRange);
		params.put("blockingrange", blockingRange);
		final MultiParamitrisable<Long> countDownTimes = new MultiParamitrisable<Long>(new DurationParamitrisable(null));
		params.put("cdt", countDownTimes);
		params.put("cdtime", countDownTimes);
		params.put("countdowntime", countDownTimes);
		final ColoredStringParamitrisable countDownMessage = new ColoredStringParamitrisable(null);
		params.put("cdm", countDownMessage);
		params.put("cdmessage", countDownMessage);
		params.put("countdownmessage", countDownMessage);
		final BooleanParamitrisable countDownBroadcast = new BooleanParamitrisable(false);
		params.put("cdb", countDownBroadcast);
		params.put("cdbroadcast", countDownBroadcast);
		params.put("countdownbroadcast", countDownBroadcast);
		final BooleanParamitrisable allowDespawn = new BooleanParamitrisable(false);
		params.put("ad", allowDespawn);
		params.put("despawn", allowDespawn);
		params.put("allowdespawning", allowDespawn);
		final BooleanParamitrisable peaceful = new BooleanParamitrisable(false);
		params.put("p", peaceful);
		params.put("peaceful", peaceful);
		final LocationParamitrisable location = new LocationParamitrisable(sender);
		location.addFullParams(params, "l", "loc", "location");
		location.addAdvancedParams(params, "");
		final DoubleParamitrisable spawnRange = new DoubleParamitrisable(0D)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				if (value < 0)
					throw new CrazyCommandParameterException(0, "positive Number (Double)");
			}
		};
		params.put("sr", spawnRange);
		params.put("srange", spawnRange);
		params.put("spawnrange", spawnRange);
		final IntegerParamitrisable health = new IntegerParamitrisable(-1)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				if (value < -1)
					throw new CrazyCommandParameterException(0, "positive Number (Integer)");
			}
		};
		params.put("h", health);
		params.put("health", health);
		final BooleanParamitrisable showHealth = new BooleanParamitrisable(false);
		params.put("sh", showHealth);
		params.put("showhealth", showHealth);
		final IntegerParamitrisable fire = new IntegerParamitrisable(-1);
		params.put("f", fire);
		params.put("fire", fire);
		params.put("burn", fire);
		params.put("burning", fire);
		final EnumParamitrisable<Thunder> thunder = new EnumParamitrisable<Thunder>("Thunder", Thunder.DISABLED, Thunder.values());
		params.put("t", thunder);
		params.put("thunder", thunder);
		params.put("strike", thunder);
		params.put("lightning", thunder);
		ChatHelperExtended.readParameters(args, params, creature, amount, repeat, interval, delay);
		if (creature.getValue() == null)
			throw new CrazyCommandNoSuchException("Creature", "(none)");
		if (location.getValue().getWorld() == null)
			throw new CrazyCommandNoSuchException("World", "(none)");
		final SpawnTask task = new SpawnTask(plugin, creature.getValue(), location.getValue(), spawnRange.getValue(), amount.getValue(), interval.getValue() / 50, repeat.getValue(), synced.getValue(), chunkLoadRange.getValue(), creatureMaxCount.getValue(), creatureRange.getValue(), playerCount.getValue(), playerRange.getValue(), blockingRange.getValue(), countDownTimes.getValue(), countDownMessage.getValue(), countDownBroadcast.getValue(), allowDespawn.getValue(), peaceful.getValue(), health.getValue(), showHealth.getValue(), fire.getValue(), thunder.getValue());
		plugin.addSpawnTask(task);
		if (synced.getValue())
			task.start();
		else
			task.start(delay.getValue() / 50);
		plugin.sendLocaleMessage("COMMAND.SPAWNED", sender, creature.getValue().getName(), amount);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final Map<String, TabbedParamitrisable> params = new TreeMap<String, TabbedParamitrisable>();
		final ExtendedCreatureParamitrisable creature = new ExtendedCreatureParamitrisable();
		params.put("c", creature);
		params.put("creature", creature);
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
		final BooleanParamitrisable synced = new BooleanParamitrisable(false);
		params.put("s", synced);
		params.put("synced", synced);
		final IntegerParamitrisable chunkLoadRange = new IntegerParamitrisable(-1);
		params.put("clr", chunkLoadRange);
		params.put("chunkloadrange", chunkLoadRange);
		final IntegerParamitrisable creatureMaxCount = new IntegerParamitrisable(0);
		params.put("m", creatureMaxCount);
		params.put("cm", creatureMaxCount);
		params.put("max", creatureMaxCount);
		params.put("cmax", creatureMaxCount);
		params.put("creaturecount", creatureMaxCount);
		params.put("creaturemaxcount", creatureMaxCount);
		final DoubleParamitrisable creatureRange = new DoubleParamitrisable(16D);
		params.put("cr", creatureRange);
		params.put("crange", creatureRange);
		params.put("creaturerange", creatureRange);
		final IntegerParamitrisable playerCount = new IntegerParamitrisable(0);
		params.put("pm", playerCount);
		params.put("min", playerCount);
		params.put("pmin", playerCount);
		params.put("playercount", playerCount);
		params.put("playermincount", playerCount);
		final DoubleParamitrisable playerRange = new DoubleParamitrisable(16D);
		params.put("pr", playerRange);
		params.put("prange", playerRange);
		params.put("playerrange", playerRange);
		final DoubleParamitrisable blockingRange = new DoubleParamitrisable(0D);
		params.put("br", blockingRange);
		params.put("brange", blockingRange);
		params.put("blockingrange", blockingRange);
		final MultiParamitrisable<Long> countDownTimes = new MultiParamitrisable<Long>(new DurationParamitrisable(null));
		params.put("cdt", countDownTimes);
		params.put("cdtime", countDownTimes);
		params.put("countdowntime", countDownTimes);
		final ColoredStringParamitrisable countDownMessage = new ColoredStringParamitrisable(null);
		params.put("cdm", countDownMessage);
		params.put("cdmessage", countDownMessage);
		params.put("countdownmessage", countDownMessage);
		final BooleanParamitrisable countDownBroadcast = new BooleanParamitrisable(false);
		params.put("cdb", countDownBroadcast);
		params.put("cdbroadcast", countDownBroadcast);
		params.put("countdownbroadcast", countDownBroadcast);
		final BooleanParamitrisable allowDespawn = new BooleanParamitrisable(false);
		params.put("ad", allowDespawn);
		params.put("despawn", allowDespawn);
		params.put("allowdespawning", allowDespawn);
		final BooleanParamitrisable peaceful = new BooleanParamitrisable(false);
		params.put("p", peaceful);
		params.put("peaceful", peaceful);
		final LocationParamitrisable location = new LocationParamitrisable(sender);
		location.addFullParams(params, "l", "loc", "location");
		location.addAdvancedParams(params, "");
		final DoubleParamitrisable spawnRange = new DoubleParamitrisable(0D);
		params.put("sr", spawnRange);
		params.put("srange", spawnRange);
		params.put("spawnrange", spawnRange);
		final IntegerParamitrisable health = new IntegerParamitrisable(-1);
		params.put("h", health);
		params.put("health", health);
		final BooleanParamitrisable showHealth = new BooleanParamitrisable(false);
		params.put("sh", showHealth);
		params.put("showhealth", showHealth);
		final IntegerParamitrisable fire = new IntegerParamitrisable(-1);
		params.put("f", fire);
		params.put("fire", fire);
		params.put("burn", fire);
		params.put("burning", fire);
		final EnumParamitrisable<Thunder> thunder = new EnumParamitrisable<Thunder>("Thunder", Thunder.DISABLED, Thunder.values());
		params.put("t", thunder);
		params.put("thunder", thunder);
		params.put("strike", thunder);
		params.put("lightning", thunder);
		return ChatHelperExtended.tabHelp(args, params, creature, amount, repeat, interval, delay);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyspawner.spawn");
	}
}
