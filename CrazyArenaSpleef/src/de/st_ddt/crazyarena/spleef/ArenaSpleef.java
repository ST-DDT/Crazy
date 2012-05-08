package de.st_ddt.crazyarena.spleef;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.participants.Participant;
import de.st_ddt.crazyarena.participants.ParticipantType;
import de.st_ddt.crazyarena.utils.SpawnList;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.geo.Geo;

public class ArenaSpleef extends Arena
{

	protected Geo arena;
	protected Geo out;
	protected final SpawnList arenaspawns = new SpawnList(world);
	protected final SpawnList spectatorspawns = new SpawnList(world);
	protected boolean running;
	protected int round;
	private long maxTime;
	protected int run;
	private ArenaSpleefTimeOut timeOut;
	// Listener
	private ArenaSpleefEntityListener entityListener;

	public ArenaSpleef(FileConfiguration config)
	{
		super(config);
		arena = Geo.load(config.getConfigurationSection("arena"), world);
		for (String name : config.getConfigurationSection("arenaspawns").getKeys(false))
			arenaspawns.add(ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection("arenaspawns." + name), world));
		for (String name : config.getConfigurationSection("spectatorspawns").getKeys(false))
			spectatorspawns.add(ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection("spectatorspawns." + name), world));
	}

	public ArenaSpleef(String name, World world)
	{
		super(name, world);
	}

	@Override
	public void join(Player player, boolean rejoin) throws CrazyCommandException
	{
		if (!rejoin && isRunning())
			throw new CrazyCommandCircumstanceException("when arena is idle");
		plugin.sendLocaleMessage("ARENASPLEEF.JOIN", player, name);
		plugin.sendLocaleMessage("ARENASPLEEF.JOIN.OTHERS", participants.getPlayers(), player.getName());
		Participant participant = participants.getParticipant(player);
		if (participant == null)
		{
			participant = new Participant(player, this);
			participants.add(participant);
		}
		participant.getSaver().backup();
		participant.getSaver().clear();
		if (participant.getParticipantType() == ParticipantType.NONE || !isRunning())
			participant.setParticipantType(ParticipantType.WAITING);
		else
			participant.setParticipantType(ParticipantType.PARTICIPANT);
	}

	@Override
	public void ready(Player player) throws CrazyCommandException
	{
		Participant participant = participants.getParticipant(player);
		participant.setParticipantType(ParticipantType.PARTICIPANT);
		plugin.sendLocaleMessage("ARENASPLEEF.READY", player, name);
		arenaspawns.teleport(player);
		start(null, false);
	}

	@Override
	public void leave(Player player, boolean kicked) throws CrazyCommandException
	{
		if (isRunning() && !kicked)
			throw new CrazyCommandCircumstanceException("when match is over");
		Participant participant = participants.getParticipant(player);
		participant.getSaver().restore();
		participants.remove(participant);
		player.teleport(spectatorspawns.get(0));
		stop(null, false);
	}

	@Override
	public void spectate(Player player) throws CrazyCommandException
	{
		Participant participant = participants.getParticipant(player);
		if (participant == null)
		{
			participant = new Participant(player, this);
			participants.add(participant);
		}
		participant.setParticipantType(ParticipantType.SPECTATOR);
		spectatorspawns.teleport(player);
	}

	@Override
	protected boolean checkStart()
	{
		if (running)
			return false;
		if (participants.getParticipants(ParticipantType.WAITING).size() != 0)
			return false;
		return true;
	}

	@Override
	public void start(CommandSender sender, boolean force)
	{
		if (isRunning())
			return;
		if (!checkStart() && !force)
			return;
		for (Participant participant : participants.getParticipants(ParticipantType.WAITING))
			try
			{
				leave(participant.getPlayer(), true);
			}
			catch (CrazyCommandException e)
			{}
		running = true;
		run++;
		plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new ArenaSpleefCounter(this, "STARTTIMER", 5), 20);
		plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new ArenaSpleefCounter(this, "STARTTIMER", 4), 40);
		plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new ArenaSpleefCounter(this, "STARTTIMER", 3), 60);
		plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new ArenaSpleefCounter(this, "STARTTIMER", 2), 80);
		plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new ArenaSpleefCounter(this, "STARTTIMER", 1), 100);
		plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new ArenaSpleefGoTimer(this), 120);
	}

	private void registerGameHooks()
	{
		unregisterGameHooks();
		PluginManager pm = plugin.getServer().getPluginManager();
		entityListener = new ArenaSpleefEntityListener(this);
		pm.registerEvents(entityListener, plugin);
	}

	private void unregisterGameHooks()
	{
		if (entityListener != null)
			HandlerList.unregisterAll(entityListener);
		entityListener = null;
	}

	public void delayedStart()
	{
		registerGameHooks();
		long endTime = maxTime * 20;
		timeOut = new ArenaSpleefTimeOut(this);
		plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, timeOut, endTime);
	}

	@Override
	protected boolean checkFinished()
	{
		if (isRunning() && participants.getParticipants(ParticipantType.PARTICIPANT).size() == 0)
			return true;
		return false;
	}

	@Override
	public void stop(CommandSender sender, boolean force)
	{
		if (!checkFinished() && !force)
			return;
		running = false;
		if (timeOut != null)
		{
			timeOut.cancel();
			timeOut = null;
		}
		unregisterGameHooks();
		for (Participant participant : participants.getParticipants(ParticipantType.PARTICIPANT))
		{
			participant.getSaver().restore();
			participant.setParticipantType(ParticipantType.SPECTATOR);
			spectatorspawns.teleport(participant.getPlayer());
		}
	}

	@Override
	public String getArenaType()
	{
		return "Spleef";
	}

	@Override
	public boolean isRunning()
	{
		return running;
	}

	@Override
	public boolean checkArena(CommandSender sender)
	{
		if (arena == null)
		{
			sendLocaleMessage("ARENASPLEEF.CHECK.NOARENAREGION", sender);
			return false;
		}
		if (out == null)
		{
			plugin.sendLocaleMessage("ARENASPLEEF.CHECK.NOARENAREGION", sender);
			return false;
		}
		if (arenaspawns.size() == 0)
		{
			plugin.sendLocaleMessage("ARENASPLEEF.CHECK.NOARENASPAWNS", sender);
			return false;
		}
		
		if (spectatorspawns.size() == 0)
		{
			plugin.sendLocaleMessage("ARENASPLEEF.CHECK.NOSPECTATORSPAWNS", sender);
			return false;
		}
		return true;
	}

	public void PlayerDeath(final Player player)
	{
		Participant participant = getParticipants().getParticipant(player);
		if (participant == null)
			return;
		if (participant.getParticipantType() != ParticipantType.PARTICIPANT)
			return;
		participant.getSaver().restore();
		participant.setParticipantType(ParticipantType.SPECTATOR);
		spectatorspawns.teleport(player);
		stop(null, false);
	}

	@Override
	public void enable()
	{
		// EDIT registerArenaHooks();
	}

	@Override
	public void disable()
	{
		stop(null, true);
		unregisterGameHooks();
		// unregisterArenaHooks();
	}

	@Override
	public int getRunNumber()
	{
		return run;
	}

	@Override
	protected String getArenaTypeLocaleDefault()
	{
		return "ARENASPLEEF";
	}

	public Geo getRegion()
	{
		return region;
	}

	public Geo getArena()
	{
		return arena;
	}

	public Geo getOut()
	{
		return out;
	}
}
