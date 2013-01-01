package de.st_ddt.crazyarena.arenas.race;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.arenas.ArenaStatus;
import de.st_ddt.crazyarena.command.CrazyArenaArenaPlayerCommandExecutor;
import de.st_ddt.crazyarena.exceptions.CrazyArenaExceedingParticipantsLimitException;
import de.st_ddt.crazyarena.listener.race.CrazyRaceArenaPlayerListener;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyarena.participants.race.RaceParticipant;
import de.st_ddt.crazyarena.tasks.CountDownTask;
import de.st_ddt.crazyarena.utils.ArenaChatHelper;
import de.st_ddt.crazyarena.utils.ArenaPlayerSaver;
import de.st_ddt.crazyarena.utils.SpawnList;
import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandErrorException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatConverter;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public class RaceArena extends Arena<RaceParticipant>
{

	protected ArrayList<Location> starts = new ArrayList<Location>();
	protected SpawnList spectatorSpawns = new SpawnList();
	protected final Map<String, Location> quitLocation = new HashMap<String, Location>();
	protected ArrayList<RaceTarget> targets = new ArrayList<RaceTarget>();
	private int runnumber = 0;
	// Match Variables
	private CrazyRaceArenaPlayerListener playerMatchListener;
	private int winners = 0;
	private final Date startTime = new Date(0);
	private final int minParticipants = 2;

	public RaceArena(final String name, final ConfigurationSection config)
	{
		super(name, config);
		final ConfigurationSection startConfig = config.getConfigurationSection("starts");
		if (startConfig != null)
			for (final String key : startConfig.getKeys(false))
				starts.add(ObjectSaveLoadHelper.loadLocation(startConfig.getConfigurationSection(key), null));
		final ConfigurationSection targetConfig = config.getConfigurationSection("targets");
		if (targetConfig != null)
		{
			RaceTarget previous = null;
			for (final String key : targetConfig.getKeys(false))
			{
				final RaceTarget temp = new RaceTarget(this, targetConfig.getConfigurationSection(key));
				if (previous != null)
					previous.setNext(temp);
				previous = temp;
			}
		}
		final ConfigurationSection spectatorConfig = config.getConfigurationSection("spectators");
		if (spectatorConfig != null)
			for (final String key : spectatorConfig.getKeys(false))
				spectatorSpawns.add(ObjectSaveLoadHelper.loadLocation(spectatorConfig.getConfigurationSection(key), null));
	}

	public RaceArena(final String name)
	{
		super(name);
	}

	@Override
	public final String getType()
	{
		return "Race";
	}

	public boolean command(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		// EDIT OUTSOURCEN!
		if (commandLabel.startsWith("p"))
			if (commandLabel.equals("p") || commandLabel.equals("player") || commandLabel.equals("players"))
			{
				if (status != ArenaStatus.CONSTRUCTING)
					throw new CrazyCommandCircumstanceException("when in edit mode", status.toString());
				commandPlayerSpawns(sender, args);
				return true;
			}
		if (commandLabel.startsWith("s"))
			if (commandLabel.equals("s") || commandLabel.equals("spectator") || commandLabel.equals("spectators"))
			{
				if (status != ArenaStatus.CONSTRUCTING)
					throw new CrazyCommandCircumstanceException("when in edit mode", status.toString());
				commandSpectatorSpawns(sender, args);
				return true;
			}
		if (commandLabel.startsWith("t"))
			if (commandLabel.equals("t") || commandLabel.equals("target") || commandLabel.equals("targets"))
			{
				if (status != ArenaStatus.CONSTRUCTING)
					throw new CrazyCommandCircumstanceException("when in edit mode", status.toString());
				// Targets
				return true;
			}
		if (commandLabel.equals("options") || commandLabel.equals("mode"))
		{
			if (status.isActive())
				throw new CrazyCommandCircumstanceException("while arena is idle", status.toString());
			// Options (minParticipants)
			return true;
		}
		return false;
	}

	private void commandPlayerSpawns(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!hasPermission(sender, "playerspawns"))
			throw new CrazyCommandPermissionException();
		if (args.length == 0)
		{
			commandPlayerSpawnsList(sender, args);
			return;
		}
		final String commandLabel = args[0].toLowerCase();
		final String[] newArgs = ChatHelperExtended.shiftArray(args, 1);
		try
		{
			if (commandLabel.equals("add"))
			{
				commandPlayerSpawnsAdd(sender, newArgs);
				return;
			}
			else if (commandLabel.equals("insert"))
			{
				commandPlayerSpawnsInsert(sender, newArgs);
				return;
			}
			else if (commandLabel.equals("del") || commandLabel.equals("delete") || commandLabel.equals("rem") || commandLabel.equals("remove"))
			{
				commandPlayerSpawnsRemove(sender, newArgs);
				return;
			}
			else if (commandLabel.equals("list"))
			{
				commandPlayerSpawnsList(sender, newArgs);
				return;
			}
		}
		catch (final CrazyCommandException e)
		{
			e.shiftCommandIndex();
			throw e;
		}
		throw new CrazyCommandUsageException("/crazyarena player add [[World] <X> <Y> <Z> [<Yaw> <Pitch>]]", "/crazyarena player insert <Index> [[World] <X> <Y> <Z> [<Yaw> <Pitch>]]", "/crazyarena player del(ete)/rem(ove) <Index>", "/crazyarena player [list]");
	}

	private void commandPlayerSpawnsAdd(final CommandSender sender, final String[] args) throws CrazyException
	{
		starts.add(ChatConverter.stringToLocation(sender, args));
		saveToFile();
		sendLocaleMessage("PLAYERSPAWNS.ADDED", sender, starts.size());
	}

	private void commandPlayerSpawnsInsert(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length == 0)
			throw new CrazyCommandUsageException("/crazyarena player insert <Index> [[World] [[X] [Y] [Z]]]");
		try
		{
			final int pos = Integer.parseInt(args[0]);
			starts.add(pos, ChatConverter.stringToLocation(sender, ChatHelperExtended.shiftArray(args, 1)));
			saveToFile();
			sendLocaleMessage("PLAYERSPAWNS.ADDED", sender, pos);
		}
		catch (final CrazyCommandException e)
		{
			e.shiftCommandIndex();
			throw e;
		}
		catch (final NumberFormatException e)
		{
			throw new CrazyCommandParameterException(0, "Number (Integer)");
		}
		catch (final IndexOutOfBoundsException e)
		{
			throw new CrazyCommandErrorException(e);
		}
	}

	private void commandPlayerSpawnsRemove(final CommandSender sender, final String[] args) throws CrazyException
	{
		try
		{
			final int pos = Integer.parseInt(args[0]);
			starts.remove(pos);
			saveToFile();
			sendLocaleMessage("PLAYERSPAWNS.REMOVED", sender, pos);
		}
		catch (final NumberFormatException e)
		{
			throw new CrazyCommandParameterException(0, "Number (Integer)");
		}
		catch (final IndexOutOfBoundsException e)
		{
			throw new CrazyCommandErrorException(e);
		}
	}

	private void commandPlayerSpawnsList(final CommandSender sender, final String[] args)
	{
		final int length = starts.size();
		sendLocaleMessage("PLAYERSPAWNS.LIST.HEAD", sender, name);
		sendLocaleMessage("PLAYERSPAWNS.LIST.SEPERATOR", sender);
		for (int i = 0; i < length; i++)
		{
			final Location start = starts.get(i);
			sendLocaleMessage("PLAYERSPAWNS.LIST.ENTRY", sender, i + 1, start.getWorld(), start.getX(), start.getY(), start.getZ(), start.getYaw(), start.getPitch());
		}
	}

	private void commandSpectatorSpawns(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!hasPermission(sender, "spectatorspawns"))
			throw new CrazyCommandPermissionException();
		if (args.length == 0)
		{
			commandSpectatorSpawnsList(sender, args);
			return;
		}
		final String commandLabel = args[0].toLowerCase();
		final String[] newArgs = ChatHelperExtended.shiftArray(args, 1);
		try
		{
			if (commandLabel.equals("add"))
			{
				commandSpectatorSpawnsAdd(sender, newArgs);
				return;
			}
			else if (commandLabel.equals("insert"))
			{
				commandSpectatorSpawnsInsert(sender, newArgs);
				return;
			}
			else if (commandLabel.equals("del") || commandLabel.equals("delete") || commandLabel.equals("rem") || commandLabel.equals("remove"))
			{
				commandSpectatorSpawnsRemove(sender, newArgs);
				return;
			}
			else if (commandLabel.equals("list"))
			{
				commandSpectatorSpawnsList(sender, newArgs);
				return;
			}
		}
		catch (final CrazyCommandException e)
		{
			e.shiftCommandIndex();
			throw e;
		}
		throw new CrazyCommandUsageException("/crazyarena spectator add [[World] <X> <Y> <Z> [<Yaw> <Pitch>]]", "/crazyarena spectator insert <Index> [[World] <X> <Y> <Z> [<Yaw> <Pitch>]]", "/crazyarena spectator del(ete)/rem(ove) <Index>", "/crazyarena spectator [list]");
	}

	private void commandSpectatorSpawnsAdd(final CommandSender sender, final String[] args) throws CrazyException
	{
		spectatorSpawns.add(ChatConverter.stringToLocation(sender, args));
		saveToFile();
		sendLocaleMessage("SPECTATORSPAWNS.ADDED", sender, spectatorSpawns.size());
	}

	private void commandSpectatorSpawnsInsert(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (args.length == 0)
			throw new CrazyCommandUsageException("/crazyarena spectator insert <Index> [[World] [[X] [Y] [Z]]]");
		try
		{
			final int pos = Integer.parseInt(args[0]);
			spectatorSpawns.add(pos, ChatConverter.stringToLocation(sender, ChatHelperExtended.shiftArray(args, 1)));
			saveToFile();
			sendLocaleMessage("SPECTATORSPAWNS.ADDED", sender, pos);
		}
		catch (final CrazyCommandException e)
		{
			e.shiftCommandIndex();
			throw e;
		}
		catch (final NumberFormatException e)
		{
			throw new CrazyCommandParameterException(0, "Number (Integer)");
		}
		catch (final IndexOutOfBoundsException e)
		{
			throw new CrazyCommandErrorException(e);
		}
	}

	private void commandSpectatorSpawnsRemove(final CommandSender sender, final String[] args) throws CrazyException
	{
		try
		{
			final int pos = Integer.parseInt(args[0]);
			spectatorSpawns.remove(pos);
			saveToFile();
			sendLocaleMessage("SPECTATORSPAWNS.REMOVED", sender, pos);
		}
		catch (final NumberFormatException e)
		{
			throw new CrazyCommandParameterException(0, "Number (Integer)");
		}
		catch (final IndexOutOfBoundsException e)
		{
			throw new CrazyCommandErrorException(e);
		}
	}

	private void commandSpectatorSpawnsList(final CommandSender sender, final String[] args)
	{
		final int length = spectatorSpawns.size();
		sendLocaleMessage("SPECTATORSPAWNS.LIST.HEAD", sender, name);
		sendLocaleMessage("SPECTATORSPAWNS.LIST.SEPERATOR", sender);
		for (int i = 0; i < length; i++)
		{
			final Location spawn = spectatorSpawns.get(i);
			sendLocaleMessage("SPECTATORSPAWNS.LIST.ENTRY", sender, i + 1, spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getYaw(), spawn.getPitch());
		}
	}

	@Override
	protected boolean checkArena(final CommandSender sender)
	{
		// EDIT Automatisch generierter Methodenstub
		// minParticpants > 1
		// Starts > minParticipants
		// SpectatorsSpawns > 0
		// TargetsCount > 0
		return true;
	}

	@Override
	public void save()
	{
		config.set("starts", null);
		int i = 0;
		for (final Location location : starts)
			ObjectSaveLoadHelper.saveLocation(config, "starts.start" + (i++) + ".", location, true, true);
		config.set("targets", null);
		i = 0;
		for (final RaceTarget target : targets)
			target.save(config, "targets.target" + (i++) + ".");
		config.set("spectators", null);
		i = 0;
		for (final RaceTarget target : targets)
			target.save(config, "spectators.spectators" + (i++) + ".");
	}

	public Location getEmptyStartLocation()
	{
		final ArrayList<Location> locations = new ArrayList<Location>(starts);
		for (final RaceParticipant participant : getParticipants())
			if (participant.isPlayer())
				locations.remove(participant.getStart());
		if (locations.size() == 0)
			return null;
		return locations.get(0);
	}

	@Override
	public void join(final Player player, final boolean rejoin) throws CrazyException
	{
		RaceParticipant participant = getParticipant(player);
		final Location location = getEmptyStartLocation();
		if (participant == null)
			if (location == null)
				throw new CrazyArenaExceedingParticipantsLimitException(this, starts.size());
			else
			{
				participant = new RaceParticipant(player, this, location, targets.get(0));
				participant.setTarget(targets.get(0));
			}
		if (rejoin && status == ArenaStatus.PLAYING)
			participant.setParticipantType(ParticipantType.PARTICIPANT);
		else
			participant.setParticipantType(ParticipantType.SELECTING);
		if (status == ArenaStatus.READY)
			status = ArenaStatus.SELECTING;
		broadcastLocaleMessage(false, "PARTICIPANT.JOINED.BROADCAST", player.getName());
		sendLocaleMessage("PARTICIPANT.JOINED.READYCOMMAND", player);
	}

	@Override
	public boolean ready(final Player player)
	{
		final RaceParticipant participant = getParticipant(player);
		if (status != ArenaStatus.SELECTING)
			return false;
		if (participant.getParticipantType() == ParticipantType.SELECTING)
		{
			sendLocaleMessage("PARTICIPANT.READY", player);
			participant.setParticipantType(ParticipantType.READY);
			if (getParticipants(ParticipantType.READY).size() > minParticipants)
				if (getParticipants(ParticipantType.SELECTING).size() == 0)
				{
					status = ArenaStatus.WAITING;
					broadcastLocaleMessage(false, "START.QUEUED");
					final RaceArena arena = this;
					CountDownTask.startCountDown(5, this, "START.COUNTDOWN", new Runnable()
					{

						@Override
						public void run()
						{
							if (arena.getStatus() == ArenaStatus.WAITING)
							{
								runnumber++;
								arena.status = ArenaStatus.PLAYING;
								broadcastLocaleMessage(false, "START.STARTED", CrazyLightPluginInterface.DATETIMEFORMAT.format(new Date()));
							}
							else
								broadcastLocaleMessage(false, "START.ABORTED");
						}
					});
				}
			return true;
		}
		return false;
	}

	@Override
	public void leave(final Player player, final boolean kicked)
	{
		quitLocation.remove(player.getName().toLowerCase());
		final RaceParticipant participant = participants.remove(player.getName().toLowerCase());
		final ArenaPlayerSaver saver = participant.getSaver();
		saver.restore(player);
		sendLocaleMessage("PARTICIPANT.QUIT", player, name);
		broadcastLocaleMessage(false, "PARTICIPANT.QUIT.BROADCAST", player.getName());
		if (getParticipants(ParticipantType.PARTICIPANT).size() == 0)
			stop();
	}

	@Override
	public void quitgame(final Player player)
	{
		quitLocation.put(player.getName().toLowerCase(), player.getLocation().clone());
		final RaceParticipant participant = getParticipant(player);
		final ArenaPlayerSaver saver = participant.getSaver();
		saver.restore(player);
		saver.reset();
		participant.setParticipantType(ParticipantType.QUITEDPARTICIPANT);
		if (getParticipants(ParticipantType.PARTICIPANT).size() == 0)
			stop();
	}

	@Override
	public int getRunNumber()
	{
		return runnumber;
	}

	@Override
	public long getRejoinTime()
	{
		return 30000;
	}

	public void reachFinish(final RaceParticipant raceParticipant)
	{
		final int position = winners++;
		raceParticipant.setParticipantType(ParticipantType.SPECTATOR);
		broadcastLocaleMessage(true, true, true, true, "PARTICIPANT.REACHEDFINISH", raceParticipant.getName(), position, ArenaChatHelper.timeConverter(new Date().getTime() - startTime.getTime(), raceParticipant.getPlayer()));
		// EDIT queue kick task for to slow players
		if (getParticipants(ParticipantType.PARTICIPANT).size() == 0)
			stop();
	}

	@Override
	public void registerMatchListener()
	{
		playerMatchListener = new CrazyRaceArenaPlayerListener(this);
		Bukkit.getPluginManager().registerEvents(playerMatchListener, CrazyArena.getPlugin());
	}

	@Override
	public void unregisterMatchListener()
	{
		if (playerMatchListener != null)
			HandlerList.unregisterAll(playerMatchListener);
		playerMatchListener = null;
	}

	@Override
	public void registerArenaListener()
	{
		// EDIT Automatisch generierter Methodenstub
	}

	@Override
	public void unregisterArenaListener()
	{
		// EDIT Automatisch generierter Methodenstub
	}

	@Override
	public void stop()
	{
		quitLocation.clear();
		super.stop();
	}

	@Override
	public CrazyArenaArenaPlayerCommandExecutor<Arena<RaceParticipant>> getCommandExecutor()
	{
		// EDIT Automatisch generierter Methodenstub
		return null;
	}
}
