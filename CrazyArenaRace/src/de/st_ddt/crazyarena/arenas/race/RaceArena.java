package de.st_ddt.crazyarena.arenas.race;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
import de.st_ddt.crazyarena.commands.race.CommandPlayerSpawns;
import de.st_ddt.crazyarena.commands.race.CommandRaceStages;
import de.st_ddt.crazyarena.commands.race.CommandSpectatorSpawns;
import de.st_ddt.crazyarena.exceptions.CrazyArenaExceedingParticipantsLimitException;
import de.st_ddt.crazyarena.listener.race.CrazyRaceArenaPlayerListener;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyarena.participants.race.RaceParticipant;
import de.st_ddt.crazyarena.tasks.CountDownTask;
import de.st_ddt.crazyarena.utils.ArenaChatHelper;
import de.st_ddt.crazyarena.utils.ArenaPlayerSaver;
import de.st_ddt.crazyarena.utils.SpawnList;
import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.commands.CrazyCommandExecutorInterface;
import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutor;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public class RaceArena extends Arena<RaceParticipant>
{

	protected final List<Location> playerSpawns = new ArrayList<Location>();
	protected final SpawnList spectatorSpawns = new SpawnList();
	protected final Map<String, Location> quitLocation = new HashMap<String, Location>();
	protected final List<RaceStage> targets = new ArrayList<RaceStage>();
	protected final CrazyCommandTreeExecutor<RaceArena> mainCommand;
	private int runnumber = 0;
	// Match Variables
	private CrazyRaceArenaPlayerListener playerMatchListener;
	private int winners = 0;
	private final Date startTime = new Date(0);
	private final int minParticipants = 2;

	public RaceArena(final String name, final ConfigurationSection config)
	{
		super(name, config);
		mainCommand = new CrazyCommandTreeExecutor<RaceArena>(this);
		// Player Spawns
		final ConfigurationSection playerConfig = config.getConfigurationSection("players");
		if (playerConfig != null)
			for (final String key : playerConfig.getKeys(false))
				playerSpawns.add(ObjectSaveLoadHelper.loadLocation(playerConfig.getConfigurationSection(key), null));
		// Spectator Spawns
		final ConfigurationSection spectatorConfig = config.getConfigurationSection("spectators");
		if (spectatorConfig != null)
			for (final String key : spectatorConfig.getKeys(false))
				spectatorSpawns.add(ObjectSaveLoadHelper.loadLocation(spectatorConfig.getConfigurationSection(key), null));
		// Race Targets
		final ConfigurationSection targetConfig = config.getConfigurationSection("targets");
		if (targetConfig != null)
		{
			RaceStage previous = null;
			for (final String key : targetConfig.getKeys(false))
			{
				final RaceStage temp = new RaceStage(this, targetConfig.getConfigurationSection(key));
				if (previous != null)
					previous.setNext(temp);
				previous = temp;
			}
		}
		registerCommands();
	}

	public RaceArena(final String name)
	{
		super(name);
		mainCommand = new CrazyCommandTreeExecutor<RaceArena>(this);
		registerCommands();
	}

	private void registerCommands()
	{
		mainCommand.addSubCommand(new CommandPlayerSpawns(this), "ps", "players", "playerspawns");
		mainCommand.addSubCommand(new CommandSpectatorSpawns(this), "ss", "spectators", "spectatorspawns");
		mainCommand.addSubCommand(new CommandRaceStages(this), "rs", "stages", "racestages");
	}

	@Override
	public final String getType()
	{
		return "Race";
	}

	public boolean command(final CommandSender sender, final String commandLabel, final String[] args) throws CrazyException
	{
		// EDIT OUTSOURCEN!
		if (commandLabel.startsWith("t"))
			if (commandLabel.equals("t") || commandLabel.equals("target") || commandLabel.equals("targets"))
			{
				if (status != ArenaStatus.CONSTRUCTING)
					throw new CrazyCommandCircumstanceException("when in edit mode", status.toString());
				// Targets
				return true;
			}
		return false;
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
	protected void save()
	{
		config.set("players", null);
		int i = 0;
		for (final Location location : playerSpawns)
			ObjectSaveLoadHelper.saveLocation(config, "players.spawn" + (i++) + ".", location, true, true);
		config.set("spectators", null);
		i = 0;
		for (final Location location : spectatorSpawns)
			ObjectSaveLoadHelper.saveLocation(config, "spectators.spawn" + (i++) + ".", location, true, true);
		config.set("targets", null);
		i = 0;
		for (final RaceStage target : targets)
			target.save(config, "targets.target" + (i++) + ".");
	}

	public Location getEmptyStartLocation()
	{
		final List<Location> locations = new LinkedList<Location>(playerSpawns);
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
				throw new CrazyArenaExceedingParticipantsLimitException(this, playerSpawns.size());
			else
				participant = new RaceParticipant(player, this, location, targets.get(0));
		// participant.setStage(targets.get(0));
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
	public CrazyCommandExecutorInterface getCommandExecutor()
	{
		return mainCommand;
	}

	public List<Location> getPlayerSpawns()
	{
		return playerSpawns;
	}

	public SpawnList getSpectatorSpawns()
	{
		return spectatorSpawns;
	}

	public List<RaceStage> getTargets()
	{
		return targets;
	}
}
