package de.st_ddt.crazysquads.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazysquads.events.CrazySquadsSquadDeleteEvent;
import de.st_ddt.crazysquads.events.CrazySquadsSquadLeaveEvent;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazySquadsSquadPlayerCommandSquadKickMember extends CrazySquadsSquadPlayerCommandExecutor
{

	public CrazySquadsSquadPlayerCommandSquadKickMember(final CrazySquads plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYSQUADS.SQUAD.OWNERLEFT $OldOwner$ $NewOwner$", "CRAZYSQUADS.SQUAD.KICKED $Owner$", "CRAZYSQUADS.SQUAD.KICKED.MEMBER $Owner$ $Kicked$", "CRAZYSQUADS.COMMAND.SQUAD.KICKED $Kicked$" })
	public void command(final Player player, final Squad squad, final String[] args) throws CrazyException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("<Player>");
		final String name = args[0];
		final Player kicked = Bukkit.getPlayerExact(name);
		if (kicked == null)
			throw new CrazyCommandNoSuchException("SquadMember", name, squad.getMemberNames());
		final Set<Player> members = squad.getMembers();
		if (!members.remove(kicked))
			throw new CrazyCommandNoSuchException("SquadMember", name);
		plugin.getSquads().remove(kicked);
		if (kicked == player)
		{
			if (members.size() > 0)
			{
				final Player newOwner = members.iterator().next();
				squad.setOwner(newOwner);
				plugin.sendLocaleMessage("SQUAD.OWNERLEFT", members, player, newOwner.getName());
			}
		}
		else
		{
			plugin.sendLocaleMessage("SQUAD.KICKED", kicked, player.getName());
			plugin.sendLocaleMessage("SQUAD.KICKED.MEMBER", members, player.getName(), kicked.getName());
			plugin.sendLocaleMessage("COMMAND.SQUAD.KICKED", player, kicked.getName());
		}
		new CrazySquadsSquadLeaveEvent(plugin, squad, kicked).callEvent();
		if (members.size() == 0)
			new CrazySquadsSquadDeleteEvent(plugin, squad).callEvent();
	}

	@Override
	public List<String> tab(final Player player, final Squad squad, final String[] args)
	{
		if (args.length != 1)
			return null;
		final String arg = args[0].toLowerCase();
		final List<String> res = new LinkedList<String>();
		final Set<Player> members = squad.getMembers();
		synchronized (members)
		{
			for (final Player member : members)
				if (member.getName().toLowerCase().startsWith(arg))
					res.add(member.getName());
		}
		return res;
	}

	@Override
	public boolean hasAccessPermission(final Player player, final Squad squad)
	{
		return squad.getOwner() == player;
	}
}
