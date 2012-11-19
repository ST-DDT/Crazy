package de.st_ddt.crazyutil.paramitrisable;

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

	public static String[] getPlayerNames()
	{
		final Set<String> res = new TreeSet<String>();
		for (final Player player : Bukkit.getOnlinePlayers())
			res.add(player.getName());
		return res.toArray(new String[res.size()]);
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
}
