package de.st_ddt.crazysquads.commands;

import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazysquads.events.CrazySquadsSquadJoinEvent;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazySquadsPlayerCommandSquadJoin extends CrazySquadsPlayerCommandExecutor
{

	public CrazySquadsPlayerCommandSquadJoin(final CrazySquads plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYSQUADS.COMMAND.SQUAD.JOINED $SquadName$")
	public void command(final Player player, final String[] args) throws CrazyException
	{
		if (plugin.getSquads().get(player) != null)
			throw new CrazyCommandUsageException("when not in a squad!");
		final Squad squad = plugin.getInvites().remove(player);
		if (squad == null)
			throw new CrazyCommandUsageException("when being invited to a squad!");
		if (squad.getMembers().size() >= plugin.getMaxSquadSize())
			throw new CrazyCommandUsageException("when squad is not full!");
		new CrazySquadsSquadJoinEvent(plugin, squad, player).callEvent();
		squad.join(player);
		plugin.getSquads().put(player, squad);
		plugin.sendLocaleMessage("COMMAND.SQUAD.JOINED", player, squad.getName());
	}

	@Override
	public boolean hasAccessPermission(final Player player)
	{
		return PermissionModule.hasPermission(player, "crazysquads.squad.join");
	}
}
