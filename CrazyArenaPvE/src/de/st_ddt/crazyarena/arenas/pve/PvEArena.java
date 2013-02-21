package de.st_ddt.crazyarena.arenas.pve;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import de.st_ddt.crazyarena.CrazyArenaPlugin;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.arenas.ArenaStatus;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyarena.participants.pve.PvEParticipant;
import de.st_ddt.crazyarena.tasks.CountDownTask;
import de.st_ddt.crazyarena.utils.SpawnList;
import de.st_ddt.crazyplugin.CrazyLightPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;

public class PvEArena extends Arena<PvEParticipant>
{

	protected final SpawnList playerSpawns = new SpawnList();
	protected Location lobby;
	protected int minParticipants;
	protected int maxParticipants;
	private long startTime;
	private int startDelay;

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
	@Localized({ "CRAZYARENA.ARENA_RACE.PARTICIPANT.READY", "CRAZYARENA.ARENA_RACE.START.QUEUED", "CRAZYARENA.ARENA_RACE.START.COUNTDOWN $Remaining$", "CRAZYARENA.ARENA_RACE.START.STARTED $DateTime$", "CRAZYARENA.ARENA_RACE.START.ABORTED", "CRAZYARENA.ARENA_RACE.PARTICIPANT.READY.BROADCAST $Selecting$" })
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
								// This arena requires a real start or something
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
	public boolean leave(final Player player, final boolean kicked) throws CrazyException
	{
		// EDIT Implementiere Arena<PvEParticipant>.leave()
		return false;
	}

	@Override
	public boolean quitgame(final Player player)
	{
		// EDIT Implementiere Arena<PvEParticipant>.quitgame()
		return false;
	}

	@Override
	public void registerMatchListener()
	{
		// EDIT Implementiere Arena<PvEParticipant>.registerMatchListener()
	}

	@Override
	public void unregisterMatchListener()
	{
		// EDIT Implementiere Arena<PvEParticipant>.unregisterMatchListener()
	}

	@Override
	public void registerArenaListener()
	{
		// EDIT Implementiere Arena<PvEParticipant>.registerArenaListener()
	}

	@Override
	public void unregisterArenaListener()
	{
		// EDIT Implementiere Arena<PvEParticipant>.unregisterArenaListener()
	}

	@Override
	public int getRunNumber()
	{
		// EDIT Implementiere Arena<PvEParticipant>.getRunNumber()
		return 0;
	}

	@Override
	public long getRejoinTime()
	{
		// EDIT Implementiere Arena<PvEParticipant>.getRejoinTime()
		return 0;
	}

	@Override
	protected boolean checkArena(final CommandSender sender)
	{
		// EDIT Implementiere Arena<PvEParticipant>.checkArena()
		return false;
	}

	@Override
	public CrazyArenaPlugin getArenaPlugin()
	{
		// EDIT Implementiere Arena<PvEParticipant>.getArenaPlugin()
		return null;
	}
}
