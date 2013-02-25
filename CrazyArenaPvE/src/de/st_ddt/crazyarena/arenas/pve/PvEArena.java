package de.st_ddt.crazyarena.arenas.pve;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import de.st_ddt.crazyarena.CrazyArenaPlugin;
import de.st_ddt.crazyarena.CrazyArenaPvE;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.arenas.ArenaStatus;
import de.st_ddt.crazyarena.arenas.pve.rounds.Round;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyarena.participants.pve.PvEParticipant;
import de.st_ddt.crazyarena.tasks.CountDownTask;
import de.st_ddt.crazyarena.utils.ArenaPlayerSaver;
import de.st_ddt.crazyarena.utils.SpawnList;
import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.LocalizedVariable;

@LocalizedVariable(variables = "ARENA_TYPE", values = "PVE")
public class PvEArena extends Arena<PvEParticipant>
{

	protected final SpawnList playerSpawns = new SpawnList();
	protected final SpawnList monsterSpawns = new SpawnList();
	protected final List<Round> rounds = new ArrayList<Round>();
	protected double antiSpawnRadius;
	protected double activeSpawnRadius;
	protected Location lobby;
	protected int runnumber;
	protected int roundnumber;
	protected int minParticipants;
	protected int maxParticipants;
	protected long startTime;
	protected int startDelay;

	public PvEArena(final String name)
	{
		super(name);
	}

	public PvEArena(final String name, final ConfigurationSection config)
	{
		super(name, config);
	}

	@Override
	public String getType()
	{
		return "PvE";
	}

	@Override
	protected void save()
	{
		// EDIT Implementiere Arena<PvEParticipant>.save()
	}

	@Override
	@Localized({ "CRAZYARENA.ARENA_PVE.PARTICIPANT.JOINED.BROADCAST $Player$", "CRAZYARENA.ARENA_PVE.PARTICIPANT.JOINED.READYCOMMAND" })
	public boolean join(final Player player, final boolean rejoin) throws CrazyException
	{
		PvEParticipant participant = getParticipant(player);
		if (participant == null)
		{
			participant = new PvEParticipant(player, this);
			participants.put(player.getName().toLowerCase(), participant);
		}
		if (rejoin && status == ArenaStatus.PLAYING)
		{
			participant.setParticipantType(ParticipantType.PARTICIPANT);
			player.teleport(playerSpawns.randomSpawn());
		}
		else
		{
			participant.setParticipantType(ParticipantType.SELECTING);
			player.teleport(lobby, TeleportCause.PLUGIN);
		}
		if (status == ArenaStatus.READY)
			status = ArenaStatus.SELECTING;
		broadcastLocaleMessage(false, "PARTICIPANT.JOINED.BROADCAST", player.getName());
		sendLocaleMessage("PARTICIPANT.JOINED.READYCOMMAND", player);
		return true;
	}

	@Override
	@Localized({ "CRAZYARENA.ARENA_PVE.PARTICIPANT.READY", "CRAZYARENA.ARENA_PVE.START.QUEUED", "CRAZYARENA.ARENA_PVE.START.COUNTDOWN $Remaining$", "CRAZYARENA.ARENA_PVE.START.STARTED $DateTime$", "CRAZYARENA.ARENA_PVE.START.ABORTED", "CRAZYARENA.ARENA_PVE.PARTICIPANT.READY.BROADCAST $Selecting$" })
	public boolean ready(final Player player)
	{
		final PvEParticipant participant = getParticipant(player);
		if (status != ArenaStatus.SELECTING)
			return false;
		if (participant.getParticipantType() == ParticipantType.SELECTING)
		{
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
								for (final PvEParticipant participant : getParticipants(ParticipantType.READY))
								{
									participant.setParticipantType(ParticipantType.PARTICIPANT);
									participant.getPlayer().teleport(playerSpawns.randomSpawn(), TeleportCause.PLUGIN);
								}
								broadcastLocaleMessage(false, "START.STARTED", CrazyLightPluginInterface.DATETIMEFORMAT.format(startTime));
								roundnumber = 0;
								nextRound();
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
	@Localized({ "CRAZYARENA.ARENA_PVE.PARTICIPANT.QUIT $Arena$", "CRAZYARENA.ARENA_PVE.PARTICIPANT.QUIT.BROADCAST $Player$" })
	public boolean leave(final Player player, final boolean kicked) throws CrazyException
	{
		final PvEParticipant participant = participants.remove(player.getName().toLowerCase());
		if (participant == null)
			return true;
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
		final PvEParticipant participant = getParticipant(player);
		if (participant == null)
			return true;
		final ArenaPlayerSaver saver = participant.getSaver();
		saver.restore(player);
		saver.reset();
		participant.setParticipantType(ParticipantType.QUITEDPARTICIPANT);
		if (getParticipants(ParticipantType.PARTICIPANT).size() == 0)
			stop();
		return true;
	}

	protected void nextRound()
	{
		rounds.get(roundnumber).next();
		roundnumber++;
		if (roundnumber < rounds.size())
		{
			final Round round = rounds.get(roundnumber);
			round.activate(roundnumber);
			broadcastLocaleMessage(false, "ROUND.START", round.getType(), roundnumber, rounds.size());
		}
		else
			fightFinished();
	}

	protected void fightFinished()
	{
		broadcastLocaleMessage(false, "ROUND.END", rounds.size());
		fightEnd(true);
	}

	protected void fightEnd(final boolean won)
	{
		// EDIT Implementiere PvEArena.fightEnd()
		// Give Rewards
		stop();
	}

	@Override
	public void stop()
	{
		super.stop();
		for (final Round round : rounds)
			round.reset();
	}

	public void creatureDeath(final LivingEntity entity)
	{
		rounds.get(roundnumber).creatureDeath(entity);
	}

	public void playerDeath(final Player player)
	{
		// EDIT Implementiere PvEArena.playerDeath()
	}

	@Override
	public void registerMatchListener()
	{
		// EDIT Implementiere PvEArena.registerMatchListener()
	}

	@Override
	public void unregisterMatchListener()
	{
		// EDIT Implementiere PvEArena.unregisterMatchListener()
	}

	@Override
	public void registerArenaListener()
	{
		// EDIT Implementiere PvEArena.registerArenaListener()
	}

	@Override
	public void unregisterArenaListener()
	{
		// EDIT Implementiere PvEArena.unregisterArenaListener()
	}

	@Override
	public final int getRunNumber()
	{
		return runnumber;
	}

	@Override
	public long getRejoinTime()
	{
		return 30000;
	}

	public SpawnList getActiveMonsterSpawns()
	{
		return monsterSpawns;
	}

	@Override
	protected boolean checkArena(final CommandSender sender)
	{
		if (playerSpawns.size() < maxParticipants)
			return false;
		if (monsterSpawns.size() == 0)
			return false;
		// EDIT Implementiere PvEArena.checkArena()
		return true;
	}

	@Override
	public final CrazyArenaPlugin getArenaPlugin()
	{
		return CrazyArenaPvE.getPlugin();
	}
}
