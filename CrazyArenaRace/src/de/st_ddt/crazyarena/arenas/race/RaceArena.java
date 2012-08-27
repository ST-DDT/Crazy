package de.st_ddt.crazyarena.arenas.race;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.arenas.ArenaStatus;
import de.st_ddt.crazyarena.exceptions.CrazyArenaExceedingParticipantsLimitException;
import de.st_ddt.crazyarena.listener.race.CrazyRaceArenaPlayerListener;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyarena.participants.race.RaceParticipant;
import de.st_ddt.crazyarena.tasks.CountDownTask;
import de.st_ddt.crazyarena.utils.ArenaChatHelper;
import de.st_ddt.crazyarena.utils.ArenaPlayerSaver;
import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public class RaceArena extends Arena<RaceParticipant>
{

	protected ArrayList<Location> starts = new ArrayList<Location>();
	protected final HashMap<String, Location> quitLocation = new HashMap<String, Location>();
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

	@Override
	public void save()
	{
		config.set("starts", null);
		int i = 0;
		for (final Location location : starts)
			ObjectSaveLoadHelper.saveLocation(config, "starts.start" + (i++) + ".", location, true, true);
		i = 0;
		config.set("targets", null);
		for (final RaceTarget target : targets)
			target.save(config, "targets.target" + (i++) + ".");
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
								broadcastLocaleMessage(false, "START.STARTED", CrazyPluginInterface.DateFormat.format(new Date()));
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
		broadcastLocaleMessage(true, true, true, true, "PARTICIPANT.REACHEDFINISH", raceParticipant.getName(), position, ArenaChatHelper.timeConverter(new Date().getTime() - startTime.getTime()));
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
	public void stop()
	{
		quitLocation.clear();
		super.stop();
	}
}
