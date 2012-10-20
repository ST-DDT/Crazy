package de.st_ddt.crazyutil.paramitrisable;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable.TypedParamitrisable;

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
			throw new CrazyCommandNoSuchException("World", parameter, getWorldNames());
	}

	private String[] getWorldNames()
	{
		List<World> worlds = Bukkit.getWorlds();
		int length = worlds.size();
		String[] res = new String[length];
		for (int i = 0; i < length; i++)
			res[i] = worlds.get(i).getName();
		return res;
	}
}
