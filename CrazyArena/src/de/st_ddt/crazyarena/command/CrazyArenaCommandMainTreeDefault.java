package de.st_ddt.crazyarena.command;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutor;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class CrazyArenaCommandMainTreeDefault extends CrazyArenaCommandExecutor
{

	private final CrazyCommandTreeExecutor<CrazyPluginInterface> treeExecutor;

	public CrazyArenaCommandMainTreeDefault(final CrazyArena plugin)
	{
		super(plugin);
		treeExecutor = plugin.getMainCommand();
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (sender instanceof Player && args.length != 0)
		{
			final Player player = (Player) sender;
			final Arena<?> arena = plugin.getArena(player);
			if (arena != null)
				try
				{
					arena.getCommandExecutor().command(player, args);
				}
				catch (final CrazyCommandNoSuchException e)
				{
					final String searched = e.getSearched();
					if (e.getType().equals("Subcommand") && searched.equals(args[0]))
					{
						for (int i = 1; i < args.length; i++)
							if (searched.equals(args[i]))
								throw e;
						final Set<String> alternatives = new HashSet<String>(e.getAlternatives());
						alternatives.addAll(treeExecutor.getSubExecutors().keySet());
						throw new CrazyCommandNoSuchException("Subcommand", args[0], alternatives);
					}
					else
						throw e;
				}
		}
		String commandLabel = "(none)";
		if (args.length > 0)
			commandLabel = args[0];
		throw new CrazyCommandNoSuchException("Subcommand", commandLabel, treeExecutor.getSubExecutors().keySet());
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (sender instanceof Player)
		{
			final Player player = (Player) sender;
			final Arena<?> arena = plugin.getArena(player);
			if (arena == null)
				return null;
			else
				return arena.getCommandExecutor().tab(player, args);
		}
		else
			return null;
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		if (sender instanceof Player)
		{
			final Player player = (Player) sender;
			final Arena<?> arena = plugin.getArena(player);
			if (arena == null)
				return true;
			else
				return arena.getCommandExecutor().hasAccessPermission(player);
		}
		else
			return true;
	}
}
