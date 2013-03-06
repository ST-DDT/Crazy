package de.st_ddt.crazyutil.paramitrisable;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.OfflinePlayer;

import de.st_ddt.crazyplugin.CrazyPlayerDataPlugin;
import de.st_ddt.crazyplugin.data.PlayerDataInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class PlayerDataParamitrisable<S extends PlayerDataInterface> extends TypedParamitrisable<S>
{

	private final CrazyPlayerDataPlugin<S, ? extends S> plugin;

	public PlayerDataParamitrisable(final CrazyPlayerDataPlugin<S, ? extends S> plugin)
	{
		super(null);
		this.plugin = plugin;
	}

	public PlayerDataParamitrisable(final CrazyPlayerDataPlugin<S, ? extends S> plugin, final String name)
	{
		super(plugin.getPlayerData(name));
		this.plugin = plugin;
	}

	public PlayerDataParamitrisable(final CrazyPlayerDataPlugin<S, ? extends S> plugin, final OfflinePlayer player)
	{
		super(plugin.getPlayerData(player));
		this.plugin = plugin;
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		value = plugin.getPlayerData(parameter);
		if (value == null)
			throw new CrazyCommandNoSuchException("PlayerData", parameter);
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return tabHelp(plugin, parameter);
	}

	public static List<String> tabHelp(final CrazyPlayerDataPlugin<?, ?> plugin, String parameter)
	{
		parameter = parameter.toLowerCase();
		final List<String> res = new LinkedList<String>();
		int max = 20;
		synchronized (plugin.getPlayerDataLock())
		{
			for (final PlayerDataInterface entry : plugin.getPlayerData())
				if (entry.getName().toLowerCase().startsWith(parameter))
				{
					res.add(entry.getName());
					if (--max < 1)
						break;
				}
		}
		return res;
	}
}
