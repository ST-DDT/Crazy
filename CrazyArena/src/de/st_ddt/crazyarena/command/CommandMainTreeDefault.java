package de.st_ddt.crazyarena.command;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutor;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class CommandMainTreeDefault extends CommandExecutor
{

	private final CrazyCommandTreeExecutor<CrazyPluginInterface> treeExecutor;

	public CommandMainTreeDefault(final CrazyArena plugin)
	{
		super(plugin);
		treeExecutor = plugin.getMainCommand();
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		Arena<?> arena = plugin.getSelections().get(sender);
		if (arena == null && sender instanceof Player)
			arena = plugin.getArenaByPlayer(((Player) sender));
		if (arena == null)
		{
			String commandLabel = "(none)";
			if (args.length > 0)
				commandLabel = args[0];
			throw new CrazyCommandNoSuchException("Subcommand", commandLabel, treeExecutor.getSubExecutors().keySet());
		}
		else
			try
			{
				arena.getCommandExecutor().command(sender, args);
			}
			catch (final CrazyCommandNoSuchException e)
			{
				final String searched = e.getSearched();
				if (e.getType().equals("Subcommand") && searched.equals(args[0]))
				{
					for (int i = 1; i < args.length; i++)
						if (searched.equals(args[i]))
							throw e;
					final Set<String> alternatives = new TreeSet<String>(e.getAlternatives());
					alternatives.addAll(treeExecutor.getSubExecutors().keySet());
					throw new CrazyCommandNoSuchException("Subcommand", args[0], alternatives);
				}
				else
					throw e;
			}
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		Arena<?> arena = plugin.getSelections().get(sender.getName().toLowerCase());
		if (arena == null && sender instanceof Player)
			arena = plugin.getArenaByPlayer(((Player) sender));
		if (arena == null)
			return null;
		else
			return arena.getCommandExecutor().tab(sender, args);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		Arena<?> arena = plugin.getSelections().get(sender.getName().toLowerCase());
		if (arena == null && sender instanceof Player)
			arena = plugin.getArenaByPlayer(((Player) sender));
		if (arena == null)
			return true;
		else
			return arena.getCommandExecutor().hasAccessPermission(sender);
	}
}
