package de.st_ddt.crazychats.channels;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Permission;

public class WorldChannel extends AbstractMuteableChannel
{

	private final World world;

	public WorldChannel(final World world)
	{
		super("World_" + world.getName());
		this.world = world;
		aliases.add("w");
		aliases.add("world");
		aliases.add(world.getName());
	}

	public World getWorld()
	{
		return world;
	}

	@Override
	@Permission({ "crazychats.worldchannels.talk", "crazychats.worldchannel.<WORLDNAME>.talk", "crazychats.worldchannels.remotetalk", "crazychats.worldchannel.<WORLDNAME>.remotetalk" })
	public boolean hasTalkPermission(final Player player)
	{
		return (world == player.getWorld() && (PermissionModule.hasPermission(player, "crazychats.worldchannels.talk") || PermissionModule.hasPermission(player, "crazychats.worldchannel." + name + ".talk"))) || PermissionModule.hasPermission(player, "crazychats.worldchannels.remotetalk") || PermissionModule.hasPermission(player, "crazychats.worldchannel." + name + ".remotetalk");
	}

	@Override
	public Set<Player> getTargets(final Player player)
	{
		final Set<Player> players = new HashSet<Player>();
		for (final Player online : Bukkit.getOnlinePlayers())
			if (world == online.getWorld())
				players.add(online);
		players.removeAll(deafPlayers);
		return players;
	}

	@Override
	public String getFormat(final Player player)
	{
		return plugin.getWorldChatFormat();
	}

	@Override
	public String toString()
	{
		return "WorldChatChannel (World: " + world.getName() + ")";
	}

	@Override
	public boolean isAffectedByServerSilence()
	{
		return true;
	}
}
