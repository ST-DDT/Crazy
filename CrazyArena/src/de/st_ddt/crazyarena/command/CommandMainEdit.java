package de.st_ddt.crazyarena.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.arenas.ArenaStatus;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.MapParamitrisable;

public class CommandMainEdit extends CommandExecutor
{

	public CommandMainEdit(final CrazyArena plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYARENA.COMMAND.ARENA.EDIT $Name$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("<Arena>");
		final String name = args[0];
		final Arena<?> arena = plugin.getArenaByName(name);
		if (arena.getStatus() != ArenaStatus.DISABLED)
			throw new CrazyCommandCircumstanceException("while arena is disabled!");
		arena.setStatus(ArenaStatus.CONSTRUCTING);
		plugin.sendLocaleMessage("COMMAND.ARENA.EDIT", sender, name);
		if (sender != Bukkit.getConsoleSender())
			plugin.sendLocaleMessage("COMMAND.ARENA.EDIT", Bukkit.getConsoleSender(), name);
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
		return PermissionModule.hasPermission(sender, "crazyarena.arena.switchmode");
	}
}
