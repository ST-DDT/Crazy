package de.st_ddt.crazyarena.commands.race;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.arenas.ArenaStatus;
import de.st_ddt.crazyarena.arenas.race.RaceArena;
import de.st_ddt.crazyarena.arenas.race.RaceStage;
import de.st_ddt.crazyplugin.commands.CrazyCommandListEditor;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.RealRoomParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;
import de.st_ddt.crazyutil.poly.room.Sphere;

public class CommandRaceStages extends CrazyCommandListEditor<RaceArena, RaceStage>
{

	private final ListFormat listFormat;

	public CommandRaceStages(final RaceArena arena)
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
			public String headFormat(final CommandSender target)
			{
				return CrazyLocale.getLocaleHead().getLocaleMessage(target, "CRAZYARENARACE.COMMAND.TARGET.LISTHEAD");
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
	protected List<RaceStage> getCollection()
	{
		return plugin.getTargets();
	}

	@Override
	@Localized("CRAZYARENARACE.COMMAND.TARGET.ADDINDEXED $Element$ $Index$")
	protected String addViaIndexLocale()
	{
		return "CRAZYARENARACE.COMMAND.TARGET.ADDINDEXED";
	}

	@Override
	@Localized("CRAZYARENARACE.COMMAND.TARGET.REMOVEINDEXED $Element$ $Index$")
	protected String removeViaIndexLocale()
	{
		return "CRAZYARENARACE.COMMAND.TARGET.REMOVEINDEXED";
	}

	@Override
	protected ListFormat listFormat()
	{
		return listFormat;
	}

	@Override
	protected RaceStage getEntry(final CommandSender sender, final String... args) throws CrazyException
	{
		final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
		final StringParamitrisable name = new StringParamitrisable(null);
		params.put("name", name);
		final RealRoomParamitrisable zone = new RealRoomParamitrisable(sender, new Sphere(5));
		params.put("z", zone);
		params.put("zone", zone);
		ChatHelperExtended.readParameters(args, params, name, zone);
		return new RaceStage(plugin, name.getValue(), zone.getValue());
	}

	@Override
	@Localized("CRAZYARENARACE.COMMAND.TARGET.ADDED $Element$")
	protected String addLocale()
	{
		return "CRAZYARENARACE.COMMAND.TARGET.ADDED";
	}

	@Override
	protected String removeLocale()
	{
		return null;
	}

	@Override
	protected void saveChanges()
	{
		final List<RaceStage> targets = getCollection();
		final int length = targets.size();
		for (int i = 0; i < length - 1; i++)
			targets.get(i).setNext(targets.get(i + 1));
		targets.get(length).setNext(null);
		plugin.saveToFile();
	}

	@Override
	protected void commandRemove(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("<index>");
		try
		{
			final int index = Integer.parseInt(args[0]);
			final RaceStage elem = getCollection().remove(index);
			saveChanges();
			CrazyLocale.getLocaleHead().getLanguageEntry(removeViaIndexLocale()).sendMessage(sender, elem, index);
			return;
		}
		catch (final NumberFormatException e)
		{
			throw new CrazyCommandParameterException(0, "Number (Integer)");
		}
	}
}
