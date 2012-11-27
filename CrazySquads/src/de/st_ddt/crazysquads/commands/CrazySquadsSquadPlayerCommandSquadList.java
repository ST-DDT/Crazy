package de.st_ddt.crazysquads.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.Tabbed;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazySquadsSquadPlayerCommandSquadList extends CrazySquadsSquadPlayerCommandExecutor
{

	public CrazySquadsSquadPlayerCommandSquadList(final CrazySquads plugin)
	{
		super(plugin);
	}

	@Override
	@Localized({ "CRAZYSQUADS.COMMAND.SQUAD.LIST.HEADER $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$", "CRAZYSQUADS.COMMAND.SQUAD.LIST.LISTFORMAT $Index$ $Entry$ $ChatHeader$" })
	public void command(final Player player, final Squad squad, final String[] args) throws CrazyException
	{
		final List<String> players = new ArrayList<String>();
		synchronized (squad.getMembers())
		{
			for (final Player member : squad.getMembers())
				players.add(member.getName());
		}
		plugin.sendLocaleList(player, "COMMAND.SQUAD.LIST.HEADER", "COMMAND.SQUAD.LIST.LISTFORMAT", null, -1, -1, players);
	}

	@Override
	public List<String> tab(final Player player, final Squad squad, final String[] args)
	{
		final Map<String, Tabbed> params = new HashMap<String, Tabbed>();
		final Tabbed tab = ChatHelperExtended.listTabHelp(params, player, null, null);
		return ChatHelperExtended.tabHelpWithPipe(player, args, params, tab);
	}
}
