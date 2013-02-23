package de.st_ddt.crazyarena.arenas.race;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.CrazyArenaRace;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.arenas.ArenaStatus;
import de.st_ddt.crazyarena.commands.race.CommandPlayerSpawns;
import de.st_ddt.crazyarena.commands.race.CommandRaceStages;
import de.st_ddt.crazyarena.commands.race.CommandSpectatorSpawns;
import de.st_ddt.crazyarena.exceptions.CrazyArenaExceedingParticipantsLimitException;
import de.st_ddt.crazyarena.listener.race.CrazyRaceArenaPlayerListener;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyarena.participants.race.RaceParticipant;
import de.st_ddt.crazyarena.score.Score;
import de.st_ddt.crazyarena.score.Score.ScoreEntry;
import de.st_ddt.crazyarena.score.ScoreOutputModifier;
import de.st_ddt.crazyarena.tasks.CountDownTask;
import de.st_ddt.crazyarena.utils.ArenaChatHelper;
import de.st_ddt.crazyarena.utils.ArenaPlayerSaver;
import de.st_ddt.crazyarena.utils.SignRotation;
import de.st_ddt.crazyarena.utils.SpawnList;
import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.modes.IntegerMode;
import de.st_ddt.crazyutil.modes.LongMode;
import de.st_ddt.crazyutil.source.Localized;

public class RaceArena extends Arena<RaceParticipant>
{

	private final static int DEFAUTLMINPARTICIPANTS = 2;
	private static final int DEFAUTLSTARTDELAY = 5;
	private final static long DEFAULTKICKSLOWPLAYERS = 30;
	private final static long DEFAULTEXPIRATIONTIME = 1000L * 60 * 60 * 24 * 30;
	protected final List<Location> playerSpawns = new ArrayList<Location>();
	protected final SpawnList spectatorSpawns = new SpawnList();
	protected final Map<String, Location> quitLocation = new HashMap<String, Location>();
	protected final List<RaceStage> stages = new ArrayList<RaceStage>();
	private int runnumber = 0;
	// Match Variables
	private CrazyRaceArenaPlayerListener playerMatchListener;
	private long startTime = 0;
	private int minParticipants;
	private int startDelay;
	private long kickSlowPlayers;
	private final Score currentScore = new Score(this, new String[] { "nextstage" }, new String[] { "nextstageindex", "timeelapsed" }, new Comparator<Score.ScoreEntry>()
	{

		@Override
		public int compare(final ScoreEntry o1, final ScoreEntry o2)
		{
			final int index1 = o1.getValue("nextstageindex").intValue();
			final int res = -compare(index1, o2.getValue("nextstageindex").intValue());
			// =0 => same stage => calc distance to next target
			if (res == 0)
				if (index1 >= stages.size())
					return -compare(o2.getValue("timeelapsed").intValue(), o2.getValue("timeelapsed").intValue());
				else
					return compare(o1.getPlayer(), o2.getPlayer(), stages.get(index1 - 1).getZone().getBasis());
			else
				return res;
		}

		private int compare(final Player player1, final Player player2, final Location basis)
		{
			if (player1 == null)
				if (player2 == null)
					return 0;
				else
					return -1;
			else if (player2 == null)
				return 1;
			else
				return Double.compare(player1.getLocation().distance(basis), player1.getLocation().distance(basis));
		}

		private int compare(final int i1, final int i2)
		{
			return (i1 < i2 ? -1 : (i1 == i2 ? 0 : 1));
		}
	}, new ScoreOutputModifier()
	{

		@Override
		public String getStringOutput(final String name, final String value)
		{
			return value;
		}

		@Override
		public String getDoubleOutput(final String name, final Double value)
		{
			if (value == null)
				return "UNKNOWN";
			else if (name.equals("nextstageindex"))
				return Integer.toString(value.intValue());
			else if (name.equals("timeelapsed"))
				return ArenaChatHelper.timeConverter(value.longValue());
			else
				return value.toString();
		}
	});
	private final Score permanentScore = new Score(this, new String[] { "date", "time" }, new String[] { "timeelapsed", "leadfirst", "opponents" }, "timeelapsed", false, new ScoreOutputModifier()
	{

		@Override
		public String getStringOutput(final String name, final String value)
		{
			return value;
		}

		@Override
		public String getDoubleOutput(final String name, final Double value)
		{
			if (value == null)
				return "UNKNOWN";
			else if (name.equals("timeelapsed") || name.equals("leadfirst"))
				return ArenaChatHelper.timeConverter(value.longValue());
			else
				return value.toString();
		}
	});

	public RaceArena(final String name, final ConfigurationSection config)
	{
		super(name, config);
		minParticipants = config.getInt("minParticipants", DEFAUTLMINPARTICIPANTS);
		startDelay = config.getInt("startDelay", DEFAUTLSTARTDELAY);
		kickSlowPlayers = config.getLong("kickSlowPlayers", DEFAULTKICKSLOWPLAYERS);
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
		// Race Stages
		final ConfigurationSection stagesConfig = config.getConfigurationSection("stages");
		if (stagesConfig != null)
		{
			RaceStage previous = null;
			for (final String key : stagesConfig.getKeys(false))
			{
				final RaceStage temp = new RaceStage(this, stagesConfig.getConfigurationSection(key));
				stages.add(temp);
				if (previous != null)
					previous.setNext(temp);
				previous = temp;
			}
		}
		// Scores
		currentScore.setExpiringTime(Long.MAX_VALUE);
		currentScore.load(config.getConfigurationSection("currentScore"), false, true);
		permanentScore.setExpiringTime(config.getLong("scoreExpiringTime", DEFAULTEXPIRATIONTIME));
		permanentScore.load(config.getConfigurationSection("permanentScore"), true, true);
		registerCommands();
	}

	public RaceArena(final String name)
	{
		super(name);
		minParticipants = DEFAUTLMINPARTICIPANTS;
		kickSlowPlayers = DEFAULTKICKSLOWPLAYERS;
		currentScore.setExpiringTime(Long.MAX_VALUE);
		permanentScore.setExpiringTime(DEFAULTEXPIRATIONTIME);
		registerCommands();
	}

	private void registerCommands()
	{
		mainCommand.addSubCommand(new CommandPlayerSpawns(this), "ps", "players", "playerspawns");
		mainCommand.addSubCommand(new CommandSpectatorSpawns(this), "ss", "spectators", "spectatorspawns");
		mainCommand.addSubCommand(new CommandRaceStages(this), "rs", "stages", "racestages");
		registerModes();
	}

	private void registerModes()
	{
		final CrazyArena plugin = CrazyArena.getPlugin();
		modeCommand.addMode(new IntegerMode(plugin, "minParticipants")
		{

			@Override
			public Integer getValue()
			{
				return minParticipants;
			}

			@Override
			public void setValue(final Integer newValue) throws CrazyException
			{
				minParticipants = newValue;
				saveToFile();
			}
		});
		modeCommand.addMode(new IntegerMode(plugin, "startDelay")
		{

			@Override
			public Integer getValue()
			{
				return startDelay;
			}

			@Override
			public void setValue(final Integer newValue) throws CrazyException
			{
				startDelay = newValue;
				saveToFile();
			}
		});
		modeCommand.addMode(new LongMode(plugin, "kickSlowPlayers")
		{

			@Override
			public Long getValue()
			{
				return kickSlowPlayers;
			}

			@Override
			public void setValue(final Long newValue) throws CrazyException
			{
				kickSlowPlayers = newValue;
				saveToFile();
			}
		});
	}

	@Override
	public final String getType()
	{
		return "Race";
	}

	@Override
	protected boolean checkArena(final CommandSender sender)
	{
		if (minParticipants < 1)
			return false;
		else if (playerSpawns.size() < minParticipants)
			return false;
		else if (spectatorSpawns.size() == 0)
			return false;
		else if (stages.size() == 0)
			return false;
		else
			return true;
	}

	@Override
	protected void save()
	{
		config.set("minParticipants", minParticipants);
		config.set("startDelay", startDelay);
		config.set("kickSlowPlayers", kickSlowPlayers);
		config.set("players", null);
		// Player Spawns
		int i = 0;
		for (final Location location : playerSpawns)
			ObjectSaveLoadHelper.saveLocation(config, "players.spawn" + (i++) + ".", location, true, true);
		config.set("spectators", null);
		// Spectator Spawns
		i = 0;
		for (final Location location : spectatorSpawns)
			ObjectSaveLoadHelper.saveLocation(config, "spectators.spawn" + (i++) + ".", location, true, true);
		// Race Stages
		config.set("stages", null);
		i = 0;
		for (final RaceStage target : stages)
			target.save(config, "stages.stage" + (i++) + ".");
		// Scores
		currentScore.save(config, "currentScore.", false, true);
		permanentScore.save(config, "permanentScore.");
		config.set("scoreExpiringTime", permanentScore.getExpiringTime());
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
	@Localized({ "CRAZYARENA.ARENA_RACE.PARTICIPANT.JOINED.BROADCAST $Player$", "CRAZYARENA.ARENA_RACE.PARTICIPANT.JOINED.READYCOMMAND" })
	public boolean join(final Player player, final boolean rejoin) throws CrazyException
	{
		RaceParticipant participant = getParticipant(player);
		final Location location = getEmptyStartLocation();
		if (participant == null)
			if (location == null)
				throw new CrazyArenaExceedingParticipantsLimitException(this, playerSpawns.size());
			else
			{
				participant = new RaceParticipant(player, this, location, stages.get(0));
				participants.put(player.getName().toLowerCase(), participant);
			}
		if (rejoin && status == ArenaStatus.PLAYING)
			participant.setParticipantType(ParticipantType.PARTICIPANT);
		else
			participant.setParticipantType(ParticipantType.SELECTING);
		if (status == ArenaStatus.READY)
			status = ArenaStatus.SELECTING;
		broadcastLocaleMessage(false, "PARTICIPANT.JOINED.BROADCAST", player.getName());
		sendLocaleMessage("PARTICIPANT.JOINED.READYCOMMAND", player);
		return true;
	}

	@Override
	@Localized({ "CRAZYARENA.ARENA_RACE.PARTICIPANT.READY", "CRAZYARENA.ARENA_RACE.START.QUEUED", "CRAZYARENA.ARENA_RACE.START.COUNTDOWN $Remaining$", "CRAZYARENA.ARENA_RACE.START.STARTED $DateTime$", "CRAZYARENA.ARENA_RACE.START.ABORTED", "CRAZYARENA.ARENA_RACE.PARTICIPANT.READY.BROADCAST $Selecting$" })
	public boolean ready(final Player player)
	{
		final RaceParticipant participant = getParticipant(player);
		if (status != ArenaStatus.SELECTING)
			return false;
		if (participant.getParticipantType() == ParticipantType.SELECTING)
		{
			player.teleport(participant.getStart(), TeleportCause.PLUGIN);
			sendLocaleMessage("PARTICIPANT.READY", player);
			participant.setParticipantType(ParticipantType.READY);
			if (getParticipants(ParticipantType.READY).size() >= minParticipants)
			{
				final Set<String> selecting = getParticipatingPlayerNames(ParticipantType.SELECTING);
				if (selecting.size() == 0)
				{
					status = ArenaStatus.WAITING;
					broadcastLocaleMessage(false, "START.QUEUED");
					registerMatchListener();
					CountDownTask.startCountDown(startDelay, this, "START.COUNTDOWN", new Runnable()
					{

						@Override
						public void run()
						{
							if (status == ArenaStatus.WAITING)
							{
								status = ArenaStatus.PLAYING;
								startTime = System.currentTimeMillis();
								for (final RaceParticipant participant : getParticipants(ParticipantType.READY))
								{
									participant.setParticipantType(ParticipantType.PARTICIPANT);
									participant.getPlayer().teleport(participant.getStart(), TeleportCause.PLUGIN);
									currentScore.addScore(participant).setValue("nextstageindex", 1);
								}
								currentScore.updateSigns();
								broadcastLocaleMessage(false, "START.STARTED", CrazyLightPluginInterface.DATETIMEFORMAT.format(startTime));
							}
							else
								broadcastLocaleMessage(false, "START.ABORTED");
						}
					});
				}
				else
					broadcastLocaleMessage(false, ParticipantType.READY, "PARTICIPANT.READY.BROADCAST", ChatHelper.listingString(selecting));
			}
			return true;
		}
		return false;
	}

	@Override
	@Localized({ "CRAZYARENA.ARENA_RACE.PARTICIPANT.QUIT $Arena$", "CRAZYARENA.ARENA_RACE.PARTICIPANT.QUIT.BROADCAST $Player$" })
	public boolean leave(final Player player, final boolean kicked)
	{
		final RaceParticipant participant = participants.remove(player.getName().toLowerCase());
		if (participant == null)
			return true;
		quitLocation.remove(player.getName().toLowerCase());
		final ArenaPlayerSaver saver = participant.getSaver();
		saver.restore(player);
		sendLocaleMessage("PARTICIPANT.QUIT", player, name);
		broadcastLocaleMessage(false, "PARTICIPANT.QUIT.BROADCAST", player.getName());
		if (getParticipants(ParticipantType.PARTICIPANT).size() == 0)
			stop();
		return true;
	}

	@Override
	public boolean quitgame(final Player player)
	{
		final RaceParticipant participant = getParticipant(player);
		if (participant == null)
			return true;
		quitLocation.put(player.getName().toLowerCase(), player.getLocation().clone());
		final ArenaPlayerSaver saver = participant.getSaver();
		saver.restore(player);
		saver.reset();
		participant.setParticipantType(ParticipantType.QUITEDPARTICIPANT);
		if (getParticipants(ParticipantType.PARTICIPANT).size() == 0)
			stop();
		return true;
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

	@Localized({ "CRAZYARENA.ARENA_RACE.PARTICIPANT.REACHEDSTAGE.FIRST.BROADCAST $Name$ $Stage$ $Duration$", "CRAZYARENA.ARENA_RACE.PARTICIPANT.REACHEDSTAGE.FIRST.MESSAGE $NextStage$ $Location$ $Distance$", "CRAZYARENA.ARENA_RACE.PARTICIPANT.REACHEDSTAGE.OTHER.BROADCAST $Name$ $Stage$ $Position$ $Duration$ $BehindFirst$", "CRAZYARENA.ARENA_RACE.PARTICIPANT.REACHEDSTAGE.OTHER.MESSAGE $NextStage$ $Location$ $Distance$ $Position$ $Previous$ $BehindPrevious$", "CRAZYARENA.ARENA_RACE.PARTICIPANT.REACHEDSTAGE.PREVIOUS.MESSAGE $Follower$ $BehindPrevious$" })
	public void reachStage(final RaceParticipant raceParticipant, final RaceStage stage)
	{
		final RaceData data = stage.reachStage(raceParticipant, System.currentTimeMillis() - startTime);
		final ScoreEntry score = currentScore.getScore(raceParticipant);
		score.addValue("nextstageindex", 1);
		score.addValue("timeelapsed", data.getTime());
		if (stage.isGoal())
		{
			score.setString("nextstage", "-");
			currentScore.updateSigns();
			reachFinish(raceParticipant, data, stage.getDatas().get(0));
		}
		else
		{
			final RaceStage next = stage.getNext();
			score.setString("nextstage", next.getName());
			currentScore.updateSigns();
			final int position = data.getPosition();
			final double dist = Math.round(raceParticipant.getPlayer().getLocation().distance(next.getZone().getBasis()) * 10) / 10D;
			if (position == 1)
			{
				broadcastLocaleMessage(false, true, true, true, "PARTICIPANT.REACHEDSTAGE.FIRST.BROADCAST", raceParticipant.getName(), stage.toShortString(), data.getTimeString());
				sendLocaleMessage("PARTICIPANT.REACHEDSTAGE.FIRST.MESSAGE", raceParticipant, next.toShortString(), ArenaChatHelper.locationConverter(next.getZone().getBasis()), dist);
			}
			else
			{
				final RaceData first = stage.getDatas().get(0);
				broadcastLocaleMessage(false, false, true, true, "PARTICIPANT.REACHEDSTAGE.OTHER.BROADCAST", raceParticipant.getName(), stage.toShortString(), position, data.getTimeString(), data.getTimeString(first));
				final RaceData previous = stage.getDatas().get(position - 2);
				sendLocaleMessage("PARTICIPANT.REACHEDSTAGE.OTHER.MESSAGE", raceParticipant, next.toShortString(), ArenaChatHelper.locationConverter(next.getZone().getBasis()), dist, position, previous.getName(), data.getTimeString(previous));
				sendLocaleMessage("PARTICIPANT.REACHEDSTAGE.PREVIOUS.MESSAGE", previous.getParticipant(), raceParticipant.getName(), data.getTimeString(previous));
			}
		}
	}

	@Localized({ "CRAZYARENA.ARENA_RACE.PARTICIPANT.REACHEDFINISH $Name$ $Position$ $Time$", "CRAZYARENA.ARENA_RACE.PARTICIPANT.TOOSLOW $Name$", "CRAZYARENA.ARENA_RACE.FINISHED $Name$ $Time$", "CRAZYARENA.ARENA_RACE.FINISHED.END $Name$ $Time$" })
	public void reachFinish(final RaceParticipant raceParticipant, final RaceData data, final RaceData winner)
	{
		raceParticipant.setParticipantType(ParticipantType.WINNER);
		final int position = data.getPosition();
		final String time = data.getTimeString();
		broadcastLocaleMessage(false, true, true, true, "PARTICIPANT.REACHEDFINISH", raceParticipant.getName(), position, time);
		final int run = runnumber;
		if (position == 1)
		{
			broadcastLocaleMessage(true, true, true, true, "FINISHED", winner.getName(), winner.getTimeString());
			Bukkit.getScheduler().scheduleSyncDelayedTask(getArenaPlugin(), new Runnable()
			{

				@Override
				public void run()
				{
					if (run == runnumber)
					{
						for (final RaceParticipant participant : getParticipants(ParticipantType.PARTICIPANT))
						{
							participant.setParticipantType(ParticipantType.DEFEADED);
							broadcastLocaleMessage(false, true, true, true, "PARTICIPANT.TOOSLOW", participant.getName());
						}
						raceEnd(winner);
					}
				}
			}, kickSlowPlayers * 20);
		}
		if (getParticipants(ParticipantType.PARTICIPANT).size() == 0)
			raceEnd(winner);
	}

	private final void raceEnd(final RaceData winner)
	{
		runnumber++;
		final List<RaceData> datas = winner.getStage().getDatas();
		final Date now = new Date();
		final String date = CrazyLightPluginInterface.DATEFORMAT.format(now);
		final String time = CrazyLightPluginInterface.TIMEFORMAT.format(now);
		for (final RaceData data : datas)
		{
			final ScoreEntry score = permanentScore.getOrAddScore(data.getName());
			if (score.setValueIfLowerOrZero("timeelapsed", data.getTime()))
			{
				score.setValue("opponents", datas.size() - 1);
				score.setString("date", date);
				score.setString("time", time);
			}
			score.setValueIfLowerOrZero("leadfirst", data.getTime(winner) + 1);
		}
		permanentScore.updateSigns();
		broadcastLocaleMessage(false, true, true, true, "FINISHED.END", winner.getName(), winner.getTimeString());
		stop();
		saveToFile();
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
		// EDIT Implementiere RaceArena.registerArenaListener()
	}

	@Override
	public void unregisterArenaListener()
	{
	}

	@Override
	public void stop()
	{
		unregisterMatchListener();
		quitLocation.clear();
		for (final RaceStage stage : stages)
			stage.getDatas().clear();
		currentScore.clear();
		super.stop();
	}

	@Override
	public CrazyArenaRace getArenaPlugin()
	{
		return CrazyArenaRace.getPlugin();
	}

	@Override
	public void attachSign(final Block block, final SignRotation rotation, final String type, final Player player)
	{
		if (block == null)
			return;
		if (rotation == null)
			return;
		if (type.equals("") || type.equals("score") || type.equals("currentscore") | type.equals("ranks"))
			currentScore.getSigns().add(block.getLocation());
		else
			permanentScore.getSigns().add(block.getLocation());
		saveToFile();
	}

	public List<Location> getPlayerSpawns()
	{
		return playerSpawns;
	}

	public SpawnList getSpectatorSpawns()
	{
		return spectatorSpawns;
	}

	public List<RaceStage> getStages()
	{
		return stages;
	}
}
