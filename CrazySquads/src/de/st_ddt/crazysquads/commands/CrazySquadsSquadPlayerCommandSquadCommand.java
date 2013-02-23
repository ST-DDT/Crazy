package de.st_ddt.crazysquads.commands;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.source.Localized;

public class CrazySquadsSquadPlayerCommandSquadCommand extends CrazySquadsSquadPlayerCommandExecutor
{

	public CrazySquadsSquadPlayerCommandSquadCommand(final CrazySquads plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYSQUADS.SQUAD.SQUADCOMMAND $Owner$ $Command$", "CRAZYSQUADS.COMMAND.SQUADCOMMAND $Command$" })
	public void command(final Player player, final Squad squad, final String[] args) throws CrazyException
	{
		if (args.length == 0)
			throw new CrazyCommandUsageException("<Command>");
		final String command = ChatHelper.listingString(" ", args);
		boolean whitelisted = false;
		for (final String string : plugin.getCommandWhiteList())
			if (command.matches(string))
			{
				whitelisted = true;
				break;
			}
		if (!whitelisted)
			throw new CrazyCommandPermissionException();
		final Set<Player> members;
		synchronized (squad.getMembers())
		{
			members = new HashSet<Player>(squad.getMembers());
		}
		for (final Player member : members)
		{
			plugin.sendLocaleMessage("SQUAD.SQUADCOMMAND", member, player.getName(), command);
			Bukkit.dispatchCommand(member, ChatHelper.putArgs(command, member.getName()));
		}
		plugin.sendLocaleMessage("COMMAND.SQUADCOMMAND", player, command);
	}

	@Override
	public boolean hasAccessPermission(final Player player, final Squad squad)
	{
		return squad.getOwner() == player;
	}
}
