package de.st_ddt.crazysquads.commands;

import java.util.Set;

import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazysquads.events.CrazySquadsSquadDeleteEvent;
import de.st_ddt.crazysquads.events.CrazySquadsSquadLeaveEvent;
import de.st_ddt.crazyutil.source.Localized;

public class CrazySquadsSquadPlayerCommandSquadDelete extends CrazySquadsSquadPlayerCommandExecutor
{

	public CrazySquadsSquadPlayerCommandSquadDelete(final CrazySquads plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYSQUADS.COMMAND.SQUAD.DELETED $Owner$")
	public void command(final Player player, final Squad squad, final String[] args) throws CrazyException
	{
		final Set<Player> members = squad.getMembers();
		final Player[] membersArray = members.toArray(new Player[members.size()]);
		for (final Player member : membersArray)
		{
			members.remove(member);
			plugin.getSquads().remove(member);
			plugin.sendLocaleMessage("COMMAND.SQUAD.DELETED", member, player.getName());
			new CrazySquadsSquadLeaveEvent(squad, member).callEvent();
		}
		members.clear();
		new CrazySquadsSquadDeleteEvent(squad).callEvent();
	}

	@Override
	public boolean hasAccessPermission(final Player player, final Squad squad)
	{
		return squad.getOwner() == player;
	}
}
