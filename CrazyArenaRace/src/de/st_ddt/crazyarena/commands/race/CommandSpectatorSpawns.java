package de.st_ddt.crazyarena.commands.race;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.arenas.ArenaStatus;
import de.st_ddt.crazyarena.arenas.race.RaceArena;
import de.st_ddt.crazyplugin.commands.CrazyCommandListEditor;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatConverter;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.source.Localized;

public class CommandSpectatorSpawns extends CrazyCommandListEditor<RaceArena, Location>
{

	private final ListFormat listFormat;

	public CommandSpectatorSpawns(final RaceArena arena)
	{
		super(arena);
		listFormat = new ListFormat()
		{

			@Override
			public String listFormat(final CommandSender target)
			{
				return "$0$) $1$\n";
			}

			@Override
			@Localized("CRAZYARENARACE.COMMAND.SPECTATOR.LISTHEAD $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$")
			public String headFormat(final CommandSender target)
			{
				return CrazyLocale.getLocaleHead().getLocaleMessage(target, "CRAZYARENARACE.COMMAND.SPECTATOR.LISTHEAD");
			}

			@Override
			public String entryFormat(final CommandSender target)
			{
				return "$0$";
			}
		};
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (plugin.getStatus() == ArenaStatus.CONSTRUCTING)
			super.command(sender, args);
		else
			throw new CrazyCommandCircumstanceException("while in edit mode", plugin.getStatus().toString());
	}

	@Override
	protected List<Location> getCollection()
	{
		return plugin.getSpectatorSpawns();
	}

	@Override
	@Localized("CRAZYARENARACE.COMMAND.SPECTATOR.ADDINDEXED $Element$ $Index$")
	protected String addViaIndexLocale()
	{
		return "CRAZYARENARACE.COMMAND.SPECTATOR.ADDINDEXED";
	}

	@Override
	@Localized("CRAZYARENARACE.COMMAND.SPECTATOR.REMOVEINDEXED $Element$ $Index$")
	protected String removeViaIndexLocale()
	{
		return "CRAZYARENARACE.COMMAND.SPECTATOR.REMOVEINDEXED";
	}

	@Override
	protected ListFormat listFormat()
	{
		return listFormat;
	}

	@Override
	protected Location getEntry(final CommandSender sender, final String... args) throws CrazyException
	{
		return ChatConverter.stringToLocation(sender, args);
	}

	@Override
	@Localized("CRAZYARENARACE.COMMAND.SPECTATOR.ADDED $Element$")
	protected String addLocale()
	{
		return "CRAZYARENARACE.COMMAND.SPECTATOR.ADDED";
	}

	@Override
	@Localized("CRAZYARENARACE.COMMAND.SPECTATOR.REMOVED $Element$")
	protected String removeLocale()
	{
		return "CRAZYARENARACE.COMMAND.SPECTATOR.REMOVED";
	}

	@Override
	protected void saveChanges()
	{
		plugin.saveToFile();
	}
}
