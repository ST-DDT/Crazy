package de.st_ddt.crazyutil.paramitrisable;

import java.util.ArrayList;
import java.util.Collection;
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
			throw new CrazyCommandNoSuchException("Player", parameter, getPlayers(parameter));
	}

	private Set<String> getPlayers(final String parameter)
	{
		final List<Player> players = getMatchingPlayers(parameter, 20);
		if (players.size() == 0)
			return getPlayerNames();
		else
			return getPlayerNames(20, players);
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return new ArrayList<String>(getPlayerNames(getMatchingPlayers(parameter, 20)));
	}

	@Deprecated
	public static List<String> tabHelp(final String parameter)
	{
		return new ArrayList<String>(getPlayerNames(getMatchingPlayers(parameter, 20)));
	}

	public static Set<String> getPlayerNames()
	{
		return getPlayerNames(Bukkit.getOnlinePlayers());
	}

	public static Set<String> getPlayerNames(final int max)
	{
		return getPlayerNames(max, Bukkit.getOnlinePlayers());
	}

	public static Set<String> getPlayerNames(final Collection<Player> players)
	{
		final Set<String> res = new TreeSet<String>();
		for (final Player player : players)
			res.add(player.getName());
		return res;
	}

	public static Set<String> getPlayerNames(int max, final Collection<Player> players)
	{
		final Set<String> res = new TreeSet<String>();
		for (final Player player : players)
			if (max-- != 0)
				res.add(player.getName());
		return res;
	}

	public static Set<String> getPlayerNames(final Player... players)
	{
		final Set<String> res = new TreeSet<String>();
		for (final Player player : players)
			res.add(player.getName());
		return res;
	}

	public static Set<String> getPlayerNames(int max, final Player... players)
	{
		final Set<String> res = new TreeSet<String>();
		for (final Player player : players)
			if (max-- != 0)
				res.add(player.getName());
		return res;
	}

	public static List<Player> getMatchingPlayers(final String parameter)
	{
		return getMatchingPlayers(parameter, Bukkit.getOnlinePlayers());
	}

	public static List<Player> getMatchingPlayers(final String parameter, final int max)
	{
		return getMatchingPlayers(parameter, max, Bukkit.getOnlinePlayers());
	}

	public static List<Player> getMatchingPlayers(String parameter, final Collection<Player> players)
	{
		parameter = parameter.toLowerCase();
		final List<Player> res = new ArrayList<Player>();
		for (final Player player : players)
			if (player.getName().toLowerCase().startsWith(parameter))
				res.add(player);
		return res;
	}

	public static List<Player> getMatchingPlayers(String parameter, int max, final Collection<Player> players)
	{
		parameter = parameter.toLowerCase();
		final List<Player> res = new ArrayList<Player>();
		for (final Player player : players)
			if (player.getName().toLowerCase().startsWith(parameter))
				if (max-- != 0)
					res.add(player);
		return res;
	}

	public static List<Player> getMatchingPlayers(String parameter, final Player... players)
	{
		parameter = parameter.toLowerCase();
		final List<Player> res = new ArrayList<Player>();
		for (final Player player : players)
			if (player.getName().toLowerCase().startsWith(parameter))
				res.add(player);
		return res;
	}

	public static List<Player> getMatchingPlayers(String parameter, int max, final Player... players)
	{
		parameter = parameter.toLowerCase();
		final List<Player> res = new ArrayList<Player>();
		for (final Player player : players)
			if (player.getName().toLowerCase().startsWith(parameter))
				if (max-- != 0)
					res.add(player);
		return res;
	}
}
