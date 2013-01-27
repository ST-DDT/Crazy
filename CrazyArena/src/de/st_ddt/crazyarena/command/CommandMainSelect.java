package de.st_ddt.crazyarena.command;

import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.MapParamitrisable;

public class CommandMainSelect extends CommandExecutor
{

	public CommandMainSelect(final CrazyArena plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYARENA.COMMAND.ARENA.SELECTED $ArenaName$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		switch (args.length)
		{
			case 0:
				Arena<?> arena = plugin.getSelections().get(sender);
				if (arena == null)
					throw new CrazyCommandUsageException("<Arena>");
				else
					plugin.sendLocaleMessage("COMMAND.ARENA.SELECTED", sender, arena.getName());
				return;
			case 1:
				arena = plugin.getArenaByName(args[0]);
				if (arena == null)
					throw new CrazyCommandNoSuchException("Arena", args[0]);
				plugin.getSelections().put(sender.getName().toLowerCase(), arena);
				plugin.sendLocaleMessage("COMMAND.ARENA.SELECTED", sender, arena.getName());
				return;
			default:
				throw new CrazyCommandUsageException("[Arena]");
		}
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length != 1)
			return null;
		return MapParamitrisable.tabHelp(plugin.getArenasByName(), args[0].toLowerCase());
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyarena.arena.modify");
	}
}
