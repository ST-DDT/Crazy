package de.st_ddt.crazysquads.commands;

import java.util.List;

import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;

public abstract class CrazySquadsSquadPlayerCommandExecutor extends CrazySquadsPlayerCommandExecutor
{

	public CrazySquadsSquadPlayerCommandExecutor(final CrazySquads plugin)
	{
		super(plugin);
	}

	@Override
	public final void command(final Player player, final String[] args) throws CrazyException
	{
		final Squad squad = plugin.getSquads().get(player);
		if (squad == null)
			throw new CrazyCommandUsageException("when being a member of a squad!");
		command(player, squad, args);
	}

	public abstract void command(final Player player, Squad squad, final String[] args) throws CrazyException;

	@Override
	public final List<String> tab(final Player player, final String[] args)
	{
		final Squad squad = plugin.getSquads().get(player);
		if (squad == null)
			return null;
		else
			return tab(player, squad, args);
	}

	public List<String> tab(final Player player, final Squad squad, final String[] args)
	{
		return null;
	}

	@Override
	public final boolean hasAccessPermission(final Player player)
	{
		final Squad squad = plugin.getSquads().get(player);
		if (squad == null)
			return true;
		else
			return hasAccessPermission(player, squad);
	}

	public boolean hasAccessPermission(final Player player, final Squad squad)
	{
		return true;
	}
}
