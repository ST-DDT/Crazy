package de.st_ddt.crazychats.channels;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.st_ddt.crazychats.CrazyChats;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public final class LocalChannel extends AbstractMuteableChannel
{

	private final CrazyChats plugin;
	private boolean enabled = true;

	public LocalChannel(final CrazyChats plugin)
	{
		super("Local");
		this.plugin = plugin;
		aliases.add("l");
		aliases.add("local");
	}

	@Override
	public boolean hasTalkPermission(final Player player)
	{
		return enabled && PermissionModule.hasPermission(player, "crazychats.localchannel.talk");
	}

	@Override
	public Set<Player> getTargets(final Player player)
	{
		final Set<Player> players = new HashSet<Player>();
		final Location location = player.getLocation();
		final World world = location.getWorld();
		final double range = plugin.getLocalChatRange();
		for (final Player online : Bukkit.getOnlinePlayers())
			if (world == online.getWorld())
				if (location.distance(online.getLocation()) < range)
					players.add(online);
		players.removeAll(deafPlayers);
		return players;
	}

	@Override
	public String getFormat(final Player player)
	{
		return plugin.getLocalChatFormat();
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
	}

	@Override
	public String toString()
	{
		return "LocalChatChannel (Range:" + plugin.getLocalChatRange() + ")";
	}

	@Override
	public boolean isAffectedByServerSilence()
	{
		return true;
	}
}
