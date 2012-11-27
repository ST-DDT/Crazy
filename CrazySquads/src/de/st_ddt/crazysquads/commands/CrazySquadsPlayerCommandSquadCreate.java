package de.st_ddt.crazysquads.commands;

import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazysquads.events.CrazySquadsSquadCreateEvent;
import de.st_ddt.crazysquads.events.CrazySquadsSquadDeleteEvent;
import de.st_ddt.crazysquads.events.CrazySquadsSquadJoinEvent;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazySquadsPlayerCommandSquadCreate extends CrazySquadsPlayerCommandExecutor
{

	public CrazySquadsPlayerCommandSquadCreate(final CrazySquads plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYSQUADS.COMMAND.SQUAD.CREATE.CANCELLED $Reason$", "CRAZYSQUADS.COMMAND.SQUAD.JOIN.CANCELLED $SquadName$ $Reason$", "CRAZYSQUADS.COMMAND.SQUAD.CREATED" })
	public void command(final Player player, final String[] args) throws CrazyException
	{
		if (plugin.getSquads().get(player) != null)
			throw new CrazyCommandUsageException("when not in a squad!");
		final Squad squad = new Squad(player);
		CrazySquadsSquadCreateEvent createEvent = new CrazySquadsSquadCreateEvent(plugin, squad);
		createEvent.callEvent();
		if (createEvent.isCancelled())
		{
			plugin.sendLocaleMessage("COMMAND.SQUAD.CREATE.CANCELLED", player, createEvent.getReason());
			new CrazySquadsSquadDeleteEvent(plugin, squad).callEvent();
		}
		else
		{
			final CrazySquadsSquadJoinEvent joinEvent = new CrazySquadsSquadJoinEvent(plugin, squad, player);
			joinEvent.callEvent();
			if (joinEvent.isCancelled())
			{
				plugin.sendLocaleMessage("COMMAND.SQUAD.JOIN.CANCELLED", player, squad.getName(), joinEvent.getReason());
				new CrazySquadsSquadDeleteEvent(plugin, squad).callEvent();
			}
			else
			{
				squad.getMembers().add(player);
				plugin.getSquads().put(player, squad);
				plugin.getInvites().remove(player);
				plugin.sendLocaleMessage("COMMAND.SQUAD.CREATED", player);
			}
		}
	}

	@Override
	public boolean hasAccessPermission(final Player player)
	{
		return PermissionModule.hasPermission(player, "crazysquads.squad.create");
	}
}
