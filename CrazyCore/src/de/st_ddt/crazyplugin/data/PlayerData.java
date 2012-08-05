package de.st_ddt.crazyplugin.data;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class PlayerData<S extends PlayerData<S>> implements PlayerDataInterface<S>
{

	protected final String name;

	public PlayerData(final String name)
	{
		super();
		this.name = name;
	}

	@Override
	public void show(final CommandSender target)
	{
		target.sendMessage(getShortInfo(new String[0]));
	}

	@Override
	public void show(final CommandSender target, final String... args)
	{
		target.sendMessage(getShortInfo(args));
	}

	@Override
	public String getShortInfo(final String... args)
	{
		return toString();
	}

	@Override
	public Player getPlayer()
	{
		return Bukkit.getPlayerExact(getName());
	}

	@Override
	public OfflinePlayer getOfflinePlayer()
	{
		return Bukkit.getOfflinePlayer(getName());
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean isOnline()
	{
		final Player player = getPlayer();
		if (player == null)
			return false;
		return player.isOnline();
	}
}
