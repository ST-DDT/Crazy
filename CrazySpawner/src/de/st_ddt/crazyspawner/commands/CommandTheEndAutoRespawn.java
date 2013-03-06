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
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.WorldParamitrisable;

public class CommandTheEndAutoRespawn extends CommandExecutor
{

	private final static double DRAGONRANGE = 2500;
	private final static double CRYSTALERANGE = 5;
	private final static double PLAYERRANGE = 500;
	private final ExtendedCreatureType DRAGONTYPE;
	private final ExtendedCreatureType CRYSTALTYPE;

	public CommandTheEndAutoRespawn(final CrazySpawner plugin)
	{
		super(plugin);
		DRAGONTYPE = ExtendedCreatureParamitrisable.getExtendedCreatureType(EntityType.ENDER_DRAGON.getName());
		CRYSTALTYPE = ExtendedCreatureParamitrisable.getExtendedCreatureType(EntityType.ENDER_CRYSTAL.getName());
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
		final DurationParamitrisable interval = new DurationParamitrisable(5 * 60 * 1000L)
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
		ChatHelperExtended.readParameters(args, params, interval);
		final World world = worldParam.getValue();
		if (world == null)
			throw new CrazyCommandNoSuchException("World", "(none)");
		if (world.getEnvironment() != Environment.THE_END)
			throw new CrazyCommandCircumstanceException("the world must be a The_End world!");
		final Location location = new Location(world, 0, 0, 0);
		final SpawnTask dragon = new SpawnTask(plugin, DRAGONTYPE, location, 1, interval.getValue(), -1, 1, DRAGONRANGE, 0, PLAYERRANGE, 0);
		plugin.addSpawnTask(dragon);
		dragon.start(20);
		for (final Entity entity : CRYSTALTYPE.getEntities(world))
		{
			final SpawnTask crystal = new SpawnTask(plugin, CRYSTALTYPE, entity.getLocation().add(0, -1, 0), 1, interval.getValue(), -1, 1, CRYSTALERANGE, 0, PLAYERRANGE, 0);
			plugin.addSpawnTask(crystal);
			crystal.start(20);
		}
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
