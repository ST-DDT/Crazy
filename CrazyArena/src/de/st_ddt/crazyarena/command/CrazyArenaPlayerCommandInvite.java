package de.st_ddt.crazyarena.command;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.PlayerParamitrisable;

public class CrazyArenaPlayerCommandInvite extends CrazyArenaPlayerCommandExecutor
{

	public CrazyArenaPlayerCommandInvite(final CrazyArena plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final Player player, final String[] args) throws CrazyException
	{
		final Arena<?> arena = plugin.getArena(player);
		if (arena == null || !arena.isParticipant(player))
			throw new CrazyCommandCircumstanceException("when participating in an arena!");
		if (args.length == 0)
			throw new CrazyCommandUsageException("<Player1> [Player...]");
		final Map<String, Arena<?>> invitations = plugin.getInvitations();
		if (args[0] == "*")
			if (player.hasPermission("crazyarena.invite.all"))
			{
				int anz = 0;
				for (final Player invited : Bukkit.getOnlinePlayers())
					if (plugin.getArena(invited) == null)
					{
						anz++;
						invitations.put(invited.getName().toLowerCase(), arena);
						plugin.sendLocaleMessage("COMMAND.INVITATION.MESSAGE", invited, player.getName(), arena.getName());
					}
				plugin.sendLocaleMessage("COMMAND.INVITATION.SUMMARY", player, anz, arena.getName());
				return;
			}
			else
				throw new CrazyCommandPermissionException();
		int anz = 0;
		for (final String name : args)
		{
			Player invited = Bukkit.getPlayerExact(name);
			if (invited == null)
				invited = Bukkit.getPlayer(name);
			if (invited == null)
				continue;
			anz++;
			invitations.put(invited.getName().toLowerCase(), arena);
			plugin.sendLocaleMessage("COMMAND.INVITATION.MESSAGE", invited, player.getName(), arena.getName());
		}
		plugin.sendLocaleMessage("COMMAND.INVITATION.SUMMARY", player, anz, arena.getName());
	}

	@Override
	public List<String> tab(final Player player, final String[] args)
	{
		return PlayerParamitrisable.tabHelp(args[args.length - 1]);
	}

	@Override
	public boolean hasAccessPermission(final Player player)
	{
		return PermissionModule.hasPermission(player, "crazyarena.invite");
	}
}
