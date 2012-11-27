package de.st_ddt.crazysquads.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazysquads.events.CrazySquadsSquadLeaveEvent;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazySquadsSquadPlayerCommandSquadKickMember extends CrazySquadsSquadPlayerCommandExecutor
{

	public CrazySquadsSquadPlayerCommandSquadKickMember(final CrazySquads plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYSQUADS.COMMAND.SQUAD.KICKED $Player$")
	public void command(final Player player, final Squad squad, final String[] args) throws CrazyException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("<Player>");
		final String name = args[0];
		final Player kicked = Bukkit.getPlayerExact(name);
		if (kicked == null)
			throw new CrazyCommandNoSuchException("Player", name, squad.getMemberNames());
		plugin.getSquads().remove(kicked);
		if (kicked == player)
			squad.leave(player);
		else
		{
			squad.kick(kicked);
			plugin.sendLocaleMessage("COMMAND.SQUAD.KICKED", player, kicked.getName());
		}
		new CrazySquadsSquadLeaveEvent(plugin, squad, kicked).callEvent();
	}

	@Override
	public boolean hasAccessPermission(final Player player, final Squad squad)
	{
		return squad.getOwner() == player;
	}
}
