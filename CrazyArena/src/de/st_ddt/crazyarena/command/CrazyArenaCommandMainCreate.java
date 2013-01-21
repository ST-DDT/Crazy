package de.st_ddt.crazyarena.command;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.events.CrazyArenaArenaCreateEvent;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandAlreadyExistsException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandErrorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyArenaCommandMainCreate extends CrazyArenaPlayerCommandExecutor
{

	public CrazyArenaCommandMainCreate(final CrazyArena plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYARENA.COMMAND.ARENA.CREATED $Name$ $Type$", "CRAZYARENA.COMMAND.ARENA.SELECTED $Name$" })
	public void command(final Player player, final String[] args) throws CrazyException
	{
		if (args.length != 2)
			throw new CrazyCommandUsageException("<Name> <ArenaClass/Type>");
		final String name = args[0];
		if (plugin.getArenaByName(name) != null)
			throw new CrazyCommandAlreadyExistsException("Arena", name);
		final String type = args[1];
		@SuppressWarnings("rawtypes")
		Class<? extends Arena> clazz = plugin.getArenaTypes().get(type.toLowerCase());
		if (clazz == null)
			try
			{
				clazz = Class.forName(type).asSubclass(Arena.class);
			}
			catch (final ClassNotFoundException e)
			{
				try
				{
					clazz = Class.forName("de.st_ddt.crazyarena.arenas." + type).asSubclass(Arena.class);
				}
				catch (final ClassNotFoundException e2)
				{
					throw new CrazyCommandNoSuchException("ArenaClass/Type", type);
				}
			}
		Arena<?> arena = null;
		try
		{
			arena = clazz.getConstructor(String.class, World.class).newInstance(name, player.getWorld());
		}
		catch (final Exception e)
		{
			throw new CrazyCommandErrorException(e);
		}
		if (arena == null)
			throw new CrazyCommandException();
		plugin.getArenas().add(arena);
		plugin.getArenasByName().put(name.toLowerCase(), arena);
		plugin.getArenasByType().get(arena.getType()).add(arena);
		plugin.sendLocaleMessage("COMMAND.ARENA.CREATED", player, arena.getName(), arena.getType());
		new CrazyArenaArenaCreateEvent(arena, false).callEvent();
		plugin.getSelections().put(player.getName().toLowerCase(), arena);
		plugin.sendLocaleMessage("COMMAND.ARENA.SELECTED", player, arena.getName());
	}

	@Override
	public List<String> tab(final Player player, final String[] args)
	{
		if (args.length != 2)
			return null;
		final List<String> res = new LinkedList<String>();
		final String arg = args[1].toLowerCase();
		for (final String type : plugin.getArenaTypes().keySet())
			if (type.startsWith(arg))
				res.add(type);
		return res;
	}

	@Override
	public boolean hasAccessPermission(final Player player)
	{
		return PermissionModule.hasPermission(player, "crazyarena.arena.create");
	}
}
