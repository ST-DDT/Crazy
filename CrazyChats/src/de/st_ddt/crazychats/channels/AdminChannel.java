package de.st_ddt.crazychats.channels;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class AdminChannel extends AbstractMuteableChannel
{

	public AdminChannel()
	{
		super("Admin");
		aliases.add("a");
		aliases.add("admin");
	}

	@Override
	public boolean hasTalkPermission(final Player player)
	{
		return PermissionModule.hasPermission(player, "crazychats.adminchannel.talk");
	}

	@Override
	public Set<Player> getTargets(final Player player)
	{
		final Set<Player> players = new HashSet<Player>();
		for (final Player online : Bukkit.getOnlinePlayers())
			if (PermissionModule.hasPermission(online, "crazychats.adminchannel.listen"))
				players.add(online);
		players.removeAll(deafPlayers);
		return players;
	}

	@Override
	public String getFormat(final Player player)
	{
		return plugin.getAdminChatFormat();
	}

	@Override
	public String toString()
	{
		return "AdminChatChannel";
	}

	@Override
	public boolean isAffectedByServerSilence()
	{
		return false;
	}
}
