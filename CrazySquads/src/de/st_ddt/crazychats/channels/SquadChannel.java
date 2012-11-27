package de.st_ddt.crazychats.channels;

import java.util.Set;

import org.bukkit.entity.Player;

import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;

public class SquadChannel extends AbstractChannel
{

	private final CrazySquads plugin;
	private final Squad squad;
	private final Set<Player> members;

	public SquadChannel(final CrazySquads plugin, final Squad squad, final Set<Player> members)
	{
		super(squad.getName());
		aliases.add("p");
		aliases.add("party");
		aliases.add("squad");
		this.plugin = plugin;
		this.squad = squad;
		this.members = members;
	}

	@Override
	public boolean hasTalkPermission(final Player player)
	{
		return members.contains(player);
	}

	public CrazySquads getPlugin()
	{
		return plugin;
	}

	public Squad getSquad()
	{
		return squad;
	}

	@Override
	public Set<Player> getTargets(final Player player)
	{
		return members;
	}

	@Override
	public String getFormat()
	{
		return plugin.getSquadChatFormat();
	}
}
