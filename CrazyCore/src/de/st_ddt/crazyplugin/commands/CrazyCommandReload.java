package de.st_ddt.crazyplugin.commands;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHeaderProvider;
import de.st_ddt.crazyutil.paramitrisable.MapParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.MultiParamitrisable;

public class CrazyCommandReload<S extends ChatHeaderProvider> extends CrazyCommandExecutor<S>
{

	protected final Map<String, Reloadable> reloadables = new TreeMap<String, Reloadable>();
	protected final Reloadable allReloadable = new Reloadable()
	{

		@Override
		public void reload(final CommandSender sender) throws CrazyException
		{
			final Set<Reloadable> reloads = new HashSet<Reloadable>(reloadables.values());
			for (final Reloadable reloadable : reloads)
				if (reloadable.hasAccessPermission(sender))
					reloadable.reload(sender);
		}

		@Override
		public boolean hasAccessPermission(final CommandSender sender)
		{
			return true;
		}
	};

	public CrazyCommandReload(final S chatHeaderProvider)
	{
		super(chatHeaderProvider);
		addReloadable(allReloadable, "*", "all");
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		final MapParamitrisable<Reloadable> reload = new MapParamitrisable<Reloadable>("Reloadable", reloadables, getDefaultReloadable(), true);
		final MultiParamitrisable<Reloadable> reloads = new MultiParamitrisable<Reloadable>(reload);
		for (final String arg : args)
			reloads.setParameter(arg);
		for (final Reloadable reloadable : reloads.getValue())
			if (!reloadable.hasAccessPermission(sender))
				throw new CrazyCommandPermissionException();
		for (final Reloadable reloadable : reloads.getValue())
			reloadable.reload(sender);
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args)
	{
		return MapParamitrisable.tabHelp(reloadables, args[args.length - 1]);
	}

	public final void addReloadable(final Reloadable reloadable, final String... aliases)
	{
		for (final String alias : aliases)
			reloadables.put(alias.toLowerCase(), reloadable);
	}

	public Reloadable getDefaultReloadable()
	{
		return allReloadable;
	}

	public interface Reloadable
	{

		public void reload(CommandSender sender) throws CrazyException;

		public boolean hasAccessPermission(final CommandSender sender);
	}
}
