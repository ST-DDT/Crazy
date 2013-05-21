package de.st_ddt.crazyarena.commands.race;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import de.st_ddt.crazyarena.CrazyArenaRace;
import de.st_ddt.crazyarena.arenas.ArenaStatus;
import de.st_ddt.crazyarena.arenas.race.RaceArena;
import de.st_ddt.crazyarena.arenas.race.RaceStage;
import de.st_ddt.crazygeo.region.RealRoom;
import de.st_ddt.crazyplugin.commands.CrazyCommandListEditor;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.ListFormat;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.paramitrisable.LocationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.PolyRoomParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.RealRoomParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.poly.room.Room;
import de.st_ddt.crazyutil.poly.room.Sphere;
import de.st_ddt.crazyutil.source.Localized;

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
			@Localized("CRAZYARENARACE.COMMAND.STAGE.LISTHEAD $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$")
			public String headFormat(final CommandSender target)
			{
				return CrazyLocale.getLocaleHead().getLocaleMessage(target, "CRAZYARENARACE.COMMAND.STAGE.LISTHEAD");
			}

			@Override
			public String entryFormat(final CommandSender target)
			{
				return "$0$";
			}
		};
		addSubCommand(new CommandStageRename(arena), "n", "name", "rename");
		addSubCommand(new CommandStageCenter(arena), "c", "center");
		addSubCommand(new CommandStageTeleport(arena), "tp", "teleport");
		addSubCommand(new CommandStageRoom(arena), "r", "room");
		addSubCommand(new CommandStageGeo(arena), "g", "geo");
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
		return plugin.getStages();
	}

	@Override
	@Localized("CRAZYARENARACE.COMMAND.STAGE.ADDINDEXED $Element$ $Index$")
	protected String addViaIndexLocale()
	{
		return "CRAZYARENARACE.COMMAND.STAGE.ADDINDEXED";
	}

	@Override
	@Localized("CRAZYARENARACE.COMMAND.STAGE.REMOVEINDEXED $Element$ $Index$")
	protected String removeViaIndexLocale()
	{
		return "CRAZYARENARACE.COMMAND.STAGE.REMOVEINDEXED";
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
	@Localized("CRAZYARENARACE.COMMAND.STAGE.ADDED $Element$")
	protected String addLocale()
	{
		return "CRAZYARENARACE.COMMAND.STAGE.ADDED";
	}

	@Override
	protected String removeLocale()
	{
		return null;
	}

	@Override
	protected void saveChanges()
	{
		final List<RaceStage> stages = getCollection();
		final int last = stages.size() - 1;
		for (int i = 0; i < last; i++)
			stages.get(i).setNext(stages.get(i + 1));
		if (last >= 0)
			stages.get(last).setNext(null);
		plugin.saveToFile();
	}

	@Override
	protected void commandRemove(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("<index>");
		final int size = getCollection().size();
		if (size == 0)
			throw new CrazyCommandCircumstanceException("if list is not empty!");
		try
		{
			final int index = Integer.parseInt(args[0]) - 1;
			if (index < 0 || index >= size)
				throw new CrazyCommandParameterException(0, "Number (Integer)", "0 < x <= " + size);
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

	private class CommandStageRename extends RaceCommandExecutor
	{

		protected CommandStageRename(final RaceArena arena)
		{
			super(arena);
		}

		@Override
		@Localized("CRAZYARENARACE.COMMAND.STAGE.RENAMED $Index$ $Name$")
		public void command(final CommandSender sender, final String[] args) throws CrazyException
		{
			final List<RaceStage> stages = plugin.getStages();
			if (stages.size() == 0)
				throw new CrazyCommandCircumstanceException("if there is at least one stage!");
			if (args.length == 0)
				throw new CrazyCommandUsageException("<Index>");
			try
			{
				final int index = Integer.parseInt(args[0]);
				final RaceStage stage = stages.get(index);
				if (args.length > 1)
				{
					final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
					final StringParamitrisable name = new StringParamitrisable(stage.getName());
					ChatHelperExtended.readParameters(ChatHelperExtended.shiftArray(args, 1), params, name);
					stage.setName(name.getValue());
				}
				CrazyArenaRace.getPlugin().sendLocaleMessage("COMMAND.STAGE.RENAMED", sender, index, stage.getName());
			}
			catch (final NumberFormatException e)
			{
				throw new CrazyCommandParameterException(0, "Number (Integer)");
			}
		}
	}

	private class CommandStageCenter extends RaceCommandExecutor
	{

		protected CommandStageCenter(final RaceArena arena)
		{
			super(arena);
		}

		@Override
		@Localized("CRAZYARENARACE.COMMAND.STAGE.CENTER $Index$ $World$ $X$ $Y$ $Z$")
		public void command(final CommandSender sender, final String[] args) throws CrazyException
		{
			final List<RaceStage> stages = plugin.getStages();
			if (stages.size() == 0)
				throw new CrazyCommandCircumstanceException("if there is at least one stage!");
			if (args.length == 0)
				throw new CrazyCommandUsageException("<Index>");
			try
			{
				final int index = Integer.parseInt(args[0]);
				final RaceStage stage = stages.get(index);
				if (args.length > 1)
				{
					final Map<String, TabbedParamitrisable> params = new HashMap<String, TabbedParamitrisable>();
					final LocationParamitrisable location = new LocationParamitrisable(stage.getZone().getBasis());
					location.addAdvancedParams(params, "");
					ChatHelperExtended.readParameters(ChatHelperExtended.shiftArray(args, 1), params, location);
					stage.getZone().setBasis(location.getValue());
				}
				final Location location = stage.getZone().getBasis();
				CrazyArenaRace.getPlugin().sendLocaleMessage("COMMAND.STAGE.CENTER", sender, index, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
			}
			catch (final NumberFormatException e)
			{
				throw new CrazyCommandParameterException(0, "Number (Integer)");
			}
		}
	}

	private class CommandStageTeleport extends RacePlayerCommandExecutor
	{

		protected CommandStageTeleport(final RaceArena arena)
		{
			super(arena);
		}

		@Override
		@Localized("CRAZYARENARACE.COMMAND.STAGE.TELEPORTED $Index$")
		public void command(final Player player, final String[] args) throws CrazyException
		{
			final List<RaceStage> stages = plugin.getStages();
			if (stages.size() == 0)
				throw new CrazyCommandCircumstanceException("if there is at least one stage!");
			if (args.length != 1)
				throw new CrazyCommandUsageException("<Index>");
			try
			{
				final int index = Integer.parseInt(args[0]);
				final Location location = stages.get(index).getZone().getBasis();
				player.teleport(location, TeleportCause.COMMAND);
				CrazyArenaRace.getPlugin().sendLocaleMessage("COMMAND.STAGE.TELEPORTED", player, index);
			}
			catch (final NumberFormatException e)
			{
				throw new CrazyCommandParameterException(0, "Number (Integer)");
			}
		}
	}

	private class CommandStageRoom extends RaceCommandExecutor
	{

		protected CommandStageRoom(final RaceArena arena)
		{
			super(arena);
		}

		@Override
		@Localized("CRAZYARENARACE.COMMAND.STAGE.ROOM $Index$ $Room$")
		public void command(final CommandSender sender, final String[] args) throws CrazyException
		{
			final List<RaceStage> stages = plugin.getStages();
			if (stages.size() == 0)
				throw new CrazyCommandCircumstanceException("if there is at least one stage!");
			if (args.length == 0)
				throw new CrazyCommandUsageException("<Index>");
			try
			{
				final int index = Integer.parseInt(args[0]);
				final RaceStage stage = stages.get(index);
				if (args.length > 1)
				{
					final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
					final PolyRoomParamitrisable room = new PolyRoomParamitrisable(stage.getZone().getRoom());
					ChatHelperExtended.readParameters(ChatHelperExtended.shiftArray(args, 1), params, room);
					stage.setZone(new RealRoom<Room>(room.getValue(), stage.getZone().getBasis()));
				}
				getArenaPlugin().sendLocaleMessage("COMMAND.STAGE.ROOM", sender, index, stage.getZone().getRoom().toString());
			}
			catch (final NumberFormatException e)
			{
				throw new CrazyCommandParameterException(0, "Number (Integer)");
			}
		}
	}

	private class CommandStageGeo extends RaceCommandExecutor
	{

		protected CommandStageGeo(final RaceArena arena)
		{
			super(arena);
		}

		@Override
		@Localized("CRAZYARENARACE.COMMAND.STAGE.GEO $Index$ $Geo$")
		public void command(final CommandSender sender, final String[] args) throws CrazyException
		{
			final List<RaceStage> stages = plugin.getStages();
			if (stages.size() == 0)
				throw new CrazyCommandCircumstanceException("if there is at least one stage!");
			if (args.length == 0)
				throw new CrazyCommandUsageException("<Index>");
			try
			{
				final int index = Integer.parseInt(args[0]);
				final RaceStage stage = stages.get(index);
				if (args.length > 1)
				{
					final Map<String, Paramitrisable> params = new HashMap<String, Paramitrisable>();
					final RealRoomParamitrisable room = new RealRoomParamitrisable(sender, stage.getZone());
					ChatHelperExtended.readParameters(ChatHelperExtended.shiftArray(args, 1), params, room);
					stage.setZone(room.getValue());
				}
				getArenaPlugin().sendLocaleMessage("COMMAND.STAGE.ROOM", sender, index, stage.getZone().toString());
			}
			catch (final NumberFormatException e)
			{
				throw new CrazyCommandParameterException(0, "Number (Integer)");
			}
		}
	}
}
