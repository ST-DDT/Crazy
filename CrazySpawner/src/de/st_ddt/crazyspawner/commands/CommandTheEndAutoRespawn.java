package de.st_ddt.crazyspawner.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.tasks.SpawnTask;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.ExtendedCreatureType;
import de.st_ddt.crazyutil.Tabbed;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.DurationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.ExtendedCreatureParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.WorldParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class CommandTheEndAutoRespawn extends CommandExecutor
{

	private final static double DRAGONRANGE = 2500;
	private final static double CRYSTALERANGE = 5;
	private final static Long[] COUNTDOWNTIMES = new Long[] { 20L, 40L, 60L, 80L, 100L, 120L, 140L, 160L, 180L, 200L, 1200L, 6000L, 18000L };
	private final ExtendedCreatureType DRAGONTYPE;
	private final ExtendedCreatureType CRYSTALTYPE;

	public CommandTheEndAutoRespawn(final CrazySpawner plugin)
	{
		super(plugin);
		DRAGONTYPE = ExtendedCreatureParamitrisable.getExtendedCreatureType(EntityType.ENDER_DRAGON.getName());
		CRYSTALTYPE = ExtendedCreatureParamitrisable.getExtendedCreatureType(EntityType.ENDER_CRYSTAL.getName());
	}

	@Override
	@Localized({ "CRAZYSPAWNER.THEENDAUTORESPAWN.COUNTDOWNMESSAGE $World$ $Time$", "CRAZYSPAWNER.COMMAND.THEENDAUTORESPAWN.DONE $World$" })
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
		final DurationParamitrisable interval = new DurationParamitrisable(60 * 60 * 1000L)
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
		final WorldParamitrisable worldParam = new WorldParamitrisable(sender);
		params.put("w", worldParam);
		params.put("world", worldParam);
		final IntegerParamitrisable chunkloadrange = new IntegerParamitrisable(0)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				if (value < -1)
					throw new CrazyCommandParameterException(0, "positive Number (Integer)", "0=unlimited");
			}
		};
		params.put("clr", chunkloadrange);
		params.put("chunkloadrange", chunkloadrange);
		ChatHelperExtended.readParameters(args, params, interval);
		final World world = worldParam.getValue();
		if (world == null)
			throw new CrazyCommandNoSuchException("World", "(none)");
		if (world.getEnvironment() != Environment.THE_END)
			throw new CrazyCommandCircumstanceException("the world must be a The_End world!");
		final SpawnTask dragon = new SpawnTask(plugin, DRAGONTYPE, new Location(world, 0, 0, 0), interval.getValue() / 50, 5, COUNTDOWNTIMES, plugin.getLocale().getDefaultLocaleMessage("THEENDAUTORESPAWN.COUNTDOWNMESSAGE", world.getName(), "$0$"), DRAGONRANGE);
		plugin.addSpawnTask(dragon);
		dragon.start(20);
		final int range = chunkloadrange.getValue();
		for (int x = -range; x <= range; x++)
			for (int z = -range; z <= range; z++)
				world.loadChunk(x, z, false);
		for (final Entity entity : CRYSTALTYPE.getEntities(world))
		{
			final SpawnTask crystal = new SpawnTask(plugin, CRYSTALTYPE, entity.getLocation().add(0, -1, 0), interval.getValue() / 50, 1, null, null, CRYSTALERANGE);
			plugin.addSpawnTask(crystal);
			crystal.start(20);
		}
		for (int x = -range; x <= range; x++)
			for (int z = -range; z <= range; z++)
				world.unloadChunkRequest(x, z, true);
		plugin.sendLocaleMessage("COMMAND.THEENDAUTORESPAWN.DONE", sender, world.getName());
		plugin.saveConfiguration();
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		final Map<String, Tabbed> params = new HashMap<String, Tabbed>();
		final DurationParamitrisable interval = new DurationParamitrisable(5 * 60 * 1000L);
		params.put("i", interval);
		params.put("interval", interval);
		final WorldParamitrisable worldParam = new WorldParamitrisable(sender);
		params.put("w", worldParam);
		params.put("world", worldParam);
		return ChatHelperExtended.tabHelp(args, params, interval);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyspawner.theendautoregenerate");
	}
}
