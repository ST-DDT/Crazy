package de.st_ddt.crazyutil.paramitrisable;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class OfflinePlayerParamitrisable extends TypedParamitrisable<OfflinePlayer>
{

	public OfflinePlayerParamitrisable(final OfflinePlayer defaultValue)
	{
		super(defaultValue);
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		value = Bukkit.getOfflinePlayer(parameter);
		if (value == null)
			throw new CrazyCommandNoSuchException("Player", parameter);
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return tabHelp(parameter);
	}

	public static List<String> tabHelp(String parameter)
	{
		parameter = parameter.toLowerCase();
		int max = 20;
		final List<String> res = new LinkedList<String>();
		for (final Player player : Bukkit.getOnlinePlayers())
			if (player.getName().toLowerCase().startsWith(parameter))
			{
				res.add(player.getName());
				if (max-- < 1)
					break;
			}
		return res;
	}

	public static String[] getPlayerNames(final List<? extends OfflinePlayer> players)
	{
		final int length = players.size();
		final String[] res = new String[length];
		for (int i = 0; i < length; i++)
			res[i] = players.get(i).getName();
		return res;
	}
}
