package de.st_ddt.crazyarena.commands.race;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.arenas.race.RaceArena;
import de.st_ddt.crazyplugin.commands.CrazyCommandListEditor;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.locales.Localized;

public class CommandPlayerSpawns extends CrazyCommandListEditor<RaceArena, Location>
{

	private final ListFormat listFormat;

	public CommandPlayerSpawns(final RaceArena arena)
	{
		super(arena);
		listFormat = new ListFormat()
		{

			@Override
			public String listFormat(CommandSender target)
			{
				return "$0$) $1$\n";
			}

			@Override
			public String headFormat(CommandSender target)
			{
				return CrazyLocale.getLocaleHead().getLocaleMessage(target, "CRAZYARENARACE.COMMAND.PLAYER.LISTHEAD");
			}

			@Override
			public String entryFormat(CommandSender target)
			{
				return "$0$";
			}
		};
	}

	@Override
	public List<Location> getCollection()
	{
		return plugin.getStarts();
	}

	@Override
	@Localized("CRAZYARENARACE.COMMAND.PLAYER.ADDINDEXED $Element$ $Index$")
	public String addViaIndexLocale()
	{
		return "CRAZYARENARACE.COMMAND.PLAYER.ADDINDEXED";
	}

	@Override
	@Localized("CRAZYARENARACE.COMMAND.PLAYER.REMOVEINDEXED $Element$ $Index$")
	public String removeViaIndexLocale()
	{
		return "CRAZYARENARACE.COMMAND.PLAYER.REMOVEINDEXED";
	}

	@Override
	public ListFormat listFormat()
	{
		return listFormat;
	}

	@Override
	public Location getEntry(final String... args) throws CrazyException
	{
		// EDIT Automatisch generierter Methodenstub
		return null;
	}

	@Override
	public String addLocale()
	{
		// EDIT Automatisch generierter Methodenstub
		return null;
	}

	@Override
	public String removeLocale()
	{
		// EDIT Automatisch generierter Methodenstub
		return null;
	}
}
