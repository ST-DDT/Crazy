package de.st_ddt.crazysquads.commands;

import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazysquads.events.CrazySquadsSquadLeaveEvent;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazySquadsSquadPlayerCommandSquadLeave extends CrazySquadsSquadPlayerCommandExecutor
{

	public CrazySquadsSquadPlayerCommandSquadLeave(final CrazySquads plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYSQUADS.COMMAND.SQUAD.LEFT $Name$")
	public void command(final Player player, final Squad squad, final String[] args) throws CrazyException
	{
		plugin.getSquads().remove(player);
		squad.leave(player);
		plugin.sendLocaleMessage("COMMAND.SQUAD.LEFT", player, squad.getName());
		new CrazySquadsSquadLeaveEvent(plugin, squad, player).callEvent();
	}
}
