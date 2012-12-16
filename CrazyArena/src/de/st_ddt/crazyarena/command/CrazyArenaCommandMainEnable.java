package de.st_ddt.crazyarena.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.MapParamitrisable;

public class CrazyArenaCommandMainEnable extends CrazyArenaCommandExecutor
{

	public CrazyArenaCommandMainEnable(final CrazyArena plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYARENA.COMMAND.ARENA.ENABLED $Name$ $Enabled$ $Status$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("<Arena>");
		final String name = args[0];
		final Arena<?> arena = plugin.getArenaByName(name);
		final boolean enabled = arena.setEnabled(sender, true);
		plugin.sendLocaleMessage("COMMAND.ARENA.ENABLED", sender, name, enabled ? "TRUE" : "FALSE", arena.getStatus().toString());
		if (sender != Bukkit.getConsoleSender())
			plugin.sendLocaleMessage("COMMAND.ARENA.ENABLED", Bukkit.getConsoleSender(), name);
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
