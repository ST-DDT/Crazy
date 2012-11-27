package de.st_ddt.crazysquads.commands;

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
			throw new CrazyCommandNoSuchException("Player", name, squad.getMemberNames());
		plugin.getSquads().remove(kicked);
		final Set<Player> members = squad.getMembers();
		members.remove(player);
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
	public boolean hasAccessPermission(final Player player, final Squad squad)
	{
		return squad.getOwner() == player;
	}
}
