package de.st_ddt.crazyutil.paramitrisable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class PlayerParamitrisable extends TypedParamitrisable<Player>
{

	public PlayerParamitrisable(final Player defaultValue)
	{
		super(defaultValue);
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		value = Bukkit.getPlayerExact(parameter);
		if (value == null)
			throw new CrazyCommandNoSuchException("Player", parameter, getPlayerNames());
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return tabHelp(parameter);
	}

	public static List<String> tabHelp(String parameter)
	{
		parameter = parameter.toLowerCase();
		final List<String> res = new LinkedList<String>();
		for (final Player player : Bukkit.getOnlinePlayers())
			if (player.getName().toLowerCase().startsWith(parameter))
				res.add(player.getName());
		return res;
	}

	public static Set<String> getPlayerNames()
	{
		return getPlayerNames(Bukkit.getOnlinePlayers());
	}

	public static Set<String> getPlayerNames(Collection<Player> players)
	{
		final Set<String> res = new TreeSet<String>();
		for (final Player player : players)
			res.add(player.getName());
		return res;
	}

	public static Set<String> getPlayerNames(Player... players)
	{
		final Set<String> res = new TreeSet<String>();
		for (final Player player : players)
			res.add(player.getName());
		return res;
	}

	public static List<Player> getMatchingPlayers(String parameter)
	{
		return getMatchingPlayers(parameter, Bukkit.getOnlinePlayers());
	}

	public static List<Player> getMatchingPlayers(String parameter, Collection<Player> players)
	{
		parameter = parameter.toLowerCase();
		final List<Player> res = new ArrayList<Player>();
		for (final Player player : players)
			if (player.getName().toLowerCase().startsWith(parameter))
				res.add(player);
		return res;
	}

	public static List<Player> getMatchingPlayers(String parameter, Player... players)
	{
		parameter = parameter.toLowerCase();
		final List<Player> res = new ArrayList<Player>();
		for (final Player player : players)
			if (player.getName().toLowerCase().startsWith(parameter))
				res.add(player);
		return res;
	}
}
