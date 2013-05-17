package de.st_ddt.crazyutil.paramitrisable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class WorldParamitrisable extends TypedParamitrisable<World>
{

	public WorldParamitrisable(final World defaultValue)
	{
		super(defaultValue);
	}

	public WorldParamitrisable(final Player player)
	{
		this(player.getWorld());
	}

	public WorldParamitrisable(final CommandSender sender)
	{
		this(sender instanceof Player ? ((Player) sender).getWorld() : null);
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		value = Bukkit.getWorld(parameter);
		if (value == null)
			throw new CrazyCommandNoSuchException("World", parameter, tab(parameter));
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return tabHelp(parameter);
	}

	public static List<String> tabHelp(String parameter)
	{
		parameter = parameter.toLowerCase();
		final List<String> worlds = getWorldNames();
		final List<String> res = new ArrayList<String>(worlds.size());
		for (final String world : worlds)
			if (world.toLowerCase().startsWith(parameter))
				res.add(world);
		if (res.isEmpty())
			return worlds;
		else
			return res;
	}

	public static List<String> getWorldNames()
	{
		return getWorldNames(Bukkit.getWorlds());
	}

	public static List<String> getWorldNames(final World... worlds)
	{
		final List<String> res = new LinkedList<String>();
		for (final World world : Bukkit.getWorlds())
			res.add(world.getName());
		return res;
	}

	public static List<String> getWorldNames(final Collection<World> worlds)
	{
		final List<String> res = new LinkedList<String>();
		for (final World world : Bukkit.getWorlds())
			res.add(world.getName());
		return res;
	}

	public static List<World> getWorldsByEnvironment(final Environment... environments)
	{
		final List<World> res = new LinkedList<World>();
		final List<World> worlds = Bukkit.getWorlds();
		for (final Environment environment : environments)
			for (final World world : worlds)
				if (world.getEnvironment() == environment)
					res.add(world);
		return res;
	}

	public static List<World> getWorldsByEnvironment(final Collection<Environment> environments)
	{
		final List<World> res = new LinkedList<World>();
		final List<World> worlds = Bukkit.getWorlds();
		for (final Environment environment : environments)
			for (final World world : worlds)
				if (world.getEnvironment() == environment)
					res.add(world);
		return res;
	}

	public static List<World> getWorldsByType(final WorldType... types)
	{
		final List<World> res = new LinkedList<World>();
		final List<World> worlds = Bukkit.getWorlds();
		for (final WorldType type : types)
			for (final World world : worlds)
				if (world.getWorldType() == type)
					res.add(world);
		return res;
	}

	public static List<World> getWorldsByType(final Collection<WorldType> types)
	{
		final List<World> res = new LinkedList<World>();
		final List<World> worlds = Bukkit.getWorlds();
		for (final WorldType type : types)
			for (final World world : worlds)
				if (world.getWorldType() == type)
					res.add(world);
		return res;
	}
}
