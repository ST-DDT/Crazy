package de.st_ddt.crazychats.channels;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;

import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;

public class SquadChannel implements ChannelInterface
{

	protected final CrazySquads plugin;
	protected final List<String> aliases = new ArrayList<String>();
	protected final Squad squad;
	protected final Set<Player> members;

	public SquadChannel(final CrazySquads plugin, final Squad squad)
	{
		super();
		aliases.add("p");
		aliases.add("party");
		aliases.add("squad");
		this.plugin = plugin;
		this.squad = squad;
		members = squad.getMembers();
	}

	@Override
	public String getName()
	{
		return squad.getName();
	}

	@Override
	public List<String> getAliases()
	{
		return aliases;
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
	public String getFormat(final Player player)
	{
		return player.equals(squad.getOwner()) ? plugin.getSquadLeaderChatFormat() : plugin.getSquadChatFormat();
	}

	@Override
	public String toString()
	{
		return "SquadChatChannel (Owner: " + squad.getOwner() + ")";
	}
}
