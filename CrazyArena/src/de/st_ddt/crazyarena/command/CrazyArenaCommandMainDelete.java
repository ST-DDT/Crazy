package de.st_ddt.crazyarena.command;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.events.CrazyArenaArenaDeleteEvent;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.MapParamitrisable;

public class CrazyArenaCommandMainDelete extends CrazyArenaCommandExecutor
{

	public CrazyArenaCommandMainDelete(final CrazyArena plugin)
	{
		super(plugin);
	}

	@Override
	@Localized("CRAZYARENA.COMMAND.ARENA.DELETED $Name$ $Type$")
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("<Arena>");
		final String name = args[0];
		final Arena<?> arena = plugin.getArenaByName(name);
		if (arena == null)
			throw new CrazyCommandNoSuchException("Arena", name, plugin.getArenasByName().keySet());
		arena.shutdown();
		arena.saveToFile();
		plugin.getArenas().remove(arena);
		plugin.getArenasByName().remove(name);
		plugin.getArenasByType().get(arena.getType()).remove(arena);
		Iterator<Entry<String, Arena<?>>> it = plugin.getInvitations().entrySet().iterator();
		while (it.hasNext())
			if (it.next().getValue() == arena)
				it.remove();
		it = plugin.getSelections().entrySet().iterator();
		while (it.hasNext())
			if (it.next().getValue() == arena)
				it.remove();
		plugin.sendLocaleMessage("COMMAND.ARENA.DELETED", sender, arena.getName(), arena.getType());
		new CrazyArenaArenaDeleteEvent(arena).callEvent();
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
		return PermissionModule.hasPermission(sender, "crazyarena.arena.delete");
	}
}
