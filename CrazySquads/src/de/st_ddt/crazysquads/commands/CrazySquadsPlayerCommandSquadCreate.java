package de.st_ddt.crazysquads.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazysquads.events.CrazySquadsSquadCreateEvent;
import de.st_ddt.crazysquads.events.CrazySquadsSquadDeleteEvent;
import de.st_ddt.crazysquads.events.CrazySquadsSquadInviteEvent;
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
	@Localized({ "CRAZYSQUADS.COMMAND.SQUAD.CREATE.CANCELLED $Reason$", "CRAZYSQUADS.COMMAND.SQUAD.JOIN.CANCELLED $SquadName$ $Reason$", "CRAZYSQUADS.COMMAND.SQUAD.CREATED", "CRAZYSQUADS.COMMAND.SQUAD.INVITE.CANCELLED $Invited$ $Reason$", "CRAZYSQUADS.COMMAND.SQUAD.INVITED.ALREADY $Invited$", "CRAZYSQUADS.SQUAD.INVITED $Owner$", "CRAZYSQUADS.COMMAND.SQUAD.INVITED $Invited$" })
	public void command(final Player player, final String[] args) throws CrazyException
	{
		if (plugin.getSquads().get(player) != null)
			throw new CrazyCommandUsageException("when not in a squad!");
		final Squad squad = new Squad(player);
		final CrazySquadsSquadCreateEvent createEvent = new CrazySquadsSquadCreateEvent(plugin, squad);
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
				for (final String name : args)
				{
					final Player invited = Bukkit.getPlayerExact(name);
					if (invited == null)
						throw new CrazyCommandNoSuchException("Player", name);
					if (squad.getMembers().contains(invited))
						throw new CrazyCommandCircumstanceException("when invited is not already member of the squad!");
					final CrazySquadsSquadInviteEvent event = new CrazySquadsSquadInviteEvent(plugin, squad, invited);
					event.callEvent();
					if (event.isCancelled())
						plugin.sendLocaleMessage("COMMAND.SQUAD.INVITE.CANCELLED", player, squad.getName(), event.getReason());
					else if (plugin.getInvites().put(invited, squad) == squad)
						plugin.sendLocaleMessage("COMMAND.SQUAD.INVITED.ALREADY", player, invited.getName());
					else
					{
						plugin.sendLocaleMessage("SQUAD.INVITED", invited, player.getName());
						plugin.sendLocaleMessage("COMMAND.SQUAD.INVITED", player, invited.getName());
					}
				}
			}
		}
	}

	@Override
	public List<String> tab(final Player player, final String[] args)
	{
		final String name = args[args.length - 1].toLowerCase();
		final List<String> res = new LinkedList<String>();
		for (final Player player2 : Bukkit.getOnlinePlayers())
			if (plugin.getSquads().get(player2) == null)
				if (player2.getName().toLowerCase().startsWith(name))
					res.add(player2.getName());
		return res;
	}

	@Override
	public boolean hasAccessPermission(final Player player)
	{
		return PermissionModule.hasPermission(player, "crazysquads.squad.create");
	}
}
