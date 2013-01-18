package de.st_ddt.crazysquads.commands;

import java.util.Set;

import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazysquads.events.CrazySquadsSquadDeleteEvent;
import de.st_ddt.crazysquads.events.CrazySquadsSquadLeaveEvent;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazySquadsSquadPlayerCommandSquadLeave extends CrazySquadsSquadPlayerCommandExecutor
{

	public CrazySquadsSquadPlayerCommandSquadLeave(final CrazySquads plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYSQUADS.SQUAD.OWNERLEFT $OldOwner$ $NewOwner$", "CRAZYSQUADS.SQUAD.LEFT $Left$", "CRAZYSQUADS.COMMAND.SQUAD.LEFT $SquadName$" })
	public void command(final Player player, final Squad squad, final String[] args) throws CrazyException
	{
		plugin.getSquads().remove(player);
		final Set<Player> members = squad.getMembers();
		members.remove(player);
		if (squad.getOwner() == player)
		{
			if (members.size() > 0)
			{
				final Player newOwner = members.iterator().next();
				squad.setOwner(newOwner);
				plugin.sendLocaleMessage("SQUAD.OWNERLEFT", members, player.getName(), newOwner.getName());
			}
		}
		else
			plugin.sendLocaleMessage("SQUAD.LEFT", members, player.getName());
		plugin.sendLocaleMessage("COMMAND.SQUAD.LEFT", player, squad.getName());
		new CrazySquadsSquadLeaveEvent(squad, player).callEvent();
		if (members.size() == 0)
			new CrazySquadsSquadDeleteEvent(squad).callEvent();
	}
}
