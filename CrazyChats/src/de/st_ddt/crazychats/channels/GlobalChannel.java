package de.st_ddt.crazychats.channels;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class GlobalChannel extends AbstractMuteableChannel
{

	public GlobalChannel()
	{
		super("Global");
		aliases.add("g");
		aliases.add("global");
	}

	@Override
	public boolean hasTalkPermission(final Player player)
	{
		return PermissionModule.hasPermission(player, "crazychats.globalchannel.talk");
	}

	@Override
	public Set<Player> getTargets(final Player player)
	{
		final Set<Player> players = new HashSet<Player>();
		for (final Player online : Bukkit.getOnlinePlayers())
			players.add(online);
		players.removeAll(deafPlayers);
		return players;
	}

	@Override
	public String getFormat()
	{
		return plugin.getGlobalChatFormat();
	}
}
