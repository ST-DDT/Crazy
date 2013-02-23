package de.st_ddt.crazyarena.command;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandAlreadyExistsException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandErrorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.source.Localized;

public class CommandMainImport extends CommandExecutor
{

	public CommandMainImport(final CrazyArena plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYARENA.COMMAND.ARENA.LOADED $Name$ $Type$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("<Name>");
		final String name = args[0];
		Arena<?> arena = plugin.getArenaByName(name);
		if (arena != null)
			throw new CrazyCommandAlreadyExistsException("Arena", name);
		final File file = new File(Arena.ARENADATAROOTPATH + name + File.separator + "config.yml");
		if (!file.exists())
			throw new CrazyCommandNoSuchException("ArenaFile", name);
		try
		{
			arena = Arena.loadFromFile(name, file);
		}
		catch (final Exception e)
		{
			throw new CrazyCommandErrorException(e);
		}
		plugin.getArenas().add(arena);
		plugin.getArenasByName().put(name, arena);
		plugin.getArenasByType().get(arena.getType().toLowerCase()).add(arena);
		plugin.sendLocaleMessage("COMMAND.ARENA.LOADED", sender, arena.getName(), arena.getType());
		if (sender != Bukkit.getConsoleSender())
			plugin.sendLocaleMessage("COMMAND.ARENA.LOADED", Bukkit.getConsoleSender(), arena.getName(), arena.getType());
		arena.show(sender);
		arena.saveToFile();
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return PermissionModule.hasPermission(sender, "crazyarena.arena.import");
	}
}
