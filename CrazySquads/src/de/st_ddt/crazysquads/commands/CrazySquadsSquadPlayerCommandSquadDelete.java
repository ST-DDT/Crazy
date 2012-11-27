package de.st_ddt.crazysquads.commands;

import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazysquads.events.CrazySquadsSquadLeaveEvent;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazySquadsSquadPlayerCommandSquadDelete extends CrazySquadsSquadPlayerCommandExecutor
{

	public CrazySquadsSquadPlayerCommandSquadDelete(final CrazySquads plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYSQUADS.COMMAND.SQUAD.DELETED $Name$")
	public void command(final Player player, final Squad squad, final String[] args) throws CrazyException
	{
		for (final Player member : squad.getMembers())
		{
			plugin.getSquads().remove(member);
			plugin.sendLocaleMessage("COMMAND.SQUAD.DELETED", member, player.getName());
		}
		final Player[] members = squad.getMembers().toArray(new Player[squad.getMembers().size()]);
		squad.getMembers().clear();
		for (final Player member : members)
			new CrazySquadsSquadLeaveEvent(plugin, squad, member).callEvent();
	}

	@Override
	public boolean hasAccessPermission(final Player player, final Squad squad)
	{
		return squad.getOwner() == player;
	}
}
