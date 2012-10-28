package de.st_ddt.crazyplugin.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;

public class CrazyCommandTreeExecutor<S extends CrazyPluginInterface> extends CrazyCommandExecutor<S> implements CrazyCommandTreeExecutorInterface<S>
{

	protected final TreeMap<String, CrazyCommandExecutorInterface> subExecutor = new TreeMap<String, CrazyCommandExecutorInterface>();
	protected CrazyCommandExecutorInterface defaultExecutor;

	public CrazyCommandTreeExecutor(final S plugin)
	{
		super(plugin);
		defaultExecutor = new CrazyCommandTreeDefaultExecutor<S>(plugin);
	}

	public CrazyCommandTreeExecutor(final S plugin, final CrazyCommandExecutor<?> defaultExecutor)
	{
		super(plugin);
		this.defaultExecutor = defaultExecutor;
	}

	@Override
	public void addSubCommand(final CrazyCommandExecutorInterface executor, final String... subCommandLabels)
	{
		for (final String label : subCommandLabels)
			subExecutor.put(label.toLowerCase(), executor);
	}

	@Override
	public void addSubCommand(final CrazyCommandExecutorInterface executor, final Collection<String> subCommandLabels)
	{
		for (final String label : subCommandLabels)
			subExecutor.put(label.toLowerCase(), executor);
	}

	@Override
	public void removeSubCommand(final String... subCommandLabels)
	{
		for (final String label : subCommandLabels)
			subExecutor.remove(label.toLowerCase());
	}

	@Override
	public void removeSubCommand(final Collection<String> subCommandLabels)
	{
		for (final String label : subCommandLabels)
			subExecutor.remove(label.toLowerCase());
	}

	@Override
	public CrazyCommandExecutorInterface getDefaultExecutor()
	{
		return defaultExecutor;
	}

	@Override
	public void setDefaultExecutor(final CrazyCommandExecutorInterface defaultExecutor)
	{
		this.defaultExecutor = defaultExecutor;
	}

	@Override
	public TreeMap<String, CrazyCommandExecutorInterface> getSubExecutors()
	{
		return subExecutor;
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length > 0)
			try
			{
				final CrazyCommandExecutorInterface executor = subExecutor.get(args[0].toLowerCase());
				if (executor != null)
				{
					executor.command(sender, ChatHelperExtended.shiftArray(args, 1));
					return;
				}
			}
			catch (final CrazyCommandException e)
			{
				e.addCommandPrefix(args[0]);
				throw e;
			}
		defaultExecutor.command(sender, args);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		if (args.length == 0)
			return new ArrayList<String>(subExecutor.keySet());
		final CrazyCommandExecutorInterface executor = subExecutor.get(args[0].toLowerCase());
		if (executor == null)
		{
			List<String> res = defaultExecutor.tab(sender, args);
			if (defaultExecutor instanceof CrazyCommandTreeDefaultExecutor || args.length > 1)
				return res;
			if (res == null)
				res = new ArrayList<String>();
			for (final String subCommand : subExecutor.keySet())
				if (subCommand.toLowerCase().startsWith(args[0]))
					res.add(subCommand);
			return res;
		}
		else if (args.length == 1)
		{
			final List<String> res = new ArrayList<String>();
			for (final String subCommand : subExecutor.keySet())
				if (subCommand.toLowerCase().startsWith(args[0]))
					res.add(subCommand);
			return res;
		}
		else
			return executor.tab(sender, ChatHelperExtended.shiftArray(args, 1));
	}

	private class CrazyCommandTreeDefaultExecutor<T extends CrazyPluginInterface> extends CrazyCommandExecutor<T>
	{

		public CrazyCommandTreeDefaultExecutor(final T plugin)
		{
			super(plugin);
		}

		@Override
		public void command(final CommandSender sender, final String[] args) throws CrazyException
		{
			String commandLabel = "(none)";
			if (args.length > 0)
				commandLabel = args[0];
			throw new CrazyCommandNoSuchException("Subcommand", commandLabel, subExecutor.keySet());
		}

		@Override
		public List<String> tab(final CommandSender sender, final String[] args)
		{
			if (args.length == 0)
				return new ArrayList<String>(subExecutor.keySet());
			else if (args.length == 1)
			{
				final List<String> res = new ArrayList<String>();
				for (final String subCommand : subExecutor.keySet())
					if (subCommand.toLowerCase().startsWith(args[0]))
						res.add(subCommand);
				return res;
			}
			else
				return null;
		}
	}
}
