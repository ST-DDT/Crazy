package de.st_ddt.crazyutil.paramitrisable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.source.Permission;

public class VisiblePlayerParamitrisable extends PlayerParamitrisable
{

	@Permission("crazycore.params.invisibleplayers")
	public static PlayerParamitrisable getParamitrisableFor(final Player defaultValue, final CommandSender sender)
	{
		if (sender instanceof Player && !sender.hasPermission("crazycore.params.invisibleplayers"))
			return new VisiblePlayerParamitrisable(defaultValue, (Player) sender);
		else
			return new PlayerParamitrisable(defaultValue);
	}

	protected final Player canBeSeenBy;

	public VisiblePlayerParamitrisable(final Player defaultValue, final Player canBeSeenBy)
	{
		super(defaultValue);
		this.canBeSeenBy = canBeSeenBy;
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		super.setParameter(parameter);
		if (canBeSeenBy != null)
			if (!canBeSeenBy.canSee(value))
				throw new CrazyCommandNoSuchException("Player", parameter, getPlayers(parameter));
	}

	private Set<String> getPlayers(final String parameter)
	{
		final List<Player> players = getMatchingPlayers(canBeSeenBy, parameter, 20);
		if (players.size() == 0)
			return getPlayerNames(canBeSeenBy, 20);
		else
			return getPlayerNames(canBeSeenBy, 20, players);
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return new ArrayList<String>(getPlayerNames(getMatchingPlayers(canBeSeenBy, parameter, 20)));
	}

	public static List<String> tabHelp(final Player canBeSeenBy, final String parameter)
	{
		return new ArrayList<String>(getPlayerNames(getMatchingPlayers(canBeSeenBy, parameter, 20)));
	}

	public static Set<String> getPlayerNames(final Player canBeSeenBy)
	{
		return getPlayerNames(canBeSeenBy, Bukkit.getOnlinePlayers());
	}

	public static Set<String> getPlayerNames(final Player canBeSeenBy, final int max)
	{
		return getPlayerNames(canBeSeenBy, max, Bukkit.getOnlinePlayers());
	}

	public static Set<String> getPlayerNames(final Player canBeSeenBy, final Collection<Player> players)
	{
		final Set<String> res = new TreeSet<String>();
		for (final Player player : players)
			if (canBeSeenBy.canSee(player))
				res.add(player.getName());
		return res;
	}

	public static Set<String> getPlayerNames(final Player canBeSeenBy, int max, final Collection<Player> players)
	{
		final Set<String> res = new TreeSet<String>();
		for (final Player player : players)
			if (canBeSeenBy.canSee(player))
				if (max-- != 0)
					res.add(player.getName());
		return res;
	}

	public static Set<String> getPlayerNames(final Player canBeSeenBy, final Player... players)
	{
		final Set<String> res = new TreeSet<String>();
		for (final Player player : players)
			if (canBeSeenBy.canSee(player))
				res.add(player.getName());
		return res;
	}

	public static Set<String> getPlayerNames(final Player canBeSeenBy, int max, final Player... players)
	{
		final Set<String> res = new TreeSet<String>();
		for (final Player player : players)
			if (canBeSeenBy.canSee(player))
				if (max-- != 0)
					res.add(player.getName());
		return res;
	}

	public static List<Player> getMatchingPlayers(final Player canBeSeenBy, final String parameter)
	{
		return getMatchingPlayers(canBeSeenBy, parameter, Bukkit.getOnlinePlayers());
	}

	public static List<Player> getMatchingPlayers(final Player canBeSeenBy, final String parameter, final int max)
	{
		return getMatchingPlayers(canBeSeenBy, parameter, max, Bukkit.getOnlinePlayers());
	}

	public static List<Player> getMatchingPlayers(final Player canBeSeenBy, String parameter, final Collection<Player> players)
	{
		parameter = parameter.toLowerCase();
		final List<Player> res = new ArrayList<Player>();
		for (final Player player : players)
			if (canBeSeenBy.canSee(player))
				if (player.getName().toLowerCase().startsWith(parameter))
					res.add(player);
		return res;
	}

	public static List<Player> getMatchingPlayers(final Player canBeSeenBy, String parameter, int max, final Collection<Player> players)
	{
		parameter = parameter.toLowerCase();
		final List<Player> res = new ArrayList<Player>();
		for (final Player player : players)
			if (canBeSeenBy.canSee(player))
				if (player.getName().toLowerCase().startsWith(parameter))
				{
					res.add(player);
					if (max-- < 1)
						break;
				}
		return res;
	}

	public static List<Player> getMatchingPlayers(final Player canBeSeenBy, String parameter, final Player... players)
	{
		parameter = parameter.toLowerCase();
		final List<Player> res = new ArrayList<Player>();
		for (final Player player : players)
			if (canBeSeenBy.canSee(player))
				if (player.getName().toLowerCase().startsWith(parameter))
					res.add(player);
		return res;
	}

	public static List<Player> getMatchingPlayers(final Player canBeSeenBy, String parameter, int max, final Player... players)
	{
		parameter = parameter.toLowerCase();
		final List<Player> res = new ArrayList<Player>();
		for (final Player player : players)
			if (canBeSeenBy.canSee(player))
				if (player.getName().toLowerCase().startsWith(parameter))
				{
					res.add(player);
					if (--max < 1)
						break;
				}
		return res;
	}
}
